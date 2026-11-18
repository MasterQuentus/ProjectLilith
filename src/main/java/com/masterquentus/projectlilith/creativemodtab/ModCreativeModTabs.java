package com.masterquentus.projectlilith.creativemodtab;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.item.ModItems;
import com.masterquentus.projectlilith.item.custom.CrimsonFang;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ProjectLilith.MOD_ID);

    public static final Supplier<CreativeModeTab> PROJECT_LILITH_ITEMS_TAB = CREATIVE_MODE_TABS.register("project_lilith_items_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.LILITH_CONTRACT.get()))
                    .title(Component.translatable("creativetab.projectlilith_items"))
                    .withTabsBefore(CreativeModeTabs.INGREDIENTS)
                    .displayItems((itemDisplayParameters, output) -> {
                        // Items
                        output.accept(ModItems.LILITH_CONTRACT.get());
                        output.accept(ModItems.LILITH_SOUL.get());
                        output.accept(ModItems.FLINT_AND_HELLFIRE.get());
                        output.accept(ModItems.HELLFIRE_CHARGE.get());
                        output.accept(ModItems.CRIMSON_FANG.get());

                        // The upgraded Crimson Fang
                        ItemStack upgraded = new ItemStack(ModItems.CRIMSON_FANG.get());
                        CrimsonFang.applyUpgrade(upgraded);
                        output.accept(upgraded);

                        // Spawn Egg
                        output.accept(ModItems.LILITH_SPAWN_EGG.get());

                        // Trophy & Base Stone Blocks
                        output.accept(ModBlocks.LILITH_TROPHY.get());
                        output.accept(ModBlocks.CRIMSON_STONE.get());
                        output.accept(ModBlocks.CRIMSON_COBBLESTONE.get());
                        output.accept(ModBlocks.CRIMSON_STONE_BRICKS.get());
                        output.accept(ModBlocks.POLISHED_CRIMSON_STONE.get());
                        output.accept(ModBlocks.CHISELED_CRIMSON_STONE.get());
                        output.accept(ModBlocks.CRACKED_CRIMSON_STONE.get());

                        // Stairs
                        output.accept(ModBlocks.CRIMSON_STONE_STAIRS.get());
                        output.accept(ModBlocks.CRIMSON_COBBLESTONE_STAIRS.get());
                        output.accept(ModBlocks.CRIMSON_STONE_BRICKS_STAIRS.get());
                        output.accept(ModBlocks.POLISHED_CRIMSON_STONE_STAIRS.get());

                        // Slabs
                        output.accept(ModBlocks.CRIMSON_STONE_SLAB.get());
                        output.accept(ModBlocks.CRIMSON_COBBLESTONE_SLAB.get());
                        output.accept(ModBlocks.CRIMSON_STONE_BRICKS_SLAB.get());
                        output.accept(ModBlocks.POLISHED_CRIMSON_STONE_SLAB.get());

                        // Walls, Buttons, and Pressure Plates
                        output.accept(ModBlocks.CRIMSON_STONE_WALL.get());
                        output.accept(ModBlocks.CRIMSON_STONE_BUTTON.get());
                        output.accept(ModBlocks.CRIMSON_STONE_PRESSURE_PLATE.get());
                    }).build());

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TABS.register(eventBus);
    }
}