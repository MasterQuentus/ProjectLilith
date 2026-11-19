package com.masterquentus.projectlilith.item.armor;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.ModItems;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;

@EventBusSubscriber(modid = ProjectLilith.MOD_ID)
public class ArmorSetBonuses {

    private static boolean wearing(LivingEntity entity, EquipmentSlot slot, Item item) {
        return entity.getItemBySlot(slot).is(item);
    }

    public static boolean hasHelmet(LivingEntity e) {
        return wearing(e, EquipmentSlot.HEAD, ModItems.ABYSSAL_HELMET.get());
    }

    public static boolean hasChestplate(LivingEntity e) {
        return wearing(e, EquipmentSlot.CHEST, ModItems.ABYSSAL_CHESTPLATE.get());
    }

    public static boolean hasLegsAndBoots(LivingEntity e) {
        return wearing(e, EquipmentSlot.LEGS, ModItems.ABYSSAL_LEGGINGS.get())
                && wearing(e, EquipmentSlot.FEET, ModItems.ABYSSAL_BOOTS.get());
    }

    public static boolean hasFullSet(LivingEntity e) {
        return hasHelmet(e) && hasChestplate(e) && hasLegsAndBoots(e);
    }

    // Helmet: immune to Darkness
    @SubscribeEvent
    public static void onEffectApplicable(MobEffectEvent.Applicable event) {
        if (event.getEntity() instanceof Player player
                && hasHelmet(player)
                && event.getEffectInstance().getEffect() == MobEffects.DARKNESS) {
            event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);
        }
    }

    // Chestplate: heal 8% of melee damage dealt
    @SubscribeEvent
    public static void onDamageDealt(LivingDamageEvent.Post event) {
        DamageSource source = event.getSource();
        if (source.getEntity() instanceof Player player
                && source.getDirectEntity() == player
                && hasChestplate(player)) {
            player.heal(event.getNewDamage() * 0.08F);
        }
    }

    // Leggings + boots: Hellfire immunity.
    // Add this as the first line of HellfireBlock.entityInside():
    //   if (entity instanceof LivingEntity le && ArmorSetBonuses.hasLegsAndBoots(le)) return;
}