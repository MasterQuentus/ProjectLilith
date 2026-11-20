package com.masterquentus.projectlilith.item.custom;

import com.masterquentus.projectlilith.ability.ModAttachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class LilithContractItem extends Item {

    public LilithContractItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        tooltip.add(Component.literal("Right-click to sign and learn the Hellfire Orbs")
                .withStyle(ChatFormatting.DARK_RED, ChatFormatting.ITALIC));
        tooltip.add(Component.literal("Set the keys under Controls > Project Lilith")
                .withStyle(ChatFormatting.GRAY));
        super.appendHoverText(stack, context, tooltip, flag);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);

        // Already signed: do nothing, so extra contracts stay usable in recipes
        if (player.getData(ModAttachments.HELLFIRE_ORBS_LEARNED)) {
            if (!level.isClientSide()) {
                player.displayClientMessage(Component.literal("You have already signed Lilith's contract.")
                        .withStyle(ChatFormatting.GRAY), true);
            }
            return InteractionResultHolder.fail(stack);
        }

        if (!level.isClientSide()) {
            player.setData(ModAttachments.HELLFIRE_ORBS_LEARNED, true);
            stack.consume(1, player);

            level.playSound(null, player.blockPosition(), SoundEvents.ENCHANTMENT_TABLE_USE, SoundSource.PLAYERS, 1.0F, 0.6F);
            if (level instanceof ServerLevel serverLevel) {
                serverLevel.sendParticles(ParticleTypes.SOUL_FIRE_FLAME,
                        player.getX(), player.getY() + 1.0, player.getZ(),
                        40, 0.5, 0.9, 0.5, 0.05);
            }
            player.displayClientMessage(Component.literal("You signed Lilith's contract. Hellfire Orbs learned!")
                    .withStyle(ChatFormatting.DARK_RED, ChatFormatting.BOLD), true);
        }
        return InteractionResultHolder.sidedSuccess(stack, level.isClientSide());
    }
}