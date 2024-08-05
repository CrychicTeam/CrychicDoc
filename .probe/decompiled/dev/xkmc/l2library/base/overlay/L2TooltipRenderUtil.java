package dev.xkmc.l2library.base.overlay;

import net.minecraft.client.gui.GuiGraphics;

public record L2TooltipRenderUtil(GuiGraphics fill, int bg, int bs, int be) {

    public void renderTooltipBackground(int x, int y, int w, int h, int z) {
        int x1 = x - 3;
        int y1 = y - 3;
        int w1 = w + 3 + 3;
        int h1 = h + 3 + 3;
        this.renderHorizontalLine(x1, y1 - 1, w1, z, this.bg);
        this.renderHorizontalLine(x1, y1 + h1, w1, z, this.bg);
        this.renderRectangle(x1, y1, w1, h1, z, this.bg);
        this.renderVerticalLine(x1 - 1, y1, h1, z, this.bg);
        this.renderVerticalLine(x1 + w1, y1, h1, z, this.bg);
        this.renderFrameGradient(x1, y1 + 1, w1, h1, z, this.bs, this.be);
    }

    private void renderFrameGradient(int x, int y, int w, int h, int z, int c0, int c1) {
        this.renderVerticalLineGradient(x, y, h - 2, z, c0, c1);
        this.renderVerticalLineGradient(x + w - 1, y, h - 2, z, c0, c1);
        this.renderHorizontalLine(x, y - 1, w, z, c0);
        this.renderHorizontalLine(x, y - 1 + h - 1, w, z, c1);
    }

    private void renderVerticalLine(int x, int y, int h, int z, int c) {
        this.fill.fillGradient(x, y, x + 1, y + h, z, c, c);
    }

    private void renderVerticalLineGradient(int x, int y, int h, int z, int c0, int c1) {
        this.fill.fillGradient(x, y, x + 1, y + h, z, c0, c1);
    }

    private void renderHorizontalLine(int x, int y, int w, int z, int c) {
        this.fill.fillGradient(x, y, x + w, y + 1, z, c, c);
    }

    private void renderRectangle(int x, int y, int w, int h, int z, int c) {
        this.fill.fillGradient(x, y, x + w, y + h, z, c, c);
    }
}