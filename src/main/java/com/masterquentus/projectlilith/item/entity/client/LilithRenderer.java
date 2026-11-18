package com.masterquentus.projectlilith.item.entity.client;

import com.masterquentus.projectlilith.item.entity.LilithEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class LilithRenderer extends GeoEntityRenderer<LilithEntity> {

    public LilithRenderer(EntityRendererProvider.Context context) {
        super(context, new LilithModel());
    }
}