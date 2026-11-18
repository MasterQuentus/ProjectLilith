package com.masterquentus.projectlilith.item.entity.client;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.entity.LilithEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.GeoModel;

public class LilithModel extends GeoModel<LilithEntity> {

    @Override
    public ResourceLocation getModelResource(LilithEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "geo/lilith.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(LilithEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "textures/entity/lilith.png");
    }

    @Override
    public ResourceLocation getAnimationResource(LilithEntity animatable) {
        return ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "animations/lilith.animation.json");
    }
}