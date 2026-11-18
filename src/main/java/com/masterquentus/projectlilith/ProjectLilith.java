package com.masterquentus.projectlilith;

import com.masterquentus.projectlilith.block.ModBlocks;
import com.masterquentus.projectlilith.creativemodtab.ModCreativeModTabs;
import com.masterquentus.projectlilith.entity.ModEntities;
import com.masterquentus.projectlilith.item.ModItems;
import org.slf4j.Logger;

import com.mojang.logging.LogUtils;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;

@Mod(ProjectLilith.MOD_ID)
public class ProjectLilith {
    public static final String MOD_ID = "projectlilith";
    public static final Logger LOGGER = LogUtils.getLogger();

    public ProjectLilith(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);

        // Register Deferred Registers
        ModCreativeModTabs.register(modEventBus);

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);

        // Register ourselves for server and other game events we are interested in
        NeoForge.EVENT_BUS.register(this);

        modEventBus.addListener(this::addCreative);

        // Register our mod's ModConfigSpec
        modContainer.registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // Common setup logic here
    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        // Custom creative tab item insertions here
    }

    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Server starting logic here
    }
}