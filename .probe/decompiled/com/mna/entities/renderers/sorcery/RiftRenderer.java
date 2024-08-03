package com.mna.entities.renderers.sorcery;

import com.mna.entities.sorcery.Rift;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

public class RiftRenderer extends EntityRenderer<Rift> {

    private static final ResourceLocation PORTAL_TEXTURE = new ResourceLocation("mna", "textures/entity/vortex.png");

    private static final RenderType RENDER_TYPE = RenderType.entityTranslucent(PORTAL_TEXTURE);

    public RiftRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    public void render(Rift entityIn, float entityYaw, float partialTicks, PoseStack matrixStackIn, MultiBufferSource bufferIn, int packedLightIn) {
        Quaternionf cameraRotation = this.f_114476_.cameraOrientation();
        Quaternionf portalRotation = new Quaternionf(0.0F, cameraRotation.y(), 0.0F, cameraRotation.w());
        float spawnPct = entityIn.getAge() < 20 ? (float) Math.min(entityIn.getAge(), 20) / 20.0F : (entityIn.getAge() > 180 ? 1.0F - (float) Math.min(entityIn.getAge() - 180, 20) / 20.0F : 1.0F);
        float scaleFactor = 1.0F * spawnPct;
        float portalSpinDegrees = (float) (entityIn.getAge() * 3 % 360);
        float verticalOffset = 1.0F;
        VertexConsumer vertexBuilder = bufferIn.getBuffer(RENDER_TYPE);
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
        int[] color = entityIn.getIsEnderChest() ? new int[] { 3, 38, 32, 230 } : new int[] { 105, 24, 152, 230 };
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, color);
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
        color = entityIn.getIsEnderChest() ? new int[] { 52, 153, 136, 230 } : new int[] { 186, 30, 180, 230 };
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 0.0F, 0.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 0.0F, 1.0F, 1.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 1.0F, 1.0F, 1.0F, 0.0F, nrmH, nrmV, color);
        addVertex(vertexBuilder, renderMatrix, normalMatrix, packedLightIn, 0.0F, 1.0F, 0.0F, 0.0F, nrmH, nrmV, color);
        matrixStackIn.popPose();
        super.render(entityIn, entityYaw, partialTicks, matrixStackIn, bufferIn, packedLightIn);
    }

    private static void addVertex(VertexConsumer vertexBuilder_, Matrix4f renderMatrix, Matrix3f normalMatrix, int packedLight, float x, float y, float u, float v, float nrmH, float nrmV, int[] color) {
        if (color.length == 4) {
            vertexBuilder_.vertex(renderMatrix, x - 0.5F, y - 0.25F, 0.0F).color(color[0], color[1], color[2], color[3]).uv(u, v).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(packedLight).normal(normalMatrix, nrmH, nrmV, nrmH).endVertex();
        }
    }

    public ResourceLocation getTextureLocation(Rift entity) {
        return PORTAL_TEXTURE;
    }
}