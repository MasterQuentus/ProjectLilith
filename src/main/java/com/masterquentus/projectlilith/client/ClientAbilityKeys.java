package com.masterquentus.projectlilith.client;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.ability.OrbAbilityPayload;
import net.minecraft.client.Minecraft;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;

/** Client only. Turns key presses into packets for the server. */
@EventBusSubscriber(modid = ProjectLilith.MOD_ID, value = Dist.CLIENT)
public class ClientAbilityKeys {

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        Minecraft mc = Minecraft.getInstance();
        // Ignore presses while a menu or chat is open (still drain the queue below)
        boolean canSend = mc.player != null && mc.screen == null;

        while (ModKeyMappings.ORB_TOGGLE.consumeClick()) {
            if (canSend) PacketDistributor.sendToServer(new OrbAbilityPayload(OrbAbilityPayload.TOGGLE));
        }
        while (ModKeyMappings.ORB_FIRE.consumeClick()) {
            if (canSend) PacketDistributor.sendToServer(new OrbAbilityPayload(OrbAbilityPayload.FIRE));
        }
    }
}
