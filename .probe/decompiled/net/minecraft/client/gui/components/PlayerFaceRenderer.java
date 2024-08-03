package net.minecraft.client.gui.components;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public class PlayerFaceRenderer {

    public static final int SKIN_HEAD_U = 8;

    public static final int SKIN_HEAD_V = 8;

    public static final int SKIN_HEAD_WIDTH = 8;

    public static final int SKIN_HEAD_HEIGHT = 8;

    public static final int SKIN_HAT_U = 40;

    public static final int SKIN_HAT_V = 8;

    public static final int SKIN_HAT_WIDTH = 8;

    public static final int SKIN_HAT_HEIGHT = 8;

    public static final int SKIN_TEX_WIDTH = 64;

    public static final int SKIN_TEX_HEIGHT = 64;

    public static void draw(GuiGraphics guiGraphics0, ResourceLocation resourceLocation1, int int2, int int3, int int4) {
        draw(guiGraphics0, resourceLocation1, int2, int3, int4, true, false);
    }

    public static void draw(GuiGraphics guiGraphics0, ResourceLocation resourceLocation1, int int2, int int3, int int4, boolean boolean5, boolean boolean6) {
        int $$7 = 8 + (boolean6 ? 8 : 0);
        int $$8 = 8 * (boolean6 ? -1 : 1);
        guiGraphics0.blit(resourceLocation1, int2, int3, int4, int4, 8.0F, (float) $$7, 8, $$8, 64, 64);
        if (boolean5) {
            drawHat(guiGraphics0, resourceLocation1, int2, int3, int4, boolean6);
        }
    }

    private static void drawHat(GuiGraphics guiGraphics0, ResourceLocation resourceLocation1, int int2, int int3, int int4, boolean boolean5) {
        int $$6 = 8 + (boolean5 ? 8 : 0);
        int $$7 = 8 * (boolean5 ? -1 : 1);
        RenderSystem.enableBlend();
        guiGraphics0.blit(resourceLocation1, int2, int3, int4, int4, 40.0F, (float) $$6, 8, $$7, 64, 64);
        RenderSystem.disableBlend();
    }
}