package me.jellysquid.mods.sodium.client.model.quad;

import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;

public interface ModelQuadView {

    float getX(int var1);

    float getY(int var1);

    float getZ(int var1);

    int getColor(int var1);

    float getTexU(int var1);

    float getTexV(int var1);

    int getLight(int var1);

    int getFlags();

    int getColorIndex();

    TextureAtlasSprite getSprite();

    Direction getLightFace();

    int getForgeNormal(int var1);

    default boolean hasColor() {
        return this.getColorIndex() != -1;
    }

    default boolean hasAmbientOcclusion() {
        return true;
    }
}