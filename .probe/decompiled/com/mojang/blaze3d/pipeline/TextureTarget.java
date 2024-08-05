package com.mojang.blaze3d.pipeline;

import com.mojang.blaze3d.systems.RenderSystem;

public class TextureTarget extends RenderTarget {

    public TextureTarget(int int0, int int1, boolean boolean2, boolean boolean3) {
        super(boolean2);
        RenderSystem.assertOnRenderThreadOrInit();
        this.m_83941_(int0, int1, boolean3);
    }
}