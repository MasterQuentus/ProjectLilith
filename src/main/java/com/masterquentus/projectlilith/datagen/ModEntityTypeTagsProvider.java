package com.masterquentus.projectlilith.datagen;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.entity.LilithEntity;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import javax.annotation.Nullable;
import java.util.concurrent.CompletableFuture;

public class ModEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ModEntityTypeTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider,
                                     @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, ProjectLilith.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        tag(LilithEntity.DOMINION_MINIONS).add(
                EntityType.SKELETON, EntityType.WITHER_SKELETON, EntityType.ZOMBIE,
                EntityType.HUSK, EntityType.DROWNED, EntityType.EVOKER,
                EntityType.STRAY, EntityType.BOGGED, EntityType.PILLAGER,
                EntityType.BREEZE, EntityType.VINDICATOR, EntityType.WITCH);

        tag(LilithEntity.PREY).add(
                EntityType.COW, EntityType.MOOSHROOM, EntityType.PIG, EntityType.SHEEP,
                EntityType.CHICKEN, EntityType.RABBIT, EntityType.GOAT);
    }
}