package com.masterquentus.projectlilith.item.entity;

import com.masterquentus.projectlilith.Config;
import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.entity.ModEntities;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.TagKey;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.*;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.npc.Villager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animation.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class LilithEntity extends Monster implements GeoEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final List<UUID> spinningFireballUUIDs = new ArrayList<>();
    private int bloodMoonCooldown = 0;
    private int darkVeilCooldown = 0;
    private int shadowDashCooldown = 0;
    private int darkDominionCooldown = 0;
    private int batSwarmCooldown = 0;
    private boolean isUsingDarkDominion = false;
    private int abilityPhaseTimer = 0;
    private static final double MAX_FLOAT_HEIGHT = 12.0;
    private double startingY = -1;
    private int darkDominionRounds = 0;
    private static final int MAX_DOMINION_ROUNDS = 5;
    private int hellfireCooldown = 0;
    private int hellfireSpinTick = 0;
    private final List<Entity> spinningFireballs = new ArrayList<>();

    private boolean isAttacking = false;
    private int attackTimer = 0;
    private int attackType = 1;
    private int noPlayerTicks = 0;

    // Transformation fields
    private boolean isTransformed = false;
    private int transformTimer = 0;
    private final List<LilithBat> activeBats = new ArrayList<>();
    private final List<Monster> darkDominionMinions = new ArrayList<>();

    // === Animations ===
    private static final RawAnimation WALK_ANIM = RawAnimation.begin().thenLoop("animation.lilith.walk");
    private static final RawAnimation ATTACK_ANIM = RawAnimation.begin().thenPlay("animation.lilith.attack");
    private static final RawAnimation ATTACK2_ANIM = RawAnimation.begin().thenPlay("animation.lilith.attack2");
    private static final RawAnimation ATTACK3_ANIM = RawAnimation.begin().thenPlay("animation.lilith.attack3");
    private static final RawAnimation IDLE_ANIM = RawAnimation.begin().thenLoop("animation.lilith.idle");

    public LilithEntity(EntityType<? extends Monster> entityType, Level level) {
        super(entityType, level);
        this.xpReward = 500;
        if (!level.isClientSide()) {
            applyConfigAttributes();
            this.setHealth(this.getMaxHealth());
        }
    }

    private void applyConfigAttributes() {
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(Config.LILITH_MAX_HEALTH.get());
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(Config.LILITH_ATTACK_DAMAGE.get());
        this.getAttribute(Attributes.ARMOR).setBaseValue(Config.LILITH_ARMOR.get());
    }

    public enum BossPhase {
        AWAKENED(1.0f),
        SHADOW_VEIL(0.75f),
        BLOOD_QUEEN(0.50f),
        VOID_EMPRESS(0.25f);

        final float healthThreshold;
        BossPhase(float threshold) { this.healthThreshold = threshold; }
    }

    private BossPhase currentPhase = BossPhase.AWAKENED;

    private final ServerBossEvent bossEvent = new ServerBossEvent(
            Component.translatable("boss.projectlilith.lilith"),
            ServerBossEvent.BossBarColor.RED,
            ServerBossEvent.BossBarOverlay.NOTCHED_10
    );

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        List<UUID> uuidsToSave = new ArrayList<>();
        for (Entity f : spinningFireballs) {
            if (f != null && !f.isRemoved()) {
                uuidsToSave.add(f.getUUID());
            }
        }
        if (uuidsToSave.isEmpty()) {
            uuidsToSave.addAll(spinningFireballUUIDs);
        }
        if (!uuidsToSave.isEmpty()) {
            // Storing UUIDs as a custom tag list in 1.21.1 NBT
            net.minecraft.nbt.ListTag listTag = new net.minecraft.nbt.ListTag();
            for (UUID uuid : uuidsToSave) {
                listTag.add(net.minecraft.nbt.NbtUtils.createUUID(uuid));
            }
            compound.put("SpinningFireballs", listTag);
        }
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (!level().isClientSide()) applyConfigAttributes();

        // Ability state isn't saved, so clear what an interrupted ability left behind
        this.setNoAi(false);
        this.setInvulnerable(false);
        this.setSilent(false);
        this.setInvisible(false);
        this.isUsingDarkDominion = false;
        this.isTransformed = false;
        this.darkDominionRounds = 0;

        this.spinningFireballUUIDs.clear();
        if (compound.contains("SpinningFireballs", 9)) {
            net.minecraft.nbt.ListTag listTag = compound.getList("SpinningFireballs", 11);
            for (int i = 0; i < listTag.size(); i++) {
                this.spinningFireballUUIDs.add(net.minecraft.nbt.NbtUtils.loadUUID(listTag.get(i)));
            }
        }
    }

    private List<Player> nearbyPlayers() {
        return level().getEntitiesOfClass(Player.class, getBoundingBox().inflate(64.0D));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(0, new MeleeAttackGoal(this, 1.2, true));
        this.goalSelector.addGoal(1, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this, 1.2D));
        this.goalSelector.addGoal(4, new LeapAtTargetGoal(this, 0.5F));

        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, Player.class, true));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Villager.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, IronGolem.class, true));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, 10, true, false,
                entity -> !(entity instanceof TamableAnimal t && t.isTame())
                        && (entity.getType().is(PREY)
                        || Config.EXTRA_PREY.get().contains(
                        BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString()))));
    }

    public static AttributeSupplier.Builder setAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 450.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.3D)
                .add(Attributes.ATTACK_DAMAGE, 10.0D)
                .add(Attributes.ATTACK_KNOCKBACK, 0.5D)
                .add(Attributes.ARMOR, 12.0D)
                .add(Attributes.ARMOR_TOUGHNESS, 10.0D)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 64.0D);
    }

    public static final TagKey<EntityType<?>> PREY = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "lilith_prey"));

    public static final TagKey<EntityType<?>> DOMINION_MINIONS = TagKey.create(
            Registries.ENTITY_TYPE,
            ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "dark_dominion_minions"));

    private EntityType<?> pickMinionType() {
        List<EntityType<?>> pool = new ArrayList<>();
        BuiltInRegistries.ENTITY_TYPE.getTag(DOMINION_MINIONS)
                .ifPresent(tag -> tag.forEach(holder -> pool.add(holder.value())));
        return pool.isEmpty() ? EntityType.ZOMBIE : pool.get(random.nextInt(pool.size()));
    }

    @Override
    public void tick() {
        super.tick();

        if (this.level().isClientSide()) {
            return;
        }

        float healthPercent = this.getHealth() / this.getMaxHealth();
        bossEvent.setProgress(healthPercent);

        if (healthPercent <= BossPhase.VOID_EMPRESS.healthThreshold) currentPhase = BossPhase.VOID_EMPRESS;
        else if (healthPercent <= BossPhase.BLOOD_QUEEN.healthThreshold) currentPhase = BossPhase.BLOOD_QUEEN;
        else if (healthPercent <= BossPhase.SHADOW_VEIL.healthThreshold) currentPhase = BossPhase.SHADOW_VEIL;

        if (!isUsingDarkDominion && !isTransformed && tickCount % 20 == 0 && nearbyPlayers().isEmpty()) {
            this.heal(this.getMaxHealth() * 0.02F);  // 2% per second, about 50 seconds to full
        }

        hellfireSpinTick++;
        if (!spinningFireballUUIDs.isEmpty() && spinningFireballs.isEmpty()) {
            for (UUID uuid : spinningFireballUUIDs) {
                Entity found = ((ServerLevel) level()).getEntity(uuid);
                if (found != null) spinningFireballs.add(found);
            }
            spinningFireballUUIDs.clear();
        }
        spinningFireballs.removeIf(f -> f == null || f.isRemoved() || !f.isAlive());

        if (getTarget() != null && hellfireCooldown <= 0 && spinningFireballs.isEmpty()) {
            for (int i = 0; i < 3; i++) {
                HellfireProjectile hellfire = new HellfireProjectile(ModEntities.HELLFIRE_PROJECTILE.get(), level());
                hellfire.setOwner(this);
                hellfire.setPos(getX(), getY() + 2.5, getZ());
                hellfire.setDeltaMovement(Vec3.ZERO);
                level().addFreshEntity(hellfire);
                spinningFireballs.add(hellfire);
            }
            hellfireCooldown = 600;
        }

        for (int i = 0; i < spinningFireballs.size(); i++) {
            Entity fireball = spinningFireballs.get(i);
            if (fireball.getDeltaMovement().lengthSqr() <= 0.01) {
                double angle = (hellfireSpinTick * 0.15) + (i * ((Math.PI * 2) / 3));
                double targetX = getX() + Math.cos(angle) * 3.5;
                double targetY = getY() + 2.2;
                double targetZ = getZ() + Math.sin(angle) * 3.5;
                fireball.setPos(targetX, targetY, targetZ);
            }
        }

        if (getTarget() != null && hellfireSpinTick % 40 == 0 && !spinningFireballs.isEmpty()) {
            spinningFireballs.stream()
                    .filter(f -> f.getDeltaMovement().lengthSqr() <= 0.01)
                    .findFirst()
                    .ifPresent(f -> {
                        Vec3 direction = getTarget().position().add(0, 1, 0).subtract(f.position()).normalize().scale(1.2);
                        f.setDeltaMovement(direction);
                        this.swing(InteractionHand.MAIN_HAND);
                    });
        }

        if (isUsingDarkDominion) {
            abilityPhaseTimer--;
            if (tickCount % 20 == 0 && nearbyPlayers().isEmpty()) {
                noPlayerTicks++;
            } else if (!nearbyPlayers().isEmpty()) {
                noPlayerTicks = 0;
            }
            if (noPlayerTicks >= 10) { // ~10 seconds with nobody near
                for (Monster m : darkDominionMinions) m.discard();
                darkDominionMinions.clear();
                isUsingDarkDominion = false;
                this.setNoAi(false);
                this.setInvulnerable(false);
                darkDominionRounds = 0;
                darkDominionCooldown = Config.DOMINION_COOLDOWN_TICKS.get();
                noPlayerTicks = 0;
                this.setHealth(this.getMaxHealth());
                return;
            }
            darkDominionMinions.removeIf(mob -> mob.isRemoved() || !mob.isAlive());
            double targetY = startingY + MAX_FLOAT_HEIGHT;
            if (this.getY() < targetY) {
                setDeltaMovement(0, 0.1, 0);
            } else {
                setDeltaMovement(0, 0, 0);
                setPos(getX(), targetY, getZ());
            }

            if (abilityPhaseTimer <= 0 && darkDominionMinions.isEmpty()) {
                darkDominionRounds++;
                if (darkDominionRounds >= MAX_DOMINION_ROUNDS) {
                    isUsingDarkDominion = false;
                    this.setNoAi(false);
                    this.setInvulnerable(false);
                    darkDominionRounds = 0;
                    darkDominionCooldown = Config.DOMINION_COOLDOWN_TICKS.get();   // was 2000
                    Player p = level().getNearestPlayer(this, 32);
                    if (p != null) this.setTarget(p);
                } else {
                    spawnDarkDominionWave();
                    abilityPhaseTimer = 100 + random.nextInt(60);
                }
            }
            return;
        }

        if (isTransformed) {
            transformTimer--;
            if (transformTimer <= 0 || activeBats.isEmpty()) revertFromBatForm();
            return;
        }

        if (bloodMoonCooldown > 0) bloodMoonCooldown--;
        if (darkVeilCooldown > 0) darkVeilCooldown--;
        if (shadowDashCooldown > 0) shadowDashCooldown--;
        if (darkDominionCooldown > 0) darkDominionCooldown--;
        if (hellfireCooldown > 0) hellfireCooldown--;
        if (batSwarmCooldown > 0) batSwarmCooldown--;
        if (attackTimer > 0) attackTimer--;
        else isAttacking = false;

        if (this.getTarget() != null && !isAttacking) {
            if (darkDominionCooldown <= 0 && healthPercent < 0.7) {
                applyDarkDominion(this);
                return;
            }

            if (healthPercent < 0.4 && batSwarmCooldown <= 0 && random.nextInt(100) == 0) {
                triggerBatSwarm(this);
                broadcastAbility("Bat Swarm");
                batSwarmCooldown = 1800;
                return;
            }

            if (shadowDashCooldown <= 0 && healthPercent < 0.3 && this.distanceTo(getTarget()) > 5) {
                shadowDash(this);
                broadcastAbility("Shadow Dash");
                return;
            }

            if (bloodMoonCooldown <= 0 && healthPercent < 0.9) {
                applyBloodMoonCurse(this);
                broadcastAbility("Blood Moon Curse");
                bloodMoonCooldown = 1200;
                return;
            }

            if (darkVeilCooldown <= 0 && healthPercent < 0.8) {
                triggerDarkVeil(this);
                broadcastAbility("Dark Veil");
                darkVeilCooldown = 1800;
                return;
            }
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (isTransformed || isUsingDarkDominion) {
            return true;
        }
        if (source.getDirectEntity() != null && spinningFireballs.contains(source.getDirectEntity())) {
            return true;
        }
        if (source.is(DamageTypes.LIGHTNING_BOLT)) {
            return true;
        }
        if (source.is(DamageTypes.IN_FIRE)
                || source.is(DamageTypes.ON_FIRE)
                || source.is(DamageTypes.FIREBALL)
                || source.is(DamageTypes.UNATTRIBUTED_FIREBALL)
                || source.is(DamageTypes.LAVA)
                || source.is(DamageTypes.HOT_FLOOR)) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    private void broadcastAbility(String abilityName) {
        if (!level().isClientSide()) {
            Component msg = Component.empty()
                    .append(Component.literal("[Lilith] ").withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD))
                    .append(Component.literal("uses ").withStyle(ChatFormatting.GRAY))
                    .append(Component.literal(abilityName).withStyle(ChatFormatting.RED, ChatFormatting.BOLD))
                    .append(Component.literal("!").withStyle(ChatFormatting.GRAY));
            for (Player player : nearbyPlayers()) {
                player.sendSystemMessage(msg);
            }
        }
    }

    private Player getNearestPlayer() {
        return level().getNearestPlayer(this, 10.0D);
    }

    @Override
    public void startSeenByPlayer(ServerPlayer player) {
        super.startSeenByPlayer(player);
        bossEvent.addPlayer(player);
    }

    @Override
    public void stopSeenByPlayer(ServerPlayer player) {
        super.stopSeenByPlayer(player);
        bossEvent.removePlayer(player);
    }

    private void applyBloodMoonCurse(LivingEntity entity) {
        if (!level().isClientSide() && bloodMoonCooldown == 0) {
            for (Player player : nearbyPlayers()) {
                player.addEffect(new MobEffectInstance(MobEffects.WITHER, 400, 1));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 300, 1));
            }
            bloodMoonCooldown = 1200;
        }
    }

    private void triggerBatSwarm(LivingEntity entity) {
        if (!level().isClientSide() && !isTransformed) {
            for (int i = 0; i < 5; i++) {
                LilithBat bat = new LilithBat(level(), this);
                bat.setPos(
                        getX() + random.nextDouble() - 0.5,
                        getY() + random.nextDouble() * 0.5,
                        getZ() + random.nextDouble() - 0.5
                );
                level().addFreshEntity(bat);
                activeBats.add(bat);
            }

            isTransformed = true;
            transformTimer = 200;
            this.setInvisible(true);
            this.setNoAi(true);
            this.setSilent(true);
            this.setInvulnerable(true);
            this.setDeltaMovement(0, 0, 0);
            this.setTarget(null);

            playSound(SoundEvents.BAT_TAKEOFF, 1.0F, 1.0F);
            startAttackAnimation(3);
            playAttackSound(3);
        }
    }

    private void revertFromBatForm() {
        for (LilithBat bat : activeBats) {
            if (bat != null && !bat.isRemoved()) {
                bat.discard();
            }
        }
        activeBats.clear();

        isTransformed = false;
        this.setInvisible(false);
        this.setNoAi(false);
        this.setSilent(false);
        this.setInvulnerable(false);

        playSound(SoundEvents.BAT_AMBIENT, 1.0F, 1.0F);
    }

    public void notifyBatDeath(LilithBat bat) {
        activeBats.remove(bat);
        if (isTransformed && activeBats.isEmpty()) {
            revertFromBatForm();
        }
    }

    public static class LilithBat extends Bat {
        private int life = 200;
        private final LilithEntity owner;

        public LilithBat(Level level, LilithEntity owner) {
            super(EntityType.BAT, level);
            this.owner = owner;
        }

        @Override
        public boolean requiresCustomPersistence() {
            return false;
        }

        @Override
        public void tick() {
            super.tick();
            if (!level().isClientSide()) {
                life--;
                if (life <= 0) discard();
            }
        }

        @Override
        protected void tickDeath() {
            this.deathTime++;
            this.setDeltaMovement(0, 0, 0);

            if (!this.level().isClientSide()) {
                ServerLevel serverLevel = (ServerLevel) this.level();

                if (this.deathTime == 1) {
                    this.playSound(SoundEvents.WITHER_DEATH, 1.0F, 0.5F);
                    this.playSound(SoundEvents.GHAST_SCREAM, 1.0F, 0.5F);
                }

                if (this.deathTime % 5 == 0) {
                    serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                            this.getX(), this.getY() + 1.0, this.getZ(),
                            15, 0.5, 1.0, 0.5, 0.1);
                }

                if (this.deathTime == 95) {
                    serverLevel.sendParticles(ParticleTypes.EXPLOSION_EMITTER,
                            this.getX(), this.getY() + 1.0, this.getZ(),
                            1, 0, 0, 0, 0);
                    this.playSound(SoundEvents.GENERIC_EXPLODE.value(), 2.0F, 1.0F);
                }

                if (this.deathTime >= 100) {
                    this.level().broadcastEntityEvent(this, (byte) 60);
                    this.remove(Entity.RemovalReason.KILLED);
                }
            }
        }

        @Override
        public void die(DamageSource source) {
            super.die(source);
            if (!level().isClientSide()) {
                owner.notifyBatDeath(this);
            }
        }
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false;
    }

    private void applyDarkDominion(LivingEntity entity) {
        if (!level().isClientSide() && !isUsingDarkDominion) {
            isUsingDarkDominion = true;
            noPlayerTicks = 0;
            startingY = this.getY();

            this.setNoAi(true);
            this.setInvulnerable(true);
            this.setTarget(null);
            setDeltaMovement(0, 0.2, 0);
            broadcastAbility("Dark Dominion");

            darkDominionMinions.clear();
            spawnDarkDominionWave();

            startAttackAnimation(2);
            playAttackSound(2);
        }
    }

    private void spawnDarkDominionWave() {
        if (level().isClientSide()) return;
        ServerLevel serverLevel = (ServerLevel) level();

        AABB area = new AABB(blockPosition()).inflate(12);
        List<Player> nearbyPlayers = level().getEntitiesOfClass(Player.class, area);

        int count = Config.DOMINION_MINIONS_PER_WAVE.get();
        for (int i = 0; i < count; i++) {
            if (!(pickMinionType().create(level()) instanceof Monster mob)) continue;

            double xOff = random.nextDouble() * 6 - 3;
            double zOff = random.nextDouble() * 6 - 3;

            mob.setPos(getX() + xOff, getY(), getZ() + zOff);
            mob.finalizeSpawn(serverLevel, serverLevel.getCurrentDifficultyAt(mob.blockPosition()), MobSpawnType.COMMAND, null);

            if (!nearbyPlayers.isEmpty()) {
                mob.setTarget(nearbyPlayers.get(random.nextInt(nearbyPlayers.size())));
            }

            level().addFreshEntity(mob);
            darkDominionMinions.add(mob);
        }
    }

    private void shadowDash(LivingEntity entity) {
        if (!level().isClientSide() && shadowDashCooldown == 0) {
            Player target = getNearestPlayer();
            if (target != null) {
                setDeltaMovement(target.getX() - getX(), 0.2, target.getZ() - getZ());
                target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0));
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 1));
                startAttackAnimation(1);
                playAttackSound(1);
            }
            shadowDashCooldown = 600;
        }
    }

    private void triggerDarkVeil(LivingEntity entity) {
        if (!level().isClientSide() && darkVeilCooldown == 0) {
            for (Player player : nearbyPlayers()) {
                player.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 200, 0));
                player.addEffect(new MobEffectInstance(MobEffects.DARKNESS, 300, 0));
            }
            darkVeilCooldown = 1800;
            startAttackAnimation(2);
            playAttackSound(2);
        }
    }

    private void startAttackAnimation(int type) {
        isAttacking = true;
        attackTimer = 20;
        attackType = type;
    }

    private void playAttackSound(int type) {
        SoundEvent sound = switch (type) {
            case 2 -> SoundEvents.WITCH_AMBIENT;
            case 3 -> SoundEvents.BAT_TAKEOFF;
            default -> SoundEvents.SPIDER_AMBIENT;
        };
        playSound(sound, 1.0F, 1.0F);
    }

    @Override
    public void die(DamageSource source) {
        super.die(source);
        playSound(SoundEvents.ELDER_GUARDIAN_DEATH, 1.0F, 1.0F);

        if (!level().isClientSide()) {
            this.spawnAtLocation(new ItemStack(ModItems.LILITH_SOUL.get()));

            float roll = random.nextFloat();
            if (roll < 0.15f) {
                this.spawnAtLocation(new ItemStack(ModItems.CRIMSON_FANG.get()));
            } else if (roll < 0.50f) {
                this.spawnAtLocation(new ItemStack(ModBlocks.LILITH_TROPHY.get()));
            } else {
                this.spawnAtLocation(new ItemStack(ModItems.LILITH_CONTRACT.get()));
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 0, state -> {
            if (isAttacking) {
                return switch (attackType) {
                    case 2 -> state.setAndContinue(ATTACK2_ANIM);
                    case 3 -> state.setAndContinue(ATTACK3_ANIM);
                    default -> state.setAndContinue(ATTACK_ANIM);
                };
            } else if (state.isMoving()) {
                return state.setAndContinue(WALK_ANIM);
            } else {
                return state.setAndContinue(IDLE_ANIM);
            }
        }));
    }

    @Override
    public boolean causeFallDamage(float fallDistance, float multiplier, DamageSource source) {
        return false;
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    protected SoundEvent getAmbientSound() {
        return SoundEvents.ELDER_GUARDIAN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource damageSource) {
        return SoundEvents.ELDER_GUARDIAN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.ELDER_GUARDIAN_DEATH;
    }

    @Override
    protected void playStepSound(BlockPos pos, BlockState state) {
        this.playSound(SoundEvents.ZOMBIE_STEP, 0.15F, 1.0F);
    }

    @Override
    public boolean isNoGravity() {
        if (isUsingDarkDominion) {
            return true;
        }
        return super.isNoGravity();
    }

    @Override
    public LivingEntity getTarget() {
        return isTransformed ? null : super.getTarget();
    }
}