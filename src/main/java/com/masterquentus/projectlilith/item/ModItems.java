package com.masterquentus.projectlilith.item;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.entity.ModEntities;
import com.masterquentus.projectlilith.item.armor.ModArmorMaterials;
import com.masterquentus.projectlilith.item.custom.*;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectLilith.MOD_ID);

    public static final DeferredItem<Item> LILITH_CONTRACT = ITEMS.registerItem("lilith_contract", LilithContractItem::new);

    public static final DeferredItem<Item> LILITH_SOUL = ITEMS.registerSimpleItem("lilith_soul");

    public static final DeferredItem<FlintAndHellfireItem> FLINT_AND_HELLFIRE =
            ITEMS.registerItem("flint_and_hellfire",
                    props -> new FlintAndHellfireItem(props.durability(64)));

    public static final DeferredItem<HellfireChargeItem> HELLFIRE_CHARGE =
            ITEMS.registerItem("hellfire_charge",
                    HellfireChargeItem::new);

    public static final DeferredItem<CrimsonFang> CRIMSON_FANG =
            ITEMS.registerItem("crimson_fang",
                    props -> new CrimsonFang(Tiers.NETHERITE, 3, -2.4F, props));

    public static final DeferredItem<ArmorItem> ABYSSAL_HELMET = ITEMS.registerItem("abyssal_helmet",
            props -> new AbyssalArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.HELMET,
                    props.fireResistant().durability(ArmorItem.Type.HELMET.getDurability(37))));

    public static final DeferredItem<ArmorItem> ABYSSAL_CHESTPLATE = ITEMS.registerItem("abyssal_chestplate",
            props -> new AbyssalArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.CHESTPLATE,
                    props.fireResistant().durability(ArmorItem.Type.CHESTPLATE.getDurability(37))));

    public static final DeferredItem<ArmorItem> ABYSSAL_LEGGINGS = ITEMS.registerItem("abyssal_leggings",
            props -> new AbyssalArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.LEGGINGS,
                    props.fireResistant().durability(ArmorItem.Type.LEGGINGS.getDurability(37))));

    public static final DeferredItem<ArmorItem> ABYSSAL_BOOTS = ITEMS.registerItem("abyssal_boots",
            props -> new AbyssalArmorItem(ModArmorMaterials.ABYSSAL, ArmorItem.Type.BOOTS,
                    props.fireResistant().durability(ArmorItem.Type.BOOTS.getDurability(37))));

    public static final DeferredItem<Item> LILITH_SPAWN_EGG = ITEMS.register("lilith_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.LILITH, 0x590000, 0x2D0B0B, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}