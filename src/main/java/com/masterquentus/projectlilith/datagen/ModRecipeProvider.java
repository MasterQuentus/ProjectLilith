package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.RecipeCategory;
import net.minecraft.data.recipes.RecipeOutput;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.data.recipes.ShapedRecipeBuilder;
import net.minecraft.data.recipes.ShapelessRecipeBuilder;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider {
    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    protected void buildRecipes(RecipeOutput output) {
        // 1. Shapeless recipe to craft the Hellfire Charge from vanilla items
        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModItems.HELLFIRE_CHARGE.get())
                .requires(Items.FIRE_CHARGE)
                .requires(Items.GUNPOWDER)
                .requires(Items.BLAZE_POWDER)
                .unlockedBy(getHasName(Items.FIRE_CHARGE), has(Items.FIRE_CHARGE))
                .save(output);

        // 2. Shaped recipe to craft the Flint and Hellfire summoning item
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.FLINT_AND_HELLFIRE.get())
                .pattern("H")
                .pattern("F")
                .define('H', ModItems.HELLFIRE_CHARGE.get())
                .define('F', Items.FLINT)
                .unlockedBy(getHasName(ModItems.HELLFIRE_CHARGE.get()), has(ModItems.HELLFIRE_CHARGE))
                .save(output);

        // 3. Crimson Stone Building Blocks Recipes
        // Stone Bricks from Crimson Stone
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CRIMSON_STONE_BRICKS.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        // Polished Crimson Stone from Crimson Stone
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.POLISHED_CRIMSON_STONE.get(), 4)
                .pattern("SS")
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE_BRICKS.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE_BRICKS.get()), has(ModBlocks.CRIMSON_STONE_BRICKS.get()))
                .save(output, "polished_crimson_stone_from_bricks");

        // Chiseled Crimson Stone from Slabs
        ShapedRecipeBuilder.shaped(RecipeCategory.BUILDING_BLOCKS, ModBlocks.CHISELED_CRIMSON_STONE.get(), 1)
                .pattern("S")
                .pattern("S")
                .define('S', ModBlocks.CRIMSON_STONE_SLAB.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE_SLAB.get()), has(ModBlocks.CRIMSON_STONE_SLAB.get()))
                .save(output);

        // Correct way for Buttons and Pressure Plates (using ShapedRecipeBuilder)
        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CRIMSON_STONE_BUTTON.get())
                .pattern("S")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        ShapedRecipeBuilder.shaped(RecipeCategory.REDSTONE, ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get())
                .pattern("SS")
                .define('S', ModBlocks.CRIMSON_STONE.get())
                .unlockedBy(getHasName(ModBlocks.CRIMSON_STONE.get()), has(ModBlocks.CRIMSON_STONE.get()))
                .save(output);

        // Stairs, Slabs, Walls, Buttons, and Pressure Plates
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

    }
}