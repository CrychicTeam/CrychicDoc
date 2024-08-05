package fr.lucreeper74.createmetallurgy.utils;

import com.mojang.blaze3d.vertex.VertexConsumer;

public class TintedVertexBuilder implements VertexConsumer {

    private final VertexConsumer inner;

    private final int tintRed;

    private final int tintGreen;

    private final int tintBlue;

    private final int tintAlpha;

    public TintedVertexBuilder(VertexConsumer inner, int tintRed, int tintGreen, int tintBlue, int tintAlpha) {
        this.inner = inner;
        this.tintRed = tintRed;
        this.tintGreen = tintGreen;
        this.tintBlue = tintBlue;
        this.tintAlpha = tintAlpha;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        return this.inner.vertex(x, y, z);
    }

    @Override
    public VertexConsumer color(int red, int green, int blue, int alpha) {
        return this.inner.color(red * this.tintRed / 255, green * this.tintGreen / 255, blue * this.tintBlue / 255, alpha * this.tintAlpha / 255);
    }

    @Override
    public void defaultColor(int red, int green, int blue, int alpha) {
        this.inner.defaultColor(red * this.tintRed / 255, green * this.tintGreen / 255, blue * this.tintBlue / 255, alpha * this.tintAlpha / 255);
    }

    @Override
    public void unsetDefaultColor() {
        this.inner.unsetDefaultColor();
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        return this.inner.uv(u, v);
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        return this.inner.overlayCoords(u, v);
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        return this.inner.uv2(u, v);
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        return this.inner.normal(x, y, z);
    }

    @Override
    public void endVertex() {
        this.inner.endVertex();
    }
}