package me.jellysquid.mods.sodium.client.util;

import com.mojang.blaze3d.systems.RenderSystem;

public class TextureUtil {

    public static int getLightTextureId() {
        return RenderSystem.getShaderTexture(2);
    }

    public static int getBlockTextureId() {
        return RenderSystem.getShaderTexture(0);
    }
}