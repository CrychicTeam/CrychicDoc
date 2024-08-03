package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.MineGuardianModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.MineGuardianEntity;
import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class MineGuardianRenderer extends MobRenderer<MineGuardianEntity, MineGuardianModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/mine_guardian.png");

    private static final ResourceLocation TEXTURE_SLEEPING = new ResourceLocation("alexscaves:textures/entity/mine_guardian_sleeping.png");

    private static final ResourceLocation TEXTURE_EYE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_eye.png");

    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/mine_guardian_explode.png");

    public MineGuardianRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new MineGuardianModel(), 0.8F);
        this.m_115326_(new MineGuardianRenderer.LayerGlow());
    }

    protected void scale(MineGuardianEntity mob, PoseStack poseStack, float partialTicks) {
        poseStack.scale(1.5F, 1.5F, 1.5F);
    }

    public ResourceLocation getTextureLocation(MineGuardianEntity entity) {
        return entity.isEyeClosed() ? TEXTURE_SLEEPING : TEXTURE;
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(230, 0, 0, 230).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(255, 0, 0, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(255, 0, 0, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    public void render(MineGuardianEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
        float bodyYaw = Mth.rotLerp(partialTicks, entityIn.f_20884_, entityIn.f_20883_);
        float scanProgress = entityIn.getScanProgress(partialTicks);
        if (scanProgress > 0.0F && entityIn.m_6084_() && !entityIn.isExploding()) {
            float ticks = (float) entityIn.f_19797_ + partialTicks;
            float length = (float) ((double) scanProgress * (4.0 + Math.sin((double) (ticks * 0.2F + 2.0F))));
            float width = scanProgress * scanProgress * 1.0F;
            float extraX = (float) ((double) scanProgress * Math.sin((double) (ticks * 0.1F)) * 3.0);
            poseStack.pushPose();
            poseStack.translate(0.0F, 0.5F, 0.0F);
            poseStack.mulPose(Axis.YN.rotationDegrees(bodyYaw));
            poseStack.translate(extraX * 0.5F / 16.0F, 0.25F, 0.75F);
            poseStack.mulPose(Axis.YP.rotationDegrees((float) (Math.sin((double) (ticks * 0.1F)) * 32.0 * (double) scanProgress)));
            ((MineGuardianModel) this.f_115290_).translateToEye(poseStack);
            poseStack.pushPose();
            poseStack.mulPose(Axis.XP.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.YP.rotationDegrees(90.0F));
            poseStack.translate(0.0F, -0.5F, 0.0F);
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            Matrix3f matrix3f1 = posestack$pose.normal();
            VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getSubmarineLights());
            shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            poseStack.popPose();
            poseStack.popPose();
        }
    }

    class LayerGlow extends RenderLayer<MineGuardianEntity, MineGuardianModel> {

        public LayerGlow() {
            super(MineGuardianRenderer.this);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, MineGuardianEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
            if (!entitylivingbaseIn.isEyeClosed()) {
                VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RenderType.eyes(MineGuardianRenderer.TEXTURE_EYE));
                ((MineGuardianModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            }
            VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(MineGuardianRenderer.TEXTURE_EXPLODE));
            ((MineGuardianModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, explodeProgress);
        }
    }
}