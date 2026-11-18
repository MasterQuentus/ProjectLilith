package com.masterquentus.projectlilith.item.entity.client;

import com.masterquentus.projectlilith.ProjectLilith;
import com.masterquentus.projectlilith.item.ModItems;
import com.masterquentus.projectlilith.item.entity.HellfireProjectile;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class HellfireProjectileRenderer<T extends HellfireProjectile> extends EntityRenderer<T> {
    private static final ResourceLocation FIRE_TEXTURE = ResourceLocation.fromNamespaceAndPath(ProjectLilith.MOD_ID, "textures/entity/projectiles/hellfire.png");

    public HellfireProjectileRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(T entity) {
        return FIRE_TEXTURE;
    }

    @Override
    public void render(T entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        poseStack.pushPose();
        poseStack.mulPose(this.entityRenderDispatcher.cameraOrientation());
        poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));

        // LAYER 1: the Hellfire Charge item
        ItemStack stack = entity.getItem();
        if (stack.isEmpty()) {
            stack = new ItemStack(ModItems.HELLFIRE_CHARGE.get());
        }
        poseStack.pushPose();
        Minecraft.getInstance().getItemRenderer().renderStatic(
                stack, ItemDisplayContext.GROUND, packedLight, OverlayTexture.NO_OVERLAY,
                poseStack, buffer, entity.level(), entity.getId());
        poseStack.popPose();

        // LAYER 2: the animated fire
        poseStack.pushPose();
        poseStack.translate(0.0D, 0.2D, -0.05D);
        poseStack.scale(1.8F, 1.2F, 1.2F);

        float frameHeight = 1.0F / 16.0F;
        int frame = (int) (((entity.tickCount + partialTicks) / 2.0F) % 16);
        float v0 = frame * frameHeight;
        float v1 = v0 + frameHeight;

        VertexConsumer fireBuffer = buffer.getBuffer(RenderType.entityTranslucentEmissive(FIRE_TEXTURE));
        PoseStack.Pose pose = poseStack.last();
        fireVertex(fireBuffer, pose, -0.4F, -0.2F, 0.0F, v1);
        fireVertex(fireBuffer, pose,  0.4F, -0.2F, 1.0F, v1);
        fireVertex(fireBuffer, pose,  0.4F,  0.6F, 1.0F, v0);
        fireVertex(fireBuffer, pose, -0.4F,  0.6F, 0.0F, v0);

        poseStack.popPose();
        poseStack.popPose();

        super.render(entity, entityYaw, partialTicks, poseStack, buffer, packedLight);
    }

    private static void fireVertex(VertexConsumer consumer, PoseStack.Pose pose, float x, float y, float u, float v) {
        consumer.addVertex(pose, x, y, 0.0F)
                .setColor(255, 255, 255, 255)
                .setUv(u, v)
                .setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(LightTexture.FULL_BRIGHT)
                .setNormal(pose, 0.0F, 1.0F, 0.0F);
    }
}