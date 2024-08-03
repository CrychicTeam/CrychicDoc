package net.minecraft.client.renderer.block.model;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public class BakedQuad {

    protected final int[] vertices;

    protected final int tintIndex;

    protected final Direction direction;

    protected final TextureAtlasSprite sprite;

    private final boolean shade;

    public BakedQuad(int[] int0, int int1, Direction direction2, TextureAtlasSprite textureAtlasSprite3, boolean boolean4) {
        this.vertices = int0;
        this.tintIndex = int1;
        this.direction = direction2;
        this.sprite = textureAtlasSprite3;
        this.shade = boolean4;
    }

    public TextureAtlasSprite getSprite() {
        return this.sprite;
    }

    public int[] getVertices() {
        return this.vertices;
    }

    public boolean isTinted() {
        return this.tintIndex != -1;
    }

    public int getTintIndex() {
        return this.tintIndex;
    }

    public Direction getDirection() {
        return this.direction;
    }

    public boolean isShade() {
        return this.shade;
    }
}