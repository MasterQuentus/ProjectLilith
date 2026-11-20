package com.masterquentus.projectlilith.ability;

import com.masterquentus.projectlilith.ProjectLilith;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

/** Client -> server: "the player pressed an orb key". action is TOGGLE or FIRE. */
public record OrbAbilityPayload(int action) implements CustomPacketPayload {

    public static final int TOGGLE = 0;   // summon / dismiss the orbs
    public static final int FIRE = 1;     // launch one orb

    public static final CustomPacketPayload.Type<OrbAbilityPayload> TYPE = new CustomPacketPayload.Type<>(
            ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "orb_ability"));

    public static final StreamCodec<RegistryFriendlyByteBuf, OrbAbilityPayload> STREAM_CODEC =
            StreamCodec.composite(ByteBufCodecs.VAR_INT, OrbAbilityPayload::action, OrbAbilityPayload::new);

    @Override
    public CustomPacketPayload.Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
