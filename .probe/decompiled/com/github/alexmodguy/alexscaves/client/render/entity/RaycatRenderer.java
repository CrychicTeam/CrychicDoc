package com.github.alexmodguy.alexscaves.client.render.entity;

import com.github.alexmodguy.alexscaves.AlexsCaves;
import com.github.alexmodguy.alexscaves.client.ClientProxy;
import com.github.alexmodguy.alexscaves.client.model.RaycatModel;
import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.github.alexmodguy.alexscaves.server.entity.living.RaycatEntity;
import com.github.alexthe666.citadel.client.shader.PostEffectRegistry;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.client.renderer.entity.layers.RenderLayer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class RaycatRenderer extends MobRenderer<RaycatEntity, RaycatModel> implements CustomBookEntityRenderer {

    private static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves:textures/entity/raycat.png");

    private static final ResourceLocation TEXTURE_BODY = new ResourceLocation("alexscaves:textures/entity/raycat_body.png");

    private static final ResourceLocation TEXTURE_EYES = new ResourceLocation("alexscaves:textures/entity/raycat_eyes.png");

    private static final float HALF_SQRT_3 = (float) (Math.sqrt(3.0) / 2.0);

    private boolean sepia;

    public RaycatRenderer(EntityRendererProvider.Context renderManagerIn) {
        super(renderManagerIn, new RaycatModel(), 0.4F);
        this.m_115326_(new RaycatRenderer.LayerGlow());
    }

    @Nullable
    protected RenderType getRenderType(RaycatEntity raycatEntity, boolean normal, boolean translucent, boolean outline) {
        ResourceLocation resourcelocation = this.getTextureLocation(raycatEntity);
        if (translucent) {
            return RenderType.itemEntityTranslucentCull(resourcelocation);
        } else if (normal) {
            return this.sepia ? null : (AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get() ? ACRenderTypes.getRadiationGlow(resourcelocation) : ACRenderTypes.m_110473_(resourcelocation));
        } else {
            return outline ? RenderType.outline(resourcelocation) : null;
        }
    }

    private static void shineOriginVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, 0.0F, 0.0F, 0.0F).color(0, 255, 0, 255).uv(xOffset + 0.5F, yOffset).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, 1.0F, 0.0F).endVertex();
    }

    private static void shineLeftCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, -HALF_SQRT_3 * float4, float3, 0.0F).color(0, 255, 0, 0).uv(xOffset, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    private static void shineRightCornerVertex(VertexConsumer vertexConsumer0, Matrix4f matrixF1, Matrix3f matrixF2, float float3, float float4, float xOffset, float yOffset) {
        vertexConsumer0.vertex(matrixF1, HALF_SQRT_3 * float4, float3, 0.0F).color(0, 255, 0, 0).uv(xOffset + 1.0F, yOffset + 1.0F).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrixF2, 0.0F, -1.0F, 0.0F).endVertex();
    }

    public void render(RaycatEntity entityIn, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource bufferIn, int packedLightIn) {
        if (!this.sepia && AlexsCaves.CLIENT_CONFIG.radiationGlowEffect.get()) {
            PostEffectRegistry.renderEffectForNextTick(ClientProxy.IRRADIATED_SHADER);
        }
        float absorbAmount = entityIn.getAbsorbAmount(partialTicks);
        Entity absorbTarget = entityIn.getAbsorbTarget();
        if (absorbAmount > 0.0F && entityIn.m_6084_() && absorbTarget != null) {
            Vec3 to = absorbTarget.getPosition(partialTicks).add(0.0, (double) (absorbTarget.getBbHeight() * 0.5F), 0.0);
            Vec3 toTranslate = to.subtract(entityIn.m_20318_(partialTicks).add(0.0, (double) (entityIn.m_20206_() * 0.5F), 0.0));
            float yRot = (float) Mth.atan2(toTranslate.x, toTranslate.z) * 180.0F / (float) Math.PI;
            float xRot = -((float) (Mth.atan2(toTranslate.y, toTranslate.horizontalDistance()) * 180.0F / (float) Math.PI));
            float length = (float) toTranslate.length() * absorbAmount;
            float width = absorbAmount * 0.8F;
            poseStack.pushPose();
            poseStack.translate(0.0F, entityIn.m_20206_() * 0.5F, 0.0F);
            poseStack.mulPose(Axis.XP.rotationDegrees(xRot));
            poseStack.mulPose(Axis.YP.rotationDegrees(yRot - 90.0F));
            poseStack.pushPose();
            poseStack.mulPose(Axis.XN.rotationDegrees(90.0F));
            poseStack.mulPose(Axis.ZN.rotationDegrees(90.0F));
            PoseStack.Pose posestack$pose = poseStack.last();
            Matrix4f matrix4f1 = posestack$pose.pose();
            Matrix3f matrix3f1 = posestack$pose.normal();
            VertexConsumer lightConsumer = bufferIn.getBuffer(ACRenderTypes.getNucleeperLights());
            shineOriginVertex(lightConsumer, matrix4f1, matrix3f1, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineRightCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            shineLeftCornerVertex(lightConsumer, matrix4f1, matrix3f1, length, width, 0.0F, 0.0F);
            poseStack.popPose();
            poseStack.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, poseStack, bufferIn, packedLightIn);
    }

    public ResourceLocation getTextureLocation(RaycatEntity entity) {
        return TEXTURE_BODY;
    }

    @Override
    public void setSepiaFlag(boolean sepiaFlag) {
        this.sepia = sepiaFlag;
    }

    class LayerGlow extends RenderLayer<RaycatEntity, RaycatModel> {

        public LayerGlow() {
            super(RaycatRenderer.this);
        }

        public void render(PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn, RaycatEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float partialTicks, float ageInTicks, float netHeadYaw, float headPitch) {
            VertexConsumer ivertexbuilder1 = bufferIn.getBuffer(RaycatRenderer.this.sepia ? ACRenderTypes.getBookWidget(RaycatRenderer.TEXTURE, true) : RenderType.entityCutoutNoCull(RaycatRenderer.TEXTURE));
            ((RaycatModel) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder1, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
            VertexConsumer ivertexbuilder2 = bufferIn.getBuffer(RaycatRenderer.this.sepia ? ACRenderTypes.getBookWidget(RaycatRenderer.TEXTURE_EYES, true) : ACRenderTypes.getEyesAlphaEnabled(RaycatRenderer.TEXTURE_EYES));
            ((RaycatModel) this.m_117386_()).renderToBuffer(matrixStackIn, ivertexbuilder2, packedLightIn, LivingEntityRenderer.getOverlayCoords(entitylivingbaseIn, 0.0F), 1.0F, 1.0F, 1.0F, 1.0F);
        }
    }
}