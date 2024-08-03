package me.jellysquid.mods.sodium.client.model.quad;

import me.jellysquid.mods.sodium.client.util.ModelQuadUtil;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class ModelQuad implements ModelQuadViewMutable {

    private final int[] data = new int[32];

    private int flags;

    private TextureAtlasSprite sprite;

    private int colorIdx;

    private Direction direction;

    private boolean hasAmbientOcclusion = true;

    @Override
    public void setX(int idx, float x) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 0] = Float.floatToRawIntBits(x);
    }

    @Override
    public void setY(int idx, float y) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 0 + 1] = Float.floatToRawIntBits(y);
    }

    @Override
    public void setZ(int idx, float z) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 0 + 2] = Float.floatToRawIntBits(z);
    }

    @Override
    public void setColor(int idx, int color) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 3] = color;
    }

    @Override
    public void setTexU(int idx, float u) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 4] = Float.floatToRawIntBits(u);
    }

    @Override
    public void setTexV(int idx, float v) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 4 + 1] = Float.floatToRawIntBits(v);
    }

    @Override
    public void setLight(int idx, int light) {
        this.data[ModelQuadUtil.vertexOffset(idx) + 6] = light;
    }

    @Override
    public void setFlags(int flags) {
        this.flags = flags;
    }

    @Override
    public void setSprite(TextureAtlasSprite sprite) {
        this.sprite = sprite;
    }

    @Override
    public void setColorIndex(int index) {
        this.colorIdx = index;
    }

    @Override
    public void setLightFace(Direction face) {
        this.direction = face;
    }

    @Override
    public void setHasAmbientOcclusion(boolean hasAmbientOcclusion) {
        this.hasAmbientOcclusion = hasAmbientOcclusion;
    }

    @Override
    public int getColorIndex() {
        return this.colorIdx;
    }

    @Override
    public float getX(int idx) {
        return Float.intBitsToFloat(this.data[ModelQuadUtil.vertexOffset(idx) + 0]);
    }

    @Override
    public float getY(int idx) {
        return Float.intBitsToFloat(this.data[ModelQuadUtil.vertexOffset(idx) + 0 + 1]);
    }

    @Override
    public float getZ(int idx) {
        return Float.intBitsToFloat(this.data[ModelQuadUtil.vertexOffset(idx) + 0 + 2]);
    }

    @Override
    public int getColor(int idx) {
        return this.data[ModelQuadUtil.vertexOffset(idx) + 3];
    }

    @Override
    public float getTexU(int idx) {
        return Float.intBitsToFloat(this.data[ModelQuadUtil.vertexOffset(idx) + 4]);
    }

    @Override
    public float getTexV(int idx) {
        return Float.intBitsToFloat(this.data[ModelQuadUtil.vertexOffset(idx) + 4 + 1]);
    }

    @Override
    public int getLight(int idx) {
        return this.data[ModelQuadUtil.vertexOffset(idx) + 6];
    }

    @Override
    public int getForgeNormal(int idx) {
        return this.data[ModelQuadUtil.vertexOffset(idx) + 7];
    }

    @Override
    public int getFlags() {
        return this.flags;
    }

    @Override
    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    @Override
    public Direction getLightFace() {
        return this.direction;
    }

    @Override
    public boolean hasAmbientOcclusion() {
        return this.hasAmbientOcclusion;
    }
}