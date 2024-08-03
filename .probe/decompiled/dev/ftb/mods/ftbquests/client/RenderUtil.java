package dev.ftb.mods.ftbquests.client;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import org.joml.Matrix4f;

public class RenderUtil {

    public static final int FULL_BRIGHT = 15728880;

    private final PoseStack matrixStack;

    private final VertexConsumer builder;

    private final float x;

    private final float y;

    private int packedLightIn = 15728880;

    private float u1 = 0.0F;

    private float v1 = 0.0F;

    private float u2 = 1.0F;

    private float v2 = 1.0F;

    private float w = 16.0F;

    private float h = 16.0F;

    private int color = -1;

    private RenderUtil(PoseStack matrixStack, VertexConsumer builder, float x, float y) {
        this.matrixStack = matrixStack;
        this.builder = builder;
        this.x = x;
        this.y = y;
    }

    public static RenderUtil create(PoseStack matrixStack, VertexConsumer builder, float x, float y) {
        return new RenderUtil(matrixStack, builder, x, y);
    }

    public RenderUtil withUV(float u1, float v1, float u2, float v2) {
        this.u1 = u1;
        this.v1 = v1;
        this.u2 = u2;
        this.v2 = v2;
        return this;
    }

    public RenderUtil withSize(float w, float h) {
        this.w = w;
        this.h = h;
        return this;
    }

    public RenderUtil withColor(int color) {
        this.color = color;
        return this;
    }

    public RenderUtil withLighting(int packedLightIn) {
        this.packedLightIn = packedLightIn;
        return this;
    }

    public void draw() {
        Matrix4f posMat = this.matrixStack.last().pose();
        this.builder.vertex(posMat, this.x, this.y + this.h, 0.0F).color(this.color).uv(this.u1, this.v2).uv2(this.packedLightIn).endVertex();
        this.builder.vertex(posMat, this.x + this.w, this.y + this.h, 0.0F).color(this.color).uv(this.u2, this.v2).uv2(this.packedLightIn).endVertex();
        this.builder.vertex(posMat, this.x + this.w, this.y, 0.0F).color(this.color).uv(this.u2, this.v1).uv2(this.packedLightIn).endVertex();
        this.builder.vertex(posMat, this.x, this.y, 0.0F).color(this.color).uv(this.u1, this.v1).uv2(this.packedLightIn).endVertex();
    }
}