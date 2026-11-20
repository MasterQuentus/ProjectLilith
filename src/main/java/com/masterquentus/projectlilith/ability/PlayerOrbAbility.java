package com.masterquentus.projectlilith.ability;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.entity.ModEntities;
import com.masterquentus.projectlilith.item.entity.HellfireProjectile;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.server.ServerStoppingEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Hellfire Orbs: a player who has signed a Lilith Contract can summon orbs that circle them
 * (key 1) and launch them one at a time (key 2). Everything here runs on the server.
 */
@EventBusSubscriber(modid = ProjectLilith.MOD_ID)
public class PlayerOrbAbility {

    // ---- Tuning ----
    public static final int ORB_COUNT = 3;
    public static final int ORB_DURATION_TICKS = 400;       // unused orbs fade after 20 seconds
    public static final int COOLDOWN_TICKS = 600;           // 30 seconds after you fire any orb
    public static final int UNUSED_COOLDOWN_TICKS = 100;    // 5 seconds if you dismiss them without firing
    public static final int FLIGHT_TICKS = 100;             // a fired orb is removed after 5 seconds
    private static final double ORBIT_RADIUS = 1.8;
    private static final double ORBIT_HEIGHT = 1.1;
    private static final double ORBIT_SPEED = 0.12;         // radians per tick
    private static final double LAUNCH_SPEED = 1.4;

    private static class OrbState {
        final List<Entity> orbs = new ArrayList<>();   // orbs still circling the player
        int ticksLeft = ORB_DURATION_TICKS;
        boolean firedAny = false;
    }

    private static final Map<UUID, OrbState> ACTIVE = new HashMap<>();
    private static final Map<UUID, Long> COOLDOWN_UNTIL = new HashMap<>();
    private static final Map<Entity, Integer> FLYING = new HashMap<>();   // fired orbs and their remaining life

    // ---------------------------------------------------------------------
    // Packet from the client (registered in ModNetwork)
    // ---------------------------------------------------------------------
    public static void handlePayload(OrbAbilityPayload payload, IPayloadContext context) {
        context.enqueueWork(() -> {
            if (!(context.player() instanceof ServerPlayer player)) return;
            if (!player.isAlive() || player.isSpectator()) return;
            switch (payload.action()) {
                case OrbAbilityPayload.TOGGLE -> toggle(player);
                case OrbAbilityPayload.FIRE -> fire(player);
                default -> { }
            }
        });
    }

    private static void say(ServerPlayer player, String text, ChatFormatting color) {
        player.displayClientMessage(Component.literal(text).withStyle(color), true);
    }

    // ---------------------------------------------------------------------
    // Toggle: summon or dismiss
    // ---------------------------------------------------------------------
    private static void toggle(ServerPlayer player) {
        if (!player.getData(ModAttachments.HELLFIRE_ORBS_LEARNED)) {
            say(player, "You have not learned this. Right-click a Lilith Contract.", ChatFormatting.GRAY);
            return;
        }
        if (ACTIVE.containsKey(player.getUUID())) {
            dismiss(player, true);
            return;
        }

        long now = player.serverLevel().getGameTime();
        Long ready = COOLDOWN_UNTIL.get(player.getUUID());
        if (ready != null && now < ready) {
            say(player, "Hellfire Orbs recharging (" + ((ready - now + 19) / 20) + "s)", ChatFormatting.GRAY);
            return;
        }
        summon(player);
    }

    private static void summon(ServerPlayer player) {
        ServerLevel level = player.serverLevel();
        OrbState state = new OrbState();

        for (int i = 0; i < ORB_COUNT; i++) {
            HellfireProjectile orb = new HellfireProjectile(ModEntities.HELLFIRE_PROJECTILE.get(), level);
            orb.setOwner(player);
            orb.setPos(player.getX(), player.getY() + ORBIT_HEIGHT, player.getZ());
            orb.setDeltaMovement(Vec3.ZERO);
            level.addFreshEntity(orb);
            state.orbs.add(orb);
        }
        ACTIVE.put(player.getUUID(), state);

        level.playSound(null, player.blockPosition(), SoundEvents.FIRECHARGE_USE, SoundSource.PLAYERS, 1.0F, 0.8F);
        say(player, "Hellfire Orbs summoned", ChatFormatting.DARK_RED);
    }

