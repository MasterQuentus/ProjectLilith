package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.ItemLike;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    /** Builds an id in OUR namespace. save(output, "string") would default to the minecraft namespace. */
    private static ResourceLocation id(String path) {
        return ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, path);
    }

    /** Stonecutter recipe saved under the mod namespace. */
    private static void stonecut(RecipeOutput output, ItemLike result, ItemLike base, int count) {
        SingleItemRecipeBuilder.stonecutting(Ingredient.of(base), RecipeCategory.BUILDING_BLOCKS, result, count)
                .unlockedBy(getHasName(base), has(base))
                .save(output, id(getItemName(result) + "_from_" + getItemName(base) + "_stonecutting"));
    }

    private static void smelt(RecipeOutput output, ItemLike input, ItemLike result, String name) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.BUILDING_BLOCKS, result, 0.1F, 200)
                .unlockedBy(getHasName(input), has(input))
                .save(output, id(name));
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // ------------------------------------------------------------------
        // Ritual items
        // ------------------------------------------------------------------
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HELLFIRE_CHARGE.get())
                .requires(Items.FIRE_CHARGE)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLINT_AND_HELLFIRE.get())
                .pattern("H")
                .pattern("F")
                .define('H', ModItems.HELLFIRE_CHARGE.get())
                .define('F', Items.FLINT)
                .unlockedBy(getHasName(ModItems.HELLFIRE_CHARGE.get()), has(ModItems.HELLFIRE_CHARGE.get()))
                .save(output);

        // ------------------------------------------------------------------
        // How players GET Crimson Stone
        // 4 Netherrack + 1 Blaze Powder -> 4 Crimson Cobblestone, then smelt for Crimson Stone
        // ------------------------------------------------------------------
        ShapelessRecipeBuilder.shapeless(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_COBBLESTONE.get(), 4)
                .requires(Items.NETHERRACK, 4)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Items.NETHERRACK), has(Items.NETHERRACK))
                .save(output, id("crimson_cobblestone_from_netherrack"));

        smelt(output, ModBlocks.CRIMSON_COBBLESTONE.get(), ModBlocks.CRIMSON_STONE.get(), "crimson_stone_from_smelting");

        // ------------------------------------------------------------------
        // Crimson Stone variants (vanilla-style)
        // ------------------------------------------------------------------
        // Bricks: 4 Crimson Stone -> 4 Bricks
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        // Polished: 4 Crimson Stone -> 4 Polished (was using bricks before, now matches vanilla)
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_CRIMSON_STONE.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        // Cracked: smelt Bricks
        smelt(output, ModBlocks.CRIMSON_STONE_BRICKS.get(), ModBlocks.CRACKED_CRIMSON_STONE.get(), "cracked_crimson_stone_from_smelting");

        // Chiseled: 2 Crimson Stone Slabs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_CRIMSON_STONE.get())
                .pattern("S")
                .pattern("S")
                .define('S', ModBlocks.CRIMSON_STONE_SLAB.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE_SLAB.get()), has(ModBlocks.CRIMSON_STONE_SLAB.get()))
                .save(output);

        // Button and Pressure Plate
        ShapelessRecipeBuilder.shapeless(RecipeCategory.REDSTONE, ModBlocks.CRIMSON_STONE_BUTTON.get())
                .requires(ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get())
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        // ------------------------------------------------------------------
        // Stairs, slabs, wall (crafting table)
        // ------------------------------------------------------------------
        stairBuilder(ModBlocks.CRIMSON_STONE_STAIRS.get(), Ingredient.of(ModBlocks.CRIMSON_STONE.get()))
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get())).save(output);
        slab(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_STONE_SLAB.get(), ModBlocks.CRIMSON_STONE.get());

        stairBuilder(ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get(), Ingredient.of(ModBlocks.CRIMSON_COBBLESTONE.get()))
                .unlockedBy(getHasName(ModBlocks.CRIMSON_COBBLESTONE.get()), has(ModBlocks.CRIMSON_COBBLESTONE.get())).save(output);
        slab(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_COBBLESTONE_SLAB.get(), ModBlocks.CRIMSON_COBBLESTONE.get());
        wall(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_STONE_WALL.get(), ModBlocks.CRIMSON_COBBLESTONE.get());

        stairBuilder(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get(), Ingredient.of(ModBlocks.CRIMSON_STONE_BRICKS.get()))
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE_BRICKS.get()), has(ModBlocks.CRIMSON_STONE_BRICKS.get())).save(output);
        slab(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get(), ModBlocks.CRIMSON_STONE_BRICKS.get());

        stairBuilder(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get(), Ingredient.of(ModBlocks.POLISHED_CRIMSON_STONE.get()))
                .unlockedBy(getHasName(ModBlocks.POLISHED_CRIMSON_STONE.get()), has(ModBlocks.POLISHED_CRIMSON_STONE.get())).save(output);
        slab(output, RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get(), ModBlocks.POLISHED_CRIMSON_STONE.get());

        // ------------------------------------------------------------------
        // Stonecutter
        // ------------------------------------------------------------------
        // From Crimson Stone
        stonecut(output, ModBlocks.CRIMSON_STONE_BRICKS.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.POLISHED_CRIMSON_STONE.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.CHISELED_CRIMSON_STONE.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_STONE_STAIRS.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_STONE_SLAB.get(), ModBlocks.CRIMSON_STONE.get(), 2);
        stonecut(output, ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get(), ModBlocks.CRIMSON_STONE.get(), 2);
        stonecut(output, ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get(), ModBlocks.CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get(), ModBlocks.CRIMSON_STONE.get(), 2);

        // From Crimson Cobblestone
        stonecut(output, ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get(), ModBlocks.CRIMSON_COBBLESTONE.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_COBBLESTONE_SLAB.get(), ModBlocks.CRIMSON_COBBLESTONE.get(), 2);
        stonecut(output, ModBlocks.CRIMSON_STONE_WALL.get(), ModBlocks.CRIMSON_COBBLESTONE.get(), 1);

        // From Crimson Stone Bricks
        stonecut(output, ModBlocks.CHISELED_CRIMSON_STONE.get(), ModBlocks.CRIMSON_STONE_BRICKS.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get(), ModBlocks.CRIMSON_STONE_BRICKS.get(), 1);
        stonecut(output, ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get(), ModBlocks.CRIMSON_STONE_BRICKS.get(), 2);

        // From Polished Crimson Stone
        stonecut(output, ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get(), ModBlocks.POLISHED_CRIMSON_STONE.get(), 1);
        stonecut(output, ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get(), ModBlocks.POLISHED_CRIMSON_STONE.get(), 2);
    }
}