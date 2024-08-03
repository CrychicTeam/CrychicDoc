package com.mna.entities.renderers.faction;

import com.mna.entities.faction.util.FactionRaid;
import com.mna.factions.Factions;
import com.mna.tools.render.MARenderTypes;
import com.mna.tools.render.WorldRenderUtils;
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
import org.joml.Quaternionf;

public class FactionRaidRenderer extends EntityRenderer<FactionRaid> {

    private static final int[] white = new int[] { 255, 255, 255 };

    private static final int[] green = new int[] { 0, 204, 0 };

    private static final int[] blue = new int[] { 102, 0, 255 };

    public FactionRaidRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(FactionRaid entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        if (entityIn.getFaction() == Factions.COUNCIL) {
            this.RenderPortal(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
        } else if (entityIn.getFaction() == Factions.FEY) {
            matrixStackIn.pushPose();
            matrixStackIn.translate(0.0F, 1.0F, 0.0F);
            WorldRenderUtils.renderRadiant(entityIn, matrixStackIn, bufferIn, green, blue, 128, 0.5F);
            matrixStackIn.popPose();
        }
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private void RenderPortal(FactionRaid entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        float spawnPct = (float) Math.min(entityIn.f_19797_, 20) / 20.0F;
        float scaleFactor = 4.0F * spawnPct;
        float portalSpinDegrees = (float) (entityIn.f_19797_ * 3 % 360);
        float verticalOffset = 1.0F;
        int[] colors = white;
        VertexConsumer vertexBuilder = bufferIn.getBuffer(MARenderTypes.RAID_PORTAL_RENDER);
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, verticalOffset, 0.0F);
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(portalRotation);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(portalSpinDegrees));
        matrixStackIn.translate(0.0F, -0.25F, 0.0F);
        PoseStack.Pose matrixstack$entry = matrixStackIn.last();
        Matrix4f renderMatrix = matrixstack$entry.pose();
        Matrix3f normalMatrix = matrixstack$entry.normal();
        float nrmV = (float) Math.cos((double) portalSpinDegrees * Math.PI / 180.0);
        float nrmH = (float) Math.cos((double) (portalSpinDegrees - 90.0F) * Math.PI / 180.0);
        int frame = entityIn.f_19797_ % 25;
        float minU = 0.0F;
        float maxU = 1.0F;
        float minV = 0.04F * (float) frame;
        float maxV = minV + 0.04F;
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, minU, maxV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, maxU, maxV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, maxU, minV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, minU, minV, nrmH, nrmV, colors);
        matrixStackIn.popPose();
        matrixStackIn.pushPose();
        matrixStackIn.translate(0.0F, verticalOffset, 0.0F);
        matrixStackIn.scale(scaleFactor, scaleFactor, scaleFactor);
        matrixStackIn.mulPose(portalRotation);
        matrixStackIn.mulPose(Axis.YP.rotationDegrees(180.0F));
        matrixStackIn.mulPose(Axis.ZP.rotationDegrees(-portalSpinDegrees));
        matrixStackIn.translate(0.0F, -0.25F, 0.001F);
        matrixstack$entry = matrixStackIn.last();
        renderMatrix = matrixstack$entry.pose();
        normalMatrix = matrixstack$entry.normal();
        nrmH = (float) Math.cos((double) (portalSpinDegrees + 90.0F) * Math.PI / 180.0);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, minU, maxV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, maxU, maxV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, maxU, minV, nrmH, nrmV, colors);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, minU, minV, nrmH, nrmV, colors);
        matrixStackIn.popPose();
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] rgb) {
        vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(rgb[0], rgb[1], rgb[2], 230).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
    }

    public ResourceLocation getTextureLocation(FactionRaid entity) {
        return MARenderTypes.PORTAL_TEXTURE_RAID;
    }
}