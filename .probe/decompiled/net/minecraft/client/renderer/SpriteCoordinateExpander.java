package net.minecraft.client.renderer;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;

public class SpriteCoordinateExpander implements VertexConsumer {

    private final VertexConsumer delegate;

    private final TextureAtlasSprite sprite;

    public SpriteCoordinateExpander(VertexConsumer vertexConsumer0, TextureAtlasSprite textureAtlasSprite1) {
        this.delegate = vertexConsumer0;
        this.sprite = textureAtlasSprite1;
    }

    @Override
    public VertexConsumer vertex(double double0, double double1, double double2) {
        return this.delegate.vertex(double0, double1, double2);
    }

    @Override
    public VertexConsumer color(int int0, int int1, int int2, int int3) {
        return this.delegate.color(int0, int1, int2, int3);
    }

    @Override
    public VertexConsumer uv(float float0, float float1) {
        return this.delegate.uv(this.sprite.getU((double) (float0 * 16.0F)), this.sprite.getV((double) (float1 * 16.0F)));
    }

    @Override
    public VertexConsumer overlayCoords(int int0, int int1) {
        return this.delegate.overlayCoords(int0, int1);
    }

    @Override
    public VertexConsumer uv2(int int0, int int1) {
        return this.delegate.uv2(int0, int1);
    }

    @Override
    public VertexConsumer normal(float float0, float float1, float float2) {
        return this.delegate.normal(float0, float1, float2);
    }

    @Override
    public void endVertex() {
        this.delegate.endVertex();
    }

    @Override
    public void defaultColor(int int0, int int1, int int2, int int3) {
        this.delegate.defaultColor(int0, int1, int2, int3);
    }

    @Override
    public void unsetDefaultColor() {
        this.delegate.unsetDefaultColor();
    }

    @Override
    public void vertex(float float0, float float1, float float2, float float3, float float4, float float5, float float6, float float7, float float8, int int9, int int10, float float11, float float12, float float13) {
        this.delegate.vertex(float0, float1, float2, float3, float4, float5, float6, this.sprite.getU((double) (float7 * 16.0F)), this.sprite.getV((double) (float8 * 16.0F)), int9, int10, float11, float12, float13);
    }
}