package com.masterquentus.projectlilith.ability;

import com.masterquentus.projectlilith.ProjectLilith;
import com.mojang.serialization.Codec;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.function.Supplier;

/** Saved data stored on the player. Survives death and relogging. */
public class ModAttachments {
    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.ATTACHMENT_TYPES, ProjectLilith.MOD_ID);

    /** True once the player has signed a Lilith Contract and learned the Hellfire Orbs. */
    public static final Supplier<AttachmentType<Boolean>> HELLFIRE_ORBS_LEARNED = ATTACHMENT_TYPES.register(
            "hellfire_orbs_learned",
            () -> AttachmentType.builder(() -> false)
                    .serialize(Codec.BOOL)
                    .copyOnDeath()
                    .build());

    public static void register(IEventBus eventBus) {
        ATTACHMENT_TYPES.register(eventBus);
    }
}
