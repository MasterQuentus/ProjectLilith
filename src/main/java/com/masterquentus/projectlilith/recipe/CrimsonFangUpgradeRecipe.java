package com.masterquentus.projectlilith.recipe;

import com.masterquentus.projectlilith.item.ModItems;
import com.masterquentus.projectlilith.item.custom.CrimsonFang;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.NonNullList;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.Level;

public class CrimsonFangUpgradeRecipe extends CustomRecipe {

    public CrimsonFangUpgradeRecipe(CraftingBookCategory category) {
        super(category);
    }

    @Override
    public boolean matches(CraftingInput input, Level level) {
        boolean hasSword = false;
        boolean hasSoul = false;
        boolean hasContract = false;
        int totalItems = 0;

        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (!stack.isEmpty()) {
                totalItems++;
                if (stack.getItem() instanceof CrimsonFang) {
                    hasSword = true;
                } else if (stack.is(ModItems.LILITH_SOUL.get())) {
                    hasSoul = true;
                } else if (stack.is(ModItems.LILITH_CONTRACT.get())) {
                    hasContract = true;
                } else {
                    return false; // Found an unauthorized item
                }
            }
        }

        return hasSword && hasSoul && hasContract && totalItems == 3;
    }

    @Override
    public ItemStack assemble(CraftingInput input, HolderLookup.Provider registries) {
        for (int i = 0; i < input.size(); i++) {
            ItemStack stack = input.getItem(i);
            if (stack.getItem() instanceof CrimsonFang) {
                ItemStack result = stack.copy();
                CrimsonFang.applyUpgrade(result);
                return result;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return width * height >= 3;
    }

    @Override
    public RecipeSerializer<? extends CustomRecipe> getSerializer() {
        return ModRecipes.CRIMSON_FANG_UPGRADE_SERIALIZER.get();
    }
}