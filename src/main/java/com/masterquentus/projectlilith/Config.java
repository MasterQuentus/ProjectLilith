package com.masterquentus.projectlilith;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.List;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.DoubleValue LILITH_MAX_HEALTH = BUILDER
            .comment("Lilith's maximum health")
            .defineInRange("lilithMaxHealth", 450.0, 20.0, 100000.0);

    public static final ModConfigSpec.DoubleValue LILITH_ATTACK_DAMAGE = BUILDER
            .comment("Lilith's melee damage")
            .defineInRange("lilithAttackDamage", 10.0, 0.0, 1000.0);

    public static final ModConfigSpec.IntValue DOMINION_MINIONS_PER_WAVE = BUILDER
            .comment("How many minions each Dark Dominion wave spawns")
            .defineInRange("dominionMinionsPerWave", 11, 1, 50);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> EXTRA_PREY = BUILDER
            .comment("Extra animals Lilith will hunt, as entity IDs, e.g. [\"minecraft:horse\"]. "
                    + "These are added on top of the projectlilith:lilith_prey tag.")
            .defineListAllowEmpty("extraPrey", List.of(), () -> "", Config::isEntityId);

    public static final ModConfigSpec.ConfigValue<List<? extends String>> HELLFIRE_DIMENSIONS = BUILDER
            .comment("Dimensions where Hellfire can exist, as dimension IDs. Leave empty to allow it everywhere.")
            .defineListAllowEmpty("hellfireDimensions", List.of(), () -> "", Config::isEntityId);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean isEntityId(final Object obj) {
        return obj instanceof String id && ResourceLocation.tryParse(id) != null;
    }
}