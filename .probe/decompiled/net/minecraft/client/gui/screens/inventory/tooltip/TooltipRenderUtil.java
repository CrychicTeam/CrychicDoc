package net.minecraft.client.gui.screens.inventory.tooltip;

import net.minecraft.client.gui.GuiGraphics;

public class TooltipRenderUtil {

    public static final int MOUSE_OFFSET = 12;

    private static final int PADDING = 3;

    public static final int PADDING_LEFT = 3;

    public static final int PADDING_RIGHT = 3;

    public static final int PADDING_TOP = 3;

    public static final int PADDING_BOTTOM = 3;

    private static final int BACKGROUND_COLOR = -267386864;

    private static final int BORDER_COLOR_TOP = 1347420415;

    private static final int BORDER_COLOR_BOTTOM = 1344798847;

    public static void renderTooltipBackground(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
        int $$6 = int1 - 3;
        int $$7 = int2 - 3;
        int $$8 = int3 + 3 + 3;
        int $$9 = int4 + 3 + 3;
        renderHorizontalLine(guiGraphics0, $$6, $$7 - 1, $$8, int5, -267386864);
        renderHorizontalLine(guiGraphics0, $$6, $$7 + $$9, $$8, int5, -267386864);
        renderRectangle(guiGraphics0, $$6, $$7, $$8, $$9, int5, -267386864);
        renderVerticalLine(guiGraphics0, $$6 - 1, $$7, $$9, int5, -267386864);
        renderVerticalLine(guiGraphics0, $$6 + $$8, $$7, $$9, int5, -267386864);
        renderFrameGradient(guiGraphics0, $$6, $$7 + 1, $$8, $$9, int5, 1347420415, 1344798847);
    }

    private static void renderFrameGradient(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6, int int7) {
        renderVerticalLineGradient(guiGraphics0, int1, int2, int4 - 2, int5, int6, int7);
        renderVerticalLineGradient(guiGraphics0, int1 + int3 - 1, int2, int4 - 2, int5, int6, int7);
        renderHorizontalLine(guiGraphics0, int1, int2 - 1, int3, int5, int6);
        renderHorizontalLine(guiGraphics0, int1, int2 - 1 + int4 - 1, int3, int5, int7);
    }

    private static void renderVerticalLine(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
        guiGraphics0.fill(int1, int2, int1 + 1, int2 + int3, int4, int5);
    }

    private static void renderVerticalLineGradient(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6) {
        guiGraphics0.fillGradient(int1, int2, int1 + 1, int2 + int3, int4, int5, int6);
    }

    private static void renderHorizontalLine(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5) {
        guiGraphics0.fill(int1, int2, int1 + int3, int2 + 1, int4, int5);
    }

    private static void renderRectangle(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4, int int5, int int6) {
        guiGraphics0.fill(int1, int2, int1 + int3, int2 + int4, int5, int6);
    }
}