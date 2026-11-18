package com.masterquentus.projectlilith;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.datagen.*;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@EventBusSubscriber(modid = ProjectLilith.MOD_ID)
public class ProjectLilithDataGen {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();

        // 1. Models & Block States (Using your inner classes)
        generator.addProvider(event.includeClient(), new ModModelProvider.BlockStates(packOutput, existingFileHelper));
        generator.addProvider(event.includeClient(), new ModModelProvider.ItemModels(packOutput, existingFileHelper));

        // 2. Block Loot Tables
        generator.addProvider(event.includeServer(), new LootTableProvider(packOutput, Collections.emptySet(),
                List.of(new LootTableProvider.SubProviderEntry(ModBlockLootTableProvider::new, LootContextParamSets.BLOCK)), lookupProvider));

        // 3. Recipes
        generator.addProvider(event.includeServer(), new ModRecipeProvider(packOutput, lookupProvider));

        // 4. Tags
        ModBlockTagsProvider blockTags = new ModBlockTagsProvider(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blockTags);

        generator.addProvider(event.includeServer(),
                new ModItemTagsProvider(packOutput, lookupProvider, blockTags.contentsGetter(), existingFileHelper));

        generator.addProvider(event.includeServer(),
                new ModEntityTypeTagsProvider(packOutput, lookupProvider, existingFileHelper));

        // 5. Advancements (3 arguments only in 1.21.1)
        generator.addProvider(event.includeServer(), new net.minecraft.data.advancements.AdvancementProvider(
                packOutput,
                lookupProvider,
                List.of(new ModAdvancementProvider())
        ));
    }
}