package com.masterquentus.projectlilith.item.custom;

import net.minecraft.ChatFormatting;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.*;

import java.util.List;

public class AbyssalArmorItem extends ArmorItem {

    public AbyssalArmorItem(Holder<ArmorMaterial> material, ArmorItem.Type type, Item.Properties properties) {
        super(material, type, properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, List<Component> tooltip, TooltipFlag flag) {
        // Only list what ArmorSetBonuses actually does, and keep the text in sync with it.
        switch (this.getType()) {
            case HELMET -> tooltip.add(Component.literal("Immune to Darkness")
                    .withStyle(ChatFormatting.DARK_RED));
            case CHESTPLATE -> tooltip.add(Component.literal("Heals you for 8% of melee damage dealt")
                    .withStyle(ChatFormatting.DARK_RED));
            case LEGGINGS, BOOTS -> {
                tooltip.add(Component.literal("Immune to fire and Hellfire")
                        .withStyle(ChatFormatting.DARK_RED));
                tooltip.add(Component.literal("Requires both Leggings and Boots")
                        .withStyle(ChatFormatting.GRAY, ChatFormatting.ITALIC));
            }
            default -> { }
        }
        super.appendHoverText(stack, context, tooltip, flag);
    }
}
