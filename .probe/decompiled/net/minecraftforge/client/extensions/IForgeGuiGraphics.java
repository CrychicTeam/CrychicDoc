package net.minecraftforge.client.extensions;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;

public interface IForgeGuiGraphics {

    int DEFAULT_BACKGROUND_COLOR = -267386864;

    int DEFAULT_BORDER_COLOR_START = 1347420415;

    int DEFAULT_BORDER_COLOR_END = 1344798847;

    String UNDO_CHAR = "↶";

    String RESET_CHAR = "☄";

    String VALID = "✔";

    String INVALID = "✕";

    int[] TEXT_COLOR_CODES = new int[] { 0, 170, 43520, 43690, 11141120, 11141290, 16755200, 11184810, 5592405, 5592575, 5635925, 5636095, 16733525, 16733695, 16777045, 16777215, 0, 42, 10752, 10794, 2752512, 2752554, 2763264, 2763306, 1381653, 1381695, 1392405, 1392447, 4134165, 4134207, 4144917, 4144959 };

    private GuiGraphics self() {
        return (GuiGraphics) this;
    }

    default int getColorFromFormattingCharacter(char c, boolean isLighter) {
        return TEXT_COLOR_CODES[isLighter ? "0123456789abcdef".indexOf(c) : "0123456789abcdef".indexOf(c) + 16];
    }

    default void blitWithBorder(ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int borderSize) {
        this.blitWithBorder(texture, x, y, u, v, width, height, textureWidth, textureHeight, borderSize, borderSize, borderSize, borderSize);
    }

