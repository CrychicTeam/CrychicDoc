package me.jellysquid.mods.sodium.client.render.util;

import com.mojang.blaze3d.systems.RenderSystem;

public class RenderAsserts {

    public static boolean validateCurrentThread() {
        if (!RenderSystem.isOnRenderThread()) {
            throw new IllegalStateException("Accessing OpenGL functions from outside the main render thread is not supported when using Embeddium");
        } else {
            return true;
        }
    }
}