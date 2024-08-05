package com.mna.entities.renderers.boss.effects;

import com.mna.entities.boss.effects.DemonReturnPortal;
import com.mna.tools.render.MARenderTypes;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class DemonReturnPortalRenderer extends EntityRenderer<DemonReturnPortal> {

    private static final int[] color = new int[] { 217, 72, 0 };

    public DemonReturnPortalRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(DemonReturnPortal entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        float spawnPct = (float) Math.min(entityIn.getAge(), 20) / 20.0F;
        if (entityIn.getAge() > 160) {
            spawnPct = (float) Math.max(180 - entityIn.getAge(), 0) / 20.0F;
        }
        float scaleFactor = 8.0F * spawnPct;
        float portalSpinDegrees = (float) (entityIn.getAge() * 3 % 360);
        VertexConsumer vertexBuilder = bufferIn.getBuffer(MARenderTypes.PORTAL_RENDER);
        matrixStackIn.pushPose();
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(portalSpinDegrees));
        matrixStackIn.translate(0.0F, -0.25F, 0.0F);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = (float) Math.cos((double) portalSpinDegrees * Math.PI / 180.0);
        float nrmH = (float) Math.cos((double) (portalSpinDegrees - 90.0F) * Math.PI / 180.0);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, color);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(Axis.XP.rotationDegrees(90.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-portalSpinDegrees));
        matrixStackIn.translate(0.0F, -0.25F, 0.001F);
        matrixstack$entry = matrixStackIn.last();
        renderMatrix = matrixstack$entry.pose();
        normalMatrix = matrixstack$entry.normal();
        nrmH = (float) Math.cos((double) (portalSpinDegrees + 90.0F) * Math.PI / 180.0);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, color);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] rgb) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(rgb[0], rgb[1], rgb[2], 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }

    public ResourceLocation getTextureLocation(DemonReturnPortal entity) {
        return MARenderTypes.PORTAL_TEXTURE;
    }
}