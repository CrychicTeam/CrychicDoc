package com.github.alexmodguy.alexscaves.client.gui.book.widget;

import com.github.alexmodguy.alexscaves.client.render.ACRenderTypes;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix3f;
import org.joml.Matrix4f;

public class EntityBoxWidget extends EntityWidget {

    @Expose
    @SerializedName("box_width")
    private float boxWidth;

    @Expose
    @SerializedName("box_height")
    private float boxHeight;

    @Expose
    @SerializedName("box_scale")
    private float boxScale;

    @Expose
    @SerializedName("entity_x_offset")
    private float entityXOffset;

    @Expose
    @SerializedName("entity_y_offset")
    private float entityYOffset;

    @Expose
    @SerializedName("box_image")
    private String borderImage;

    @Expose(serialize = false, deserialize = false)
    private ResourceLocation borderTexture;

    private static final int BORDER_TEXTURE_SIZE = 64;

    private static final int PIX_WIDTH_CORNER = 10;

    private static final int PIX_WIDTH_LINE = 4;

    public EntityBoxWidget(int displayPage, String entityId, boolean sepia, float boxWidth, float boxHeight, float boxScale, float entityXOffset, float entityYOffset, String borderImage, String entityNBT, int x, int y, float scale) {
        super(displayPage, BookWidget.Type.ENTITY_BOX, entityId, sepia, entityNBT, x, y, scale);
        this.boxHeight = boxHeight;
        this.boxWidth = boxWidth;
        this.borderImage = borderImage;
        this.boxScale = boxScale;
        this.entityXOffset = entityXOffset;
        this.entityYOffset = entityYOffset;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        if (this.borderTexture == null) {
            this.borderTexture = new ResourceLocation(this.borderImage);
        }
        poseStack.pushPose();
        poseStack.translate(this.entityXOffset * this.getScale(), this.entityYOffset * this.getScale(), 0.0F);
        super.render(poseStack, bufferSource, partialTicks, onFlippingPage);
        poseStack.popPose();
        VertexConsumer vertexconsumer = bufferSource.getBuffer(ACRenderTypes.getBookWidget(this.borderTexture, this.isSepia()));
        float endUV = 14.0F;
        float endSub = 5.0F;
        float endUV1 = endUV + 10.0F;
        Lighting.setupForFlatItems();
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 2.0F);
        poseStack.translate((float) this.getX() - this.boxWidth * 0.5F * this.boxScale, (float) this.getY() - this.boxHeight * 0.5F * this.boxScale, -15.0F);
        poseStack.scale(this.boxScale, this.boxScale, 1.0F);
        poseStack.pushPose();
        poseStack.pushPose();
        poseStack.translate(0.0F, 0.0F, 2.0F);
        this.renderCorner(poseStack, vertexconsumer, 0.0F, 10.0F, 0.0F, 10.0F);
        poseStack.popPose();
        this.renderQuad(poseStack, vertexconsumer, this.boxWidth - endSub, 5.0F, -5.0F, 5.0F, 10.0F, 14.0F, 0.0F, 10.0F);
        poseStack.pushPose();
        poseStack.translate(this.boxWidth, 0.0F, 2.0F);
        this.renderCorner(poseStack, vertexconsumer, endUV, endUV1, 0.0F, 10.0F);
        poseStack.popPose();
        this.renderQuad(poseStack, vertexconsumer, 5.0F, -5.0F, 4.9F, this.boxHeight - endSub, 0.0F, 10.0F, 10.0F, 14.0F);
        poseStack.pushPose();
        poseStack.translate(0.0F, this.boxHeight, 2.0F);
        this.renderCorner(poseStack, vertexconsumer, 0.0F, 10.0F, endUV, endUV1);
        poseStack.popPose();
        this.renderQuad(poseStack, vertexconsumer, this.boxWidth - endSub, 5.0F, this.boxHeight - 5.0F, this.boxHeight + 5.0F, 10.0F, 14.0F, 14.0F, 24.0F);
        poseStack.pushPose();
        poseStack.translate(this.boxWidth, this.boxHeight, 2.0F);
        this.renderCorner(poseStack, vertexconsumer, endUV, endUV1, endUV, endUV1);
        poseStack.popPose();
        this.renderQuad(poseStack, vertexconsumer, this.boxWidth + 5.0F, this.boxWidth - 5.0F, 4.9F, this.boxHeight - endSub, 14.0F, 24.0F, 10.0F, 14.0F);
        poseStack.popPose();
        poseStack.popPose();
    }

    private void renderCorner(PoseStack poseStack, VertexConsumer vertexconsumer, float u0, float u1, float v0, float v1) {
        float texWidth = (u1 - u0) / 2.0F;
        float texHeight = (v1 - v0) / 2.0F;
        this.renderQuad(poseStack, vertexconsumer, texWidth, -texWidth, -texHeight, texHeight, u0, u1, v0, v1);
    }

    private void renderQuad(PoseStack poseStack, VertexConsumer vertexconsumer, float x0, float x1, float y0, float y1, float u0, float u1, float v0, float v1) {
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        float scaledU0 = u0 / 64.0F;
        float scaledU1 = u1 / 64.0F;
        float scaledV0 = v0 / 64.0F;
        float scaledV1 = v1 / 64.0F;
        vertexconsumer.vertex(matrix4f, x0, y0, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU1, scaledV0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, x1, y0, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU0, scaledV0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(scaledU0, scaledV1).endVertex();
        vertexconsumer.vertex(matrix4f, x1, y1, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU0, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(scaledU0, scaledV0).endVertex();
        vertexconsumer.vertex(matrix4f, x0, y1, 0.0F).color(1.0F, 1.0F, 1.0F, 1.0F).uv(scaledU1, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).uv(scaledU1, scaledV0).endVertex();
    }
}