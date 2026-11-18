package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.block.ModBlocks;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;

import java.util.Set;

public class ModBlockLootTableProvider extends BlockLootSubProvider {
    public ModBlockLootTableProvider(HolderLookup.Provider registries) {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
    }

    @Override
    protected void generate() {
        dropSelf(ModBlocks.LILITH_TROPHY.get());

        // Stone drops Cobblestone unless mined with Silk Touch
        this.add(ModBlocks.CRIMSON_STONE.get(),
                block -> createSingleItemTableWithSilkTouch(block, ModBlocks.CRIMSON_COBBLESTONE.get()));

        dropSelf(ModBlocks.CRIMSON_COBBLESTONE.get());
        dropSelf(ModBlocks.CRIMSON_STONE_BRICKS.get());
        dropSelf(ModBlocks.POLISHED_CRIMSON_STONE.get());
        dropSelf(ModBlocks.CHISELED_CRIMSON_STONE.get());
        dropSelf(ModBlocks.CRACKED_CRIMSON_STONE.get());

        // Stairs
        dropSelf(ModBlocks.CRIMSON_STONE_STAIRS.get());
        dropSelf(ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get());
        dropSelf(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get());
        dropSelf(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get());

        // Slabs (use createSlabItemTable so double slabs drop 2)
        this.add(ModBlocks.CRIMSON_STONE_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.CRIMSON_STONE_SLAB.get()));
        this.add(ModBlocks.CRIMSON_COBBLESTONE_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.CRIMSON_COBBLESTONE_SLAB.get()));
        this.add(ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get()));
        this.add(ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get(),
                block -> createSlabItemTable(ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get()));

        // Wall, Button, Pressure Plate
        dropSelf(ModBlocks.CRIMSON_STONE_WALL.get());
        dropSelf(ModBlocks.CRIMSON_STONE_BUTTON.get());
        dropSelf(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());
    }

    protected LootTable.Builder createMultipleOreDrops(Block block, Item item, float minDrops, float maxDrops) {
        HolderLookup.RegistryLookup<Enchantment> enchantments = this.registries.lookupOrThrow(Registries.ENCHANTMENT);
        return this.createSilkTouchDispatchTable(block, this.applyExplosionDecay(block,
                LootItem.lootTableItem(item)
                        .apply(SetItemCountFunction.setCount(UniformGenerator.between(minDrops, maxDrops)))
                        .apply(ApplyBonusCount.addOreBonusCount(enchantments.getOrThrow(Enchantments.FORTUNE)))));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(Holder::value)::iterator;
    }
}