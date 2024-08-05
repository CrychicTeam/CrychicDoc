package net.minecraftforge.client.model.pipeline;

import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.blaze3d.vertex.VertexFormatElement;

public abstract class VertexConsumerWrapper implements VertexConsumer {

    protected final VertexConsumer parent;

    public VertexConsumerWrapper(VertexConsumer parent) {
        this.parent = parent;
    }

    @Override
    public VertexConsumer vertex(double x, double y, double z) {
        this.parent.vertex(x, y, z);
        return this;
    }

    @Override
    public VertexConsumer color(int r, int g, int b, int a) {
        this.parent.color(r, g, b, a);
        return this;
    }

    @Override
    public VertexConsumer uv(float u, float v) {
        this.parent.uv(u, v);
        return this;
    }

    @Override
    public VertexConsumer overlayCoords(int u, int v) {
        this.parent.overlayCoords(u, v);
        return this;
    }

    @Override
    public VertexConsumer uv2(int u, int v) {
        this.parent.uv2(u, v);
        return this;
    }

    @Override
    public VertexConsumer normal(float x, float y, float z) {
        this.parent.normal(x, y, z);
        return this;
    }

    public VertexConsumer misc(VertexFormatElement element, int... values) {
        this.parent.misc(element, values);
        return this;
    }

    @Override
    public void endVertex() {
        this.parent.endVertex();
    }

    @Override
    public void defaultColor(int r, int g, int b, int a) {
        this.parent.defaultColor(r, g, b, a);
    }

    @Override
    public void unsetDefaultColor() {
        this.parent.unsetDefaultColor();
    }
}