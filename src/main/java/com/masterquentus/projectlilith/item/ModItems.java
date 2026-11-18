package com.masterquentus.projectlilith.item;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.entity.ModEntities;
import com.masterquentus.projectlilith.item.custom.CrimsonFang;
import com.masterquentus.projectlilith.item.custom.FlintAndHellfireItem;
import com.masterquentus.projectlilith.item.custom.HellfireChargeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(ProjectLilith.MOD_ID);

    public static final DeferredItem<Item> LILITH_CONTRACT = ITEMS.registerSimpleItem("lilith_contract");

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

    public static final DeferredItem<Item> LILITH_SPAWN_EGG = ITEMS.register("lilith_spawn_egg",
            () -> new DeferredSpawnEggItem(ModEntities.LILITH, 0x590000, 0x2D0B0B, new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}