package com.masterquentus.projectlilith.item.armor;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

@EventBusSubscriber(modid = ProjectLilith.MOD_ID)
public class ArmorSetBonuses {

    // Abyssal Surge tuning (the tooltip reads these, so the text always matches)
    public static final float SURGE_HEALTH_FRACTION = 0.30F;  // triggers at/below 30% health
    public static final int SURGE_DURATION_TICKS = 160;       // 8 seconds of Speed II
    public static final int SURGE_COOLDOWN_TICKS = 1200;      // 60 second cooldown

    private static boolean wearing(LivingEntity entity, EquipmentSlot slot, Item item) {
        return entity.getItemBySlot(slot).is(item);
    }

    public static boolean hasHelmet(LivingEntity e) {
        return wearing(e, EquipmentSlot.HEAD, ModItems.ABYSSAL_HELMET.get());
    }

    public static boolean hasChestplate(LivingEntity e) {
        return wearing(e, EquipmentSlot.CHEST, ModItems.ABYSSAL_CHESTPLATE.get());
    }

    public static boolean hasLegsAndBoots(LivingEntity e) {
        return wearing(e, EquipmentSlot.LEGS, ModItems.ABYSSAL_LEGGINGS.get())
                && wearing(e, EquipmentSlot.FEET, ModItems.ABYSSAL_BOOTS.get());
    }

    public static boolean hasFullSet(LivingEntity e) {
        return hasHelmet(e) && hasChestplate(e) && hasLegsAndBoots(e);
    }

    // Leggings + boots: no fire or burning damage
    @SubscribeEvent
    public static void onIncomingDamage(LivingIncomingDamageEvent event) {
        LivingEntity entity = event.getEntity();
        if (!hasLegsAndBoots(entity)) return;

        DamageSource source = event.getSource();
        if (source.is(DamageTypes.IN_FIRE) || source.is(DamageTypes.ON_FIRE)) {
            event.setCanceled(true);
            entity.clearFire();   // also removes the flames and the burn timer
        }
    }

    // Helmet: immune to Darkness
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player
                && hasHelmet(player)
                && event.getEffectInstance().getEffect() == MobEffects.DARKNESS) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    // Chestplate: heal 8% of melee damage YOU DEAL
    @SubscribeEvent
    public static void onDamageDealt(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player
                && source.getDirectEntity() == player
                && hasChestplate(player)) {
            player.heal(event.getNewDamage() * 0.08F);
        }
    }

    // Full set: "Abyssal Surge" - Speed II when your health is at/below 30%.
    // Checked a few times a second while you wear the set, so it does not depend on
    // how or when the damage happened (hits, Wither, fire, a command, anything).
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;
        if (player.tickCount % 5 != 0 || !player.isAlive()) return;
        if (!hasFullSet(player)) return;
        if (player.getHealth() > player.getMaxHealth() * SURGE_HEALTH_FRACTION) return;

        // The chestplate doubles as the cooldown timer (shows the usual cooldown sweep on the item)
        Item cooldownItem = ModItems.ABYSSAL_CHESTPLATE.get();
        if (player.getCooldowns().isOnCooldown(cooldownItem)) return;

        player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, SURGE_DURATION_TICKS, 1));
        player.getCooldowns().addCooldown(cooldownItem, SURGE_COOLDOWN_TICKS);

        ServerLevel level = player.serverLevel();
        level.playSound(null, player.blockPosition(), SoundEvents.WARDEN_HEARTBEAT, SoundSource.PLAYERS, 1.0F, 1.0F);
        level.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                player.getX(), player.getY() + 1.0, player.getZ(),
                25, 0.4, 0.8, 0.4, 0.05);
        player.displayClientMessage(
                Component.literal("Abyssal Surge!").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD), true);
    }
}