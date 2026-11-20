package com.masterquentus.projectlilith.ability;

import com.masterquentus.projectlilith.ProjectLilith;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = ProjectLilith.MOD_ID)
public class ModNetwork {

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar("1");
        registrar.playToServer(
                OrbAbilityPayload.TYPE,
                OrbAbilityPayload.STREAM_CODEC,
                PlayerOrbAbility::handlePayload);
    }
}
