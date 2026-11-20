package com.masterquentus.projectlilith.client;

import com.masterquentus.projectlilith.ProjectLilith;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.lwjgl.glfw.GLFW;

/** Client only. Players can rebind these under Options > Controls > Project Lilith. */
@EventBusSubscriber(modid = ProjectLilith.MOD_ID, value = Dist.CLIENT)
public class ModKeyMappings {

    public static final String CATEGORY = "key.categories.projectlilith";

    public static final KeyMapping ORB_TOGGLE =
            new KeyMapping("key.projectlilith.orb_toggle", GLFW.GLFW_KEY_R, CATEGORY);

    public static final KeyMapping ORB_FIRE =
            new KeyMapping("key.projectlilith.orb_fire", GLFW.GLFW_KEY_G, CATEGORY);

    @SubscribeEvent
    public static void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(ORB_TOGGLE);
        event.register(ORB_FIRE);
    }
}
