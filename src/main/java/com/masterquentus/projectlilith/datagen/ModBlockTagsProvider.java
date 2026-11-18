package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModBlockTagsProvider extends BlockTagsProvider {
    public ModBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ProjectLilith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(BlockTags.MINEABLE_WITH_PICKAXE)
                .add(ModBlocks.LILITH_TROPHY.get())
                .add(ModBlocks.CRIMSON_STONE.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE.get())
                .add(ModBlocks.CRIMSON_STONE_BRICKS.get())
                .add(ModBlocks.POLISHED_CRIMSON_STONE.get())
                .add(ModBlocks.CHISELED_CRIMSON_STONE.get())
                .add(ModBlocks.CRACKED_CRIMSON_STONE.get())
                .add(ModBlocks.CRIMSON_STONE_STAIRS.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get())
                .add(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get())
                .add(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get())
                .add(ModBlocks.CRIMSON_STONE_SLAB.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE_SLAB.get())
                .add(ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get())
                .add(ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get())
                .add(ModBlocks.CRIMSON_STONE_WALL.get())
                .add(ModBlocks.CRIMSON_STONE_BUTTON.get())
                .add(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());

        tag(BlockTags.WALLS)
                .add(ModBlocks.CRIMSON_STONE_WALL.get());

        tag(BlockTags.SLABS)
                .add(ModBlocks.CRIMSON_STONE_SLAB.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE_SLAB.get())
                .add(ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get())
                .add(ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get());

        tag(BlockTags.STAIRS)
                .add(ModBlocks.CRIMSON_STONE_STAIRS.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get())
                .add(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get())
                .add(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get());

        tag(BlockTags.FIRE)
                .add(ModBlocks.HELLFIRE.get());

        tag(BlockTags.BUTTONS).add(ModBlocks.CRIMSON_STONE_BUTTON.get());
        tag(BlockTags.STONE_BUTTONS).add(ModBlocks.CRIMSON_STONE_BUTTON.get());
        tag(BlockTags.PRESSURE_PLATES).add(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());
        tag(BlockTags.STONE_PRESSURE_PLATES).add(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());
        tag(Tags.Blocks.COBBLESTONES).add(ModBlocks.CRIMSON_COBBLESTONE.get());
        tag(Tags.Blocks.STONES).add(ModBlocks.CRIMSON_STONE.get());

        // Stone categorization & Redstone
        tag(BlockTags.STONE_BRICKS).add(ModBlocks.CRIMSON_STONE_BRICKS.get());
        tag(BlockTags.STONE_BUTTONS).add(ModBlocks.CRIMSON_STONE_BUTTON.get());
        tag(BlockTags.BUTTONS).add(ModBlocks.CRIMSON_STONE_BUTTON.get());
        tag(BlockTags.PRESSURE_PLATES).add(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());

// Ore replacement (allows ores to embed in your custom stone)
        tag(BlockTags.STONE_ORE_REPLACEABLES)
                .add(ModBlocks.CRIMSON_STONE.get())
                .add(ModBlocks.CRIMSON_COBBLESTONE.get());
    }
}