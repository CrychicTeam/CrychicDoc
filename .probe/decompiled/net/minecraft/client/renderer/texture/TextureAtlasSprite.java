package net.minecraft.client.renderer.texture;

import com.mojang.blaze3d.vertex.VertexConsumer;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.SpriteCoordinateExpander;
import net.minecraft.resources.ResourceLocation;

public class TextureAtlasSprite {

    private final ResourceLocation atlasLocation;

    private final SpriteContents contents;

    final int x;

    final int y;

    private final float u0;

    private final float u1;

    private final float v0;

    private final float v1;

    protected TextureAtlasSprite(ResourceLocation resourceLocation0, SpriteContents spriteContents1, int int2, int int3, int int4, int int5) {
        this.atlasLocation = resourceLocation0;
        this.contents = spriteContents1;
        this.x = int4;
        this.y = int5;
        this.u0 = (float) int4 / (float) int2;
        this.u1 = (float) (int4 + spriteContents1.width()) / (float) int2;
        this.v0 = (float) int5 / (float) int3;
        this.v1 = (float) (int5 + spriteContents1.height()) / (float) int3;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public float getU0() {
        return this.u0;
    }

    public float getU1() {
        return this.u1;
    }

    public SpriteContents contents() {
        return this.contents;
    }

    @Nullable
    public TextureAtlasSprite.Ticker createTicker() {
        final SpriteTicker $$0 = this.contents.createTicker();
        return $$0 != null ? new TextureAtlasSprite.Ticker() {

            @Override
            public void tickAndUpload() {
                $$0.tickAndUpload(TextureAtlasSprite.this.x, TextureAtlasSprite.this.y);
            }

            @Override
            public void close() {
                $$0.close();
            }
        } : null;
    }

    public float getU(double double0) {
        float $$1 = this.u1 - this.u0;
        return this.u0 + $$1 * (float) double0 / 16.0F;
    }

    public float getUOffset(float float0) {
        float $$1 = this.u1 - this.u0;
        return (float0 - this.u0) / $$1 * 16.0F;
    }

    public float getV0() {
        return this.v0;
    }

    public float getV1() {
        return this.v1;
    }

    public float getV(double double0) {
        float $$1 = this.v1 - this.v0;
        return this.v0 + $$1 * (float) double0 / 16.0F;
    }

    public float getVOffset(float float0) {
        float $$1 = this.v1 - this.v0;
        return (float0 - this.v0) / $$1 * 16.0F;
    }

    public ResourceLocation atlasLocation() {
        return this.atlasLocation;
    }

    public String toString() {
        return "TextureAtlasSprite{contents='" + this.contents + "', u0=" + this.u0 + ", u1=" + this.u1 + ", v0=" + this.v0 + ", v1=" + this.v1 + "}";
    }

    public void uploadFirstFrame() {
        this.contents.uploadFirstFrame(this.x, this.y);
    }

    private float atlasSize() {
        float $$0 = (float) this.contents.width() / (this.u1 - this.u0);
        float $$1 = (float) this.contents.height() / (this.v1 - this.v0);
        return Math.max($$1, $$0);
    }

    public float uvShrinkRatio() {
        return 4.0F / this.atlasSize();
    }

    public VertexConsumer wrap(VertexConsumer vertexConsumer0) {
        return new SpriteCoordinateExpander(vertexConsumer0, this);
    }

    public interface Ticker extends AutoCloseable {

        void tickAndUpload();

        void close();
    }
}