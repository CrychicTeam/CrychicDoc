package com.sihenzhang.crockpot.integration.jei.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class DrawableNineSliceResource implements IDrawable {

    private final ResourceLocation resourceLocation;

    private final int textureWidth;

    private final int textureHeight;

    private final int u;

    private final int v;

    private final int actualWidth;

    private final int actualHeight;

    private final int width;

    private final int height;

    private final int sliceLeft;

    private final int sliceRight;

    private final int sliceTop;

    private final int sliceBottom;

    public DrawableNineSliceResource(ResourceLocation resourceLocation, int u, int v, int actualWidth, int actualHeight, int width, int height, int sliceLeft, int sliceRight, int sliceTop, int sliceBottom, int textureWidth, int textureHeight) {
        this.resourceLocation = resourceLocation;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
        this.u = u;
        this.v = v;
        this.actualWidth = actualWidth;
        this.actualHeight = actualHeight;
        this.width = width;
        this.height = height;
        this.sliceLeft = sliceLeft;
        this.sliceRight = sliceRight;
        this.sliceTop = sliceTop;
        this.sliceBottom = sliceBottom;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, this.resourceLocation);
        float scaledWidth = 1.0F / (float) this.textureWidth;
        float scaledHeight = 1.0F / (float) this.textureHeight;
        float uMin = (float) this.u * scaledWidth;
        float uMax = (float) (this.u + this.actualWidth) * scaledWidth;
        float vMin = (float) this.v * scaledHeight;
        float vMax = (float) (this.v + this.actualHeight) * scaledHeight;
        float uSize = uMax - uMin;
        float vSize = vMax - vMin;
        float uLeft = uMin + uSize * ((float) this.sliceLeft / (float) this.actualWidth);
        float uRight = uMax - uSize * ((float) this.sliceRight / (float) this.actualWidth);
        float vTop = vMin + vSize * ((float) this.sliceTop / (float) this.actualHeight);
        float vBottom = vMax - vSize * ((float) this.sliceBottom / (float) this.actualHeight);
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tesselator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        Matrix4f matrix = guiGraphics.pose().last().pose();
        draw(bufferBuilder, matrix, uMin, vMin, uLeft, vTop, xOffset, yOffset, this.sliceLeft, this.sliceTop);
        draw(bufferBuilder, matrix, uMin, vBottom, uLeft, vMax, xOffset, yOffset + this.height - this.sliceBottom, this.sliceLeft, this.sliceBottom);
        draw(bufferBuilder, matrix, uRight, vMin, uMax, vTop, xOffset + this.width - this.sliceRight, yOffset, this.sliceRight, this.sliceTop);
        draw(bufferBuilder, matrix, uRight, vBottom, uMax, vMax, xOffset + this.width - this.sliceRight, yOffset + this.height - this.sliceBottom, this.sliceRight, this.sliceBottom);
        int middleWidth = this.actualWidth - this.sliceLeft - this.sliceRight;
        int middleHeight = this.actualHeight - this.sliceTop - this.sliceBottom;
        int tiledMiddleWidth = this.width - this.sliceLeft - this.sliceRight;
        int tiledMiddleHeight = this.height - this.sliceTop - this.sliceBottom;
        if (tiledMiddleWidth > 0) {
            this.drawTiled(bufferBuilder, matrix, uLeft, vMin, uRight, vTop, xOffset + this.sliceLeft, yOffset, tiledMiddleWidth, this.sliceTop, middleWidth, this.sliceTop);
            this.drawTiled(bufferBuilder, matrix, uLeft, vBottom, uRight, vMax, xOffset + this.sliceLeft, yOffset + this.height - this.sliceBottom, tiledMiddleWidth, this.sliceBottom, middleWidth, this.sliceBottom);
        }
        if (tiledMiddleHeight > 0) {
            this.drawTiled(bufferBuilder, matrix, uMin, vTop, uLeft, vBottom, xOffset, yOffset + this.sliceTop, this.sliceLeft, tiledMiddleHeight, this.sliceLeft, middleHeight);
            this.drawTiled(bufferBuilder, matrix, uRight, vTop, uMax, vBottom, xOffset + this.width - this.sliceRight, yOffset + this.sliceTop, this.sliceRight, tiledMiddleHeight, this.sliceRight, middleHeight);
        }
        if (tiledMiddleHeight > 0 && tiledMiddleWidth > 0) {
            this.drawTiled(bufferBuilder, matrix, uLeft, vTop, uRight, vBottom, xOffset + this.sliceLeft, yOffset + this.sliceTop, tiledMiddleWidth, tiledMiddleHeight, middleWidth, middleHeight);
        }
        tesselator.end();
    }

    private void drawTiled(BufferBuilder bufferBuilder, Matrix4f matrix, float uMin, float vMin, float uMax, float vMax, int xOffset, int yOffset, int tiledWidth, int tiledHeight, int width, int height) {
        int xTileCount = tiledWidth / width;
        int xRemainder = tiledWidth - xTileCount * width;
        int yTileCount = tiledHeight / height;
        int yRemainder = tiledHeight - yTileCount * height;
        int yStart = yOffset + tiledHeight;
        float uSize = uMax - uMin;
        float vSize = vMax - vMin;
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int tileWidth = xTile == xTileCount ? xRemainder : width;
                int tileHeight = yTile == yTileCount ? yRemainder : height;
                int x = xOffset + xTile * width;
                int y = yStart - (yTile + 1) * height;
                if (tileWidth > 0 && tileHeight > 0) {
                    int maskRight = width - tileWidth;
                    int maskTop = height - tileHeight;
                    float uOffset = (float) maskRight / (float) width * uSize;
                    float vOffset = (float) maskTop / (float) height * vSize;
                    draw(bufferBuilder, matrix, uMin, vMin + vOffset, uMax - uOffset, vMax, x, y + maskTop, tileWidth, tileHeight);
                }
            }
        }
    }

    private static void draw(BufferBuilder bufferBuilder, Matrix4f matrix, float uMin, float vMin, float uMax, float vMax, int xOffset, int yOffset, int width, int height) {
        bufferBuilder.m_252986_(matrix, (float) xOffset, (float) (yOffset + height), 0.0F).uv(uMin, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, (float) (xOffset + width), (float) (yOffset + height), 0.0F).uv(uMax, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, (float) (xOffset + width), (float) yOffset, 0.0F).uv(uMax, vMin).endVertex();
        bufferBuilder.m_252986_(matrix, (float) xOffset, (float) yOffset, 0.0F).uv(uMin, vMin).endVertex();
    }
}