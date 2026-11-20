package com.masterquentus.projectlilith.item.armor;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.Util;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.crafting.Ingredient;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.EnumMap;
import java.util.List;

public class ModArmorMaterials {
    public static final DeferredRegister<ArmorMaterial> ARMOR_MATERIALS =
            DeferredRegister.create(Registries.ARMOR_MATERIAL, ProjectLilith.MOD_ID);

    // Netherite-level stats. The set bonuses are the upgrade.
    public static final Holder<ArmorMaterial> ABYSSAL = ARMOR_MATERIALS.register("abyssal",
            () -> new ArmorMaterial(
                    Util.make(new EnumMap<>(ArmorItem.Type.class), map -> {
                        map.put(ArmorItem.Type.BOOTS, 3);
                        map.put(ArmorItem.Type.LEGGINGS, 6);
                        map.put(ArmorItem.Type.CHESTPLATE, 8);
                        map.put(ArmorItem.Type.HELMET, 3);
                        map.put(ArmorItem.Type.BODY, 11);
                    }),
                    15,                                    // enchantability
                    SoundEvents.ARMOR_EQUIP_NETHERITE,
                    () -> Ingredient.of(ModItems.LILITH_SOUL.get()),   // anvil repair item
                    List.of(new ArmorMaterial.Layer(
                            ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "abyssal"))),
                    3.0F,                                  // toughness
                    0.1F));                                // knockback resistance

    public static void register(IEventBus eventBus) {
        ARMOR_MATERIALS.register(eventBus);
    }
}
