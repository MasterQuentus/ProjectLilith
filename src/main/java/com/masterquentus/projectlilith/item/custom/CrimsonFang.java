package com.masterquentus.projectlilith.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class CrimsonFang extends SwordItem {

    private static final String UPGRADED_TAG = "upgradedCrimsonFang";

    public CrimsonFang(Tier tier, int attackDamageModifier, float attackSpeedModifier, Properties properties) {
        super(tier, properties.attributes(SwordItem.createAttributes(tier, attackDamageModifier, attackSpeedModifier)));
    }

    @Override
    public boolean isFoil(ItemStack stack) {
        return isUpgraded(stack);
    }

    @Override
    public boolean hurtEnemy(ItemStack stack, LivingEntity target, LivingEntity attacker) {
        attacker.heal(2.0F);

        if (isUpgraded(stack)) {
            attacker.heal(1.0F);

            if (attacker instanceof Player player && player.getHealth() >= player.getMaxHealth()) {
                player.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 60, 1));

                if (player.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ParticleTypes.SOUL,
                            target.getX(),
                            target.getY() + 1,
                            target.getZ(),
                            5,
                            0.2,
                            0.2,
                            0.2,
                            0.05
                    );
                }
            }
        }

        return super.hurtEnemy(stack, target, attacker);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        if (!isUpgraded(stack)) {
            return InteractionResultHolder.pass(stack);
        }

        if (!level.isClientSide()) {
            Vec3 look = player.getLookAngle();
            player.setDeltaMovement(look.x * 1.5, 0.4, look.z * 1.5);
            player.hurtMarked = true;

            level.playSound(
                    null,
                    player.getX(), player.getY(), player.getZ(),
                    SoundEvents.ENDERMAN_TELEPORT,
                    SoundSource.PLAYERS,
                    0.5F, 1.2F
            );

            player.getCooldowns().addCooldown(this, 20);
        }

        return InteractionResultHolder.success(stack);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltipComponents, TooltipFlag tooltipFlag) {
        if (isUpgraded(stack)) {
            tooltipComponents.add(
                    Component.literal("Contract Signed")
                            .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC)
            );
            tooltipComponents.add(
                    Component.literal("Ability: Shadow Dash [Right-Click]")
                            .withStyle(ChatFormatting.GOLD)
            );
            tooltipComponents.add(
                    Component.literal("Passive: Lifesteal (Heals on Hit)")
                            .withStyle(ChatFormatting.RED)
            );
            tooltipComponents.add(
                    Component.literal("Passive: Vampiric Wrath")
                            .withStyle(ChatFormatting.RED)
            );
        } else {
            tooltipComponents.add(
                    Component.literal("Upgrade at a Smithing Table: Contract (template) + Soul (addition)")
                            .withStyle(ChatFormatting.GRAY)
            );
        }

        super.appendHoverText(stack, context, tooltipComponents, tooltipFlag);
    }

    private boolean isUpgraded(ItemStack stack) {
        CustomData data = stack.get(DataComponents.CUSTOM_DATA);
        if (data == null) {
            return false;
        }
        return data.copyTag().getBoolean(UPGRADED_TAG);
    }

    public static void applyUpgrade(ItemStack stack) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(UPGRADED_TAG, true);
        stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag));
    }
}