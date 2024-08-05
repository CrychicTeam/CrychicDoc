package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.BurrowingArrowModel;
import com.github.alexmodguy.alexscaves.server.entity.item.BurrowingArrowEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public class BurrowingArrowRenderer extends EntityRenderer<BurrowingArrowEntity> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/burrowing_arrow.png");

    private static final BurrowingArrowModel MODEL = new BurrowingArrowModel();

    public BurrowingArrowRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(BurrowingArrowEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferSource, int lighting) {
        poseStack.pushPose();
        poseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(partialTicks, entity.f_19859_, entity.m_146908_())));
        poseStack.mulPose(Axis.XN.rotationDegrees(Mth.lerp(partialTicks, entity.f_19860_, entity.m_146909_())));
        poseStack.translate(0.0, 1.5, -0.35);
        poseStack.mulPose(Axis.XP.rotationDegrees(180.0F));
        float f9 = (float) entity.f_36706_ - partialTicks;
        if (f9 > 0.0F) {
            float f10 = -Mth.sin(f9 * 3.0F) * f9;
            poseStack.mulPose(Axis.ZP.rotationDegrees(f10));
        }
        VertexConsumer vertexconsumer = bufferSource.getBuffer(RenderType.entityCutoutNoCull(this.getTextureLocation(entity)));
        MODEL.setupAnim(entity, 0.0F, 0.0F, (float) entity.f_19797_ + partialTicks, 0.0F, 0.0F);
        MODEL.m_7695_(poseStack, vertexconsumer, lighting, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        poseStack.popPose();
        super.render(entity, entityYaw, partialTicks, poseStack, bufferSource, lighting);
    }

    public ResourceLocation getTextureLocation(BurrowingArrowEntity entity) {
        return TEXTURE;
    }
}