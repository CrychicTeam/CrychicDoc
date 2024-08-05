package com.github.alexthe666.alexsmobs.client.render;

import com.github.alexthe666.alexsmobs.client.model.ModelSunbird;
import com.github.alexthe666.alexsmobs.entity.EntitySunbird;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RenderSunbird extends MobRenderer<EntitySunbird, ModelSunbird> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexsmobs:textures/entity/sunbird.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexsmobs:textures/entity/sunbird_glow.png");

    public RenderSunbird(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new ModelSunbird(), 0.5F);
        this.m_115326_(new RenderSunbird.LayerScorch(this));
    }

    private static void vertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, int int3, float float4, float float5, int int6, int int7) {
        vertexConsumer0.vertex(matrixF1, float4, float5, 0.0F).color(255, 255, 255, 100).uv((float) int6, (float) int7).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(int3).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    public void render(EntitySunbird entity, float yaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int light) {
        super.render(entity, yaw, partialTicks, poseStack, buffer, light);
        float ageInTicks = (float) entity.f_19797_ + partialTicks;
        float scale = (12.0F + (float) Math.sin((double) (ageInTicks * 0.3F))) * entity.getScorchProgress(partialTicks);
        if (scale > 0.0F) {
            poseStack.pushPose();
            poseStack.translate(0.0F, entity.m_20206_() * 0.5F, 0.0F);
            poseStack.mulPose(this.f_114476_.cameraOrientation());
            poseStack.mulPose(Axis.YP.rotationDegrees(180.0F));
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZP.rotationDegrees(ageInTicks * 8.0F));
            poseStack.translate(-scale * 0.5F, -scale * 0.5F, 0.0F);
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f = posestack$pose.pose();
            Matrix3f matrix3f = posestack$pose.normal();
            VertexConsumer vertexconsumer = buffer.getBuffer(AMRenderTypes.getSunbirdShine());
            vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, 0.0F, 0, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, light, scale, 0.0F, 1, 1);
            vertex(vertexconsumer, matrix4f, matrix3f, light, scale, scale, 1, 0);
            vertex(vertexconsumer, matrix4f, matrix3f, light, 0.0F, scale, 0, 0);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    protected void scale(EntitySunbird entitylivingbaseIn, PoseStack matrixStackIn, float partialTickTime) {
    }

    protected int getBlockLightLevel(EntitySunbird entityIn, BlockPos partialTicks) {
        return 15;
    }

    public ResourceLocation getTextureLocation(EntitySunbird entity) {
        return TEXTURE;
    }

    static class LayerScorch extends RenderLayer<EntitySunbird, ModelSunbird> {

        public LayerScorch(RenderSunbird p_i50928_1_) {
            super(p_i50928_1_);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, EntitySunbird entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer scorch = bufferIn.getBuffer(AMRenderTypes.getEyesAlphaEnabled(RenderSunbird.TEXTURE_GLOW));
            float alpha = entitylivingbaseIn.getScorchProgress(partialTicks);
            ((ModelSunbird) this.m_117386_()).m_7695_(matrixStackIn, scorch, 240, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
        }
    }
}