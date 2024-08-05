package com.mna.gui.radial.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;

public class DrawingContext {

    public final int width;

    public final int height;

    public final float x;

    public final float y;

    public final float z;

    public final Font fontRenderer;

    public final GuiGraphics guiGraphics;

    public final IDrawingHelper drawingHelper;

    public DrawingContext(GuiGraphics guiGraphics, int width, int height, float x, float y, float z, Font fontRenderer, IDrawingHelper drawingHelper) {
        this.guiGraphics = guiGraphics;
        this.width = width;
        this.height = height;
        this.x = x;
        this.y = y;
        this.z = z;
        this.fontRenderer = fontRenderer;
        this.drawingHelper = drawingHelper;
    }
}