package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.client.model.NucleeperModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.NucleeperEntity;
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
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeRenderTypes;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class NucleeperRenderer extends MobRenderer<NucleeperEntity, NucleeperModel> {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper.png");

    private static final ResourceLocation TEXTURE_GLOW = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glow.png");

    private static final ResourceLocation TEXTURE_GLASS = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_glass.png");

    private static final ResourceLocation TEXTURE_BUTTONS_0 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_0.png");

    private static final ResourceLocation TEXTURE_BUTTONS_1 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_1.png");

    private static final ResourceLocation TEXTURE_BUTTONS_2 = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_buttons_2.png");

    private static final ResourceLocation TEXTURE_EXPLODE = new ResourceLocation("alexscaves:textures/entity/nucleeper/nucleeper_explode.png");

    public NucleeperRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new NucleeperModel(), 0.8F);
        this.m_115326_(new NucleeperRenderer.LayerGlow());
    }

    protected void scale(NucleeperEntity mob, PoseStack poseStack, float partialTicks) {
    }

    public ResourceLocation getTextureLocation(NucleeperEntity entity) {
        return TEXTURE;
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(0, 255, 0, 230).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 255, 0, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, ACMath.HALF_SQRT_3 * float4, float3, 0.0F).color(0, 255, 0, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    public void render(NucleeperEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        float closeProgress = entityIn.getCloseProgress(partialTicks);
        if (closeProgress > 0.0F && entityIn.m_6084_() && !entityIn.isExploding()) {
            float sq = Mth.sqrt(closeProgress);
            float length = sq * 3.0F;
            float width = (1.0F - sq) * 1.25F;
            Vec3 modelOffset = ((NucleeperModel) this.f_115290_).getSirenPosition(new Vec3(0.0, -0.5, 0.0));
            poseStack.pushPose();
            poseStack.translate(modelOffset.x, modelOffset.y, modelOffset.z);
            poseStack.mulPose(Axis.YP.rotationDegrees(entityIn.getSirenAngle(partialTicks)));
            poseStack.pushPose();
            poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            Matrix3f matrix3f1 = posestack$pose.normal();
            VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getNucleeperLights());
            shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            Matrix4f matrix4f2 = posestack$pose.pose();
            Matrix3f matrix3f2 = posestack$pose.normal();
            poseStack.mulPose(Axis.ZN.rotationDegrees(180.0F));
            shineOriginVertex(lightConsumer, matrix4f2, matrix3f2, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
            shineRightCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f2, matrix3f2, length, width, 0.0F, 0.0F);
            poseStack.popPose();
            poseStack.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    class LayerGlow extends RenderLayer<NucleeperEntity, NucleeperModel> {

        public LayerGlow() {
            super(NucleeperRenderer.this);
        }

        public void render(PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn, NucleeperEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            float alpha = (float) (1.0 + Math.sin((double) (ageInTicks * 0.3F))) * 0.25F + 0.5F;
            float explodeProgress = entitylivingbaseIn.getExplodeProgress(partialTicks);
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(NucleeperRenderer.TEXTURE_GLOW));
            ((NucleeperModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, alpha);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(ForgeRenderTypes.getUnlitTranslucent(NucleeperRenderer.TEXTURE_GLASS));
            ((NucleeperModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            int buttonDiv = entitylivingbaseIn.f_19797_ / 5 % 6;
            ResourceLocation buttons;
            if (buttonDiv < 2) {
                buttons = NucleeperRenderer.TEXTURE_BUTTONS_0;
            } else if (buttonDiv < 4) {
                buttons = NucleeperRenderer.TEXTURE_BUTTONS_1;
            } else {
                buttons = NucleeperRenderer.TEXTURE_BUTTONS_2;
            }
            VertexConsumer ivertexbuilder3 = bufferIn.getBuffer(RenderType.eyes(buttons));
            ((NucleeperModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder3, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            VertexConsumer ivertexbuilder4 = bufferIn.getBuffer(ACRenderTypes.getEyesAlphaEnabled(NucleeperRenderer.TEXTURE_EXPLODE));
            ((NucleeperModel) this.m_117386_()).m_7695_(poseStack, ivertexbuilder4, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, explodeProgress);
        }
    }
}