    // ---------------------------------------------------------------------
    // Fire: launch the first orbiting orb toward where the player is looking
    // ---------------------------------------------------------------------
    private static void fire(ServerPlayer player) {
        OrbState state = ACTIVE.get(player.getUUID());
        if (state == null || state.orbs.isEmpty()) {
            say(player, "Summon your orbs first", ChatFormatting.GRAY);
            return;
        }

        Entity orb = state.orbs.remove(0);
        Vec3 target = player.getEyePosition().add(player.getLookAngle().scale(30.0));
        Vec3 direction = target.subtract(orb.position()).normalize().scale(LAUNCH_SPEED);
        orb.setDeltaMovement(direction);
        orb.hurtMarked = true;

        state.firedAny = true;
        FLYING.put(orb, FLIGHT_TICKS);

        player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.BLAZE_SHOOT, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    // ---------------------------------------------------------------------
    // Dismiss: remove the orbiting orbs and start the cooldown
    // ---------------------------------------------------------------------
    private static void dismiss(ServerPlayer player, boolean announce) {
        OrbState state = ACTIVE.remove(player.getUUID());
        if (state == null) return;

        for (Entity orb : state.orbs) {
            if (orb != null && !orb.isRemoved()) orb.discard();
        }
        state.orbs.clear();

        int cooldown = state.firedAny ? COOLDOWN_TICKS : UNUSED_COOLDOWN_TICKS;
        COOLDOWN_UNTIL.put(player.getUUID(), player.serverLevel().getGameTime() + cooldown);

        if (announce) {
            player.serverLevel().playSound(null, player.blockPosition(), SoundEvents.FIRE_EXTINGUISH, SoundSource.PLAYERS, 1.0F, 1.0F);
            say(player, "Hellfire Orbs dismissed", ChatFormatting.GRAY);
        }
    }

    // ---------------------------------------------------------------------
    // Per-tick: keep the orbs circling the player
    // ---------------------------------------------------------------------
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        OrbState state = ACTIVE.get(player.getUUID());
        if (state == null) return;

        // Drop orbs that were destroyed, or left behind in another dimension
        Iterator<Entity> it = state.orbs.iterator();
        while (it.hasNext()) {
            Entity orb = it.next();
            if (orb == null || orb.isRemoved()) {
                it.remove();
            } else if (orb.level() != player.level()) {
                orb.discard();
                it.remove();
            }
        }

        // End when: dead, all fired, or out of time
        if (!player.isAlive() || player.isSpectator() || state.orbs.isEmpty() || --state.ticksLeft <= 0) {
            dismiss(player, false);
            return;
        }

        int count = state.orbs.size();
        for (int i = 0; i < count; i++) {
            double angle = player.tickCount * ORBIT_SPEED + i * (Math.PI * 2.0 / count);
            state.orbs.get(i).setPos(
                    player.getX() + Math.cos(angle) * ORBIT_RADIUS,
                    player.getY() + ORBIT_HEIGHT,
                    player.getZ() + Math.sin(angle) * ORBIT_RADIUS);
        }
    }

    // Remove fired orbs that never hit anything
    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        if (FLYING.isEmpty()) return;
        Iterator<Map.Entry<Entity, Integer>> it = FLYING.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Entity, Integer> entry = it.next();
            Entity orb = entry.getKey();
            int left = entry.getValue() - 1;
            if (orb.isRemoved()) {
                it.remove();
            } else if (left <= 0) {
                orb.discard();
                it.remove();
            } else {
                entry.setValue(left);
            }
        }
    }

    // ---------------------------------------------------------------------
    // Cleanup so no stray orbs are left in the world
    // ---------------------------------------------------------------------
    @SubscribeEvent
    public static void onLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) dismiss(player, false);
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) dismiss(player, false);
    }

    @SubscribeEvent
    public static void onServerStopping(ServerStoppingEvent event) {
        for (OrbState state : ACTIVE.values()) {
            for (Entity orb : state.orbs) {
                if (orb != null && !orb.isRemoved()) orb.discard();
            }
        }
        for (Entity orb : FLYING.keySet()) {
            if (!orb.isRemoved()) orb.discard();
        }
        ACTIVE.clear();
        FLYING.clear();
        COOLDOWN_UNTIL.clear();
    }
}