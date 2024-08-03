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

public class ImageWidget extends BookWidget {

    @Expose
    @SerializedName("image")
    private String image;

    @Expose
    private boolean sepia;

    @Expose
    private int u0;

    @Expose
    private int v0;

    @Expose
    private int u1;

    @Expose
    private int v1;

    @Expose
    private int width;

    @Expose
    private int height;

    @Expose(serialize = false, deserialize = false)
    private ResourceLocation actualTexture;

    public ImageWidget(int displayPage, String image, boolean sepia, int u0, int v0, int u1, int v1, int width, int height, int x, int y, float scale) {
        super(displayPage, BookWidget.Type.IMAGE, x, y, scale);
        this.image = image;
        this.sepia = sepia;
        this.u0 = u0;
        this.v0 = v0;
        this.u1 = u1;
        this.v1 = v1;
        this.width = width;
        this.height = height;
    }

    @Override
    public void render(PoseStack poseStack, MultiBufferSource.BufferSource bufferSource, float partialTicks, boolean onFlippingPage) {
        if (this.actualTexture == null) {
            this.actualTexture = new ResourceLocation(this.image);
        }
        VertexConsumer vertexconsumer = bufferSource.getBuffer(ACRenderTypes.getBookWidget(this.actualTexture, this.sepia));
        float scale = this.getScale();
        float scaledU0 = (float) this.u0 / (float) this.width;
        float scaledU1 = (float) this.u1 / (float) this.width;
        float scaledV0 = (float) this.v0 / (float) this.height;
        float scaledV1 = (float) this.v1 / (float) this.height;
        float texWidth = (float) (this.u1 - this.u0) / 2.0F;
        float texHeight = (float) (this.v1 - this.v0) / 2.0F;
        float alpha = 1.0F;
        Lighting.setupForFlatItems();
        poseStack.pushPose();
        poseStack.translate((float) this.getX(), (float) this.getY(), -15.0F);
        poseStack.scale(scale, scale, scale);
        PoseStack.Pose posestack$pose = poseStack.last();
        Matrix4f matrix4f = posestack$pose.pose();
        Matrix3f matrix3f = posestack$pose.normal();
        vertexconsumer.vertex(matrix4f, texWidth, -texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(scaledU1, scaledV0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, -texWidth, -texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(scaledU0, scaledV0).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, -texWidth, texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(scaledU0, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        vertexconsumer.vertex(matrix4f, texWidth, texHeight, 0.0F).color(1.0F, 1.0F, 1.0F, alpha).uv(scaledU1, scaledV1).overlayCoords(OverlayTexture.NO_OVERLAY).uv2(240).normal(matrix3f, 0.0F, 1.0F, 0.0F).endVertex();
        poseStack.popPose();
    }
}