    default void blitWithBorder(ResourceLocation texture, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int topBorder, int bottomBorder, int leftBorder, int rightBorder) {
        int fillerWidth = textureWidth - leftBorder - rightBorder;
        int fillerHeight = textureHeight - topBorder - bottomBorder;
        int canvasWidth = width - leftBorder - rightBorder;
        int canvasHeight = height - topBorder - bottomBorder;
        int xPasses = canvasWidth / fillerWidth;
        int remainderWidth = canvasWidth % fillerWidth;
        int yPasses = canvasHeight / fillerHeight;
        int remainderHeight = canvasHeight % fillerHeight;
        this.self().blit(texture, x, y, u, v, leftBorder, topBorder);
        this.self().blit(texture, x + leftBorder + canvasWidth, y, u + leftBorder + fillerWidth, v, rightBorder, topBorder);
        this.self().blit(texture, x, y + topBorder + canvasHeight, u, v + topBorder + fillerHeight, leftBorder, bottomBorder);
        this.self().blit(texture, x + leftBorder + canvasWidth, y + topBorder + canvasHeight, u + leftBorder + fillerWidth, v + topBorder + fillerHeight, rightBorder, bottomBorder);
        for (int i = 0; i < xPasses + (remainderWidth > 0 ? 1 : 0); i++) {
            this.self().blit(texture, x + leftBorder + i * fillerWidth, y, u + leftBorder, v, i == xPasses ? remainderWidth : fillerWidth, topBorder);
            this.self().blit(texture, x + leftBorder + i * fillerWidth, y + topBorder + canvasHeight, u + leftBorder, v + topBorder + fillerHeight, i == xPasses ? remainderWidth : fillerWidth, bottomBorder);
            for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
                this.self().blit(texture, x + leftBorder + i * fillerWidth, y + topBorder + j * fillerHeight, u + leftBorder, v + topBorder, i == xPasses ? remainderWidth : fillerWidth, j == yPasses ? remainderHeight : fillerHeight);
            }
        }
        for (int j = 0; j < yPasses + (remainderHeight > 0 ? 1 : 0); j++) {
            this.self().blit(texture, x, y + topBorder + j * fillerHeight, u, v + topBorder, leftBorder, j == yPasses ? remainderHeight : fillerHeight);
            this.self().blit(texture, x + leftBorder + canvasWidth, y + topBorder + j * fillerHeight, u + leftBorder + fillerWidth, v + topBorder, rightBorder, j == yPasses ? remainderHeight : fillerHeight);
        }
    }

    default void blitInscribed(ResourceLocation texture, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight) {
        this.blitInscribed(texture, x, y, boundsWidth, boundsHeight, rectWidth, rectHeight, true, true);
    }

    default void blitInscribed(ResourceLocation texture, int x, int y, int boundsWidth, int boundsHeight, int rectWidth, int rectHeight, boolean centerX, boolean centerY) {
        if (rectWidth * boundsHeight > rectHeight * boundsWidth) {
            int h = boundsHeight;
            boundsHeight = (int) ((double) boundsWidth * ((double) rectHeight / (double) rectWidth));
            if (centerY) {
                y += (h - boundsHeight) / 2;
            }
        } else {
            int w = boundsWidth;
            boundsWidth = (int) ((double) boundsHeight * ((double) rectWidth / (double) rectHeight));
            if (centerX) {
                x += (w - boundsWidth) / 2;
            }
        }
        this.self().blit(texture, x, y, boundsWidth, boundsHeight, 0.0F, 0.0F, rectWidth, rectHeight, rectWidth, rectHeight);
    }

    default void blitNineSlicedSized(ResourceLocation texture, int x, int y, int width, int height, int sliceSize, int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        this.blitNineSlicedSized(texture, x, y, width, height, sliceSize, sliceSize, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    default void blitNineSlicedSized(ResourceLocation texture, int x, int y, int width, int height, int sliceWidth, int sliceHeight, int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        this.blitNineSlicedSized(texture, x, y, width, height, sliceWidth, sliceHeight, sliceWidth, sliceHeight, uWidth, vHeight, uOffset, vOffset, textureWidth, textureHeight);
    }

    default void blitNineSlicedSized(ResourceLocation texture, int x, int y, int width, int height, int cornerWidth, int cornerHeight, int edgeWidth, int edgeHeight, int uWidth, int vHeight, int uOffset, int vOffset, int textureWidth, int textureHeight) {
        cornerWidth = Math.min(cornerWidth, width / 2);
        edgeWidth = Math.min(edgeWidth, width / 2);
        cornerHeight = Math.min(cornerHeight, height / 2);
        edgeHeight = Math.min(edgeHeight, height / 2);
        GuiGraphics self = this.self();
        if (width == uWidth && height == vHeight) {
            self.blit(texture, x, y, (float) uOffset, (float) vOffset, width, height, textureWidth, textureHeight);
        } else if (height == vHeight) {
            self.blit(texture, x, y, (float) uOffset, (float) vOffset, cornerWidth, height, textureWidth, textureHeight);
            self.blitRepeating(texture, x + cornerWidth, y, width - edgeWidth - cornerWidth, height, uOffset + cornerWidth, vOffset, uWidth - edgeWidth - cornerWidth, vHeight, textureWidth, textureHeight);
            self.blit(texture, x + width - edgeWidth, y, (float) (uOffset + uWidth - edgeWidth), (float) vOffset, edgeWidth, height, textureWidth, textureHeight);
        } else if (width == uWidth) {
            self.blit(texture, x, y, (float) uOffset, (float) vOffset, width, cornerHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x, y + cornerHeight, width, height - edgeHeight - cornerHeight, uOffset, vOffset + cornerHeight, uWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            self.blit(texture, x, y + height - edgeHeight, (float) uOffset, (float) (vOffset + vHeight - edgeHeight), width, edgeHeight, textureWidth, textureHeight);
        } else {
            self.blit(texture, x, y, (float) uOffset, (float) vOffset, cornerWidth, cornerHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x + cornerWidth, y, width - edgeWidth - cornerWidth, cornerHeight, uOffset + cornerWidth, vOffset, uWidth - edgeWidth - cornerWidth, cornerHeight, textureWidth, textureHeight);
            self.blit(texture, x + width - edgeWidth, y, (float) (uOffset + uWidth - edgeWidth), (float) vOffset, edgeWidth, cornerHeight, textureWidth, textureHeight);
            self.blit(texture, x, y + height - edgeHeight, (float) uOffset, (float) (vOffset + vHeight - edgeHeight), cornerWidth, edgeHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x + cornerWidth, y + height - edgeHeight, width - edgeWidth - cornerWidth, edgeHeight, uOffset + cornerWidth, vOffset + vHeight - edgeHeight, uWidth - edgeWidth - cornerWidth, edgeHeight, textureWidth, textureHeight);
            self.blit(texture, x + width - edgeWidth, y + height - edgeHeight, (float) (uOffset + uWidth - edgeWidth), (float) (vOffset + vHeight - edgeHeight), edgeWidth, edgeHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x, y + cornerHeight, cornerWidth, height - edgeHeight - cornerHeight, uOffset, vOffset + cornerHeight, cornerWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x + cornerWidth, y + cornerHeight, width - edgeWidth - cornerWidth, height - edgeHeight - cornerHeight, uOffset + cornerWidth, vOffset + cornerHeight, uWidth - edgeWidth - cornerWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
            self.blitRepeating(texture, x + width - edgeWidth, y + cornerHeight, cornerWidth, height - edgeHeight - cornerHeight, uOffset + uWidth - edgeWidth, vOffset + cornerHeight, edgeWidth, vHeight - edgeHeight - cornerHeight, textureWidth, textureHeight);
        }
    }
}