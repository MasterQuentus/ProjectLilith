package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModItemTagsProvider extends ItemTagsProvider {
    public ModItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                               CompletableFuture<TagLookup<Block>> blockTags,
                               @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, ProjectLilith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        copy(BlockTags.STAIRS, ItemTags.STAIRS);
        copy(BlockTags.SLABS, ItemTags.SLABS);
        copy(BlockTags.WALLS, ItemTags.WALLS);
        copy(BlockTags.BUTTONS, ItemTags.BUTTONS);
        copy(BlockTags.STONE_BUTTONS, ItemTags.STONE_BUTTONS);


        tag(ItemTags.TRIMMABLE_ARMOR)
                .add(ModItems.ABYSSAL_HELMET.get())
                .add(ModItems.ABYSSAL_CHESTPLATE.get())
                .add(ModItems.ABYSSAL_LEGGINGS.get())
                .add(ModItems.ABYSSAL_BOOTS.get());

        tag(ItemTags.HEAD_ARMOR).add(ModItems.ABYSSAL_HELMET.get());
        tag(ItemTags.CHEST_ARMOR).add(ModItems.ABYSSAL_CHESTPLATE.get());
        tag(ItemTags.LEG_ARMOR).add(ModItems.ABYSSAL_LEGGINGS.get());
        tag(ItemTags.FOOT_ARMOR).add(ModItems.ABYSSAL_BOOTS.get());

    }
}