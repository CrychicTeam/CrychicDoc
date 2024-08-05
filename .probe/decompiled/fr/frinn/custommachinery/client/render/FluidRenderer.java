package fr.frinn.custommachinery.client.render;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import dev.architectury.fluid.FluidStack;
import dev.architectury.hooks.fluid.FluidStackHooks;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.joml.Matrix4f;

public class FluidRenderer {

    private static final int MIN_FLUID_HEIGHT = 1;

    private static final int TEXTURE_SIZE = 16;

    public static void renderFluid(PoseStack poseStack, int posX, int posY, int width, int height, FluidStack fluidStack, long capacity) {
        Fluid fluid = fluidStack.getFluid();
        if (fluid != null && fluid != Fluids.EMPTY) {
            RenderSystem.enableBlend();
            poseStack.pushPose();
            poseStack.translate((float) posX, (float) posY, 0.0F);
            TextureAtlasSprite sprite = FluidStackHooks.getStillTexture(fluidStack);
            int fluidColor = FluidStackHooks.getColor(fluidStack);
            long amount = fluidStack.getAmount();
            int scaledAmount = (int) (amount * (long) height / capacity);
            if (amount > 0L && scaledAmount < 1) {
                scaledAmount = 1;
            }
            if (scaledAmount > height) {
                scaledAmount = height;
            }
            drawTiledSprite(poseStack, width, height, fluidColor, scaledAmount, sprite);
            poseStack.popPose();
            RenderSystem.disableBlend();
        }
    }

    private static void drawTiledSprite(PoseStack poseStack, int tiledWidth, int tiledHeight, int color, int scaledAmount, TextureAtlasSprite sprite) {
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderTexture(0, InventoryMenu.BLOCK_ATLAS);
        Matrix4f matrix = poseStack.last().pose();
        setGLColorFromInt(color);
        int xTileCount = tiledWidth / 16;
        int xRemainder = tiledWidth - xTileCount * 16;
        int yTileCount = scaledAmount / 16;
        int yRemainder = scaledAmount - yTileCount * 16;
        for (int xTile = 0; xTile <= xTileCount; xTile++) {
            for (int yTile = 0; yTile <= yTileCount; yTile++) {
                int width = xTile == xTileCount ? xRemainder : 16;
                int height = yTile == yTileCount ? yRemainder : 16;
                int x = xTile * 16;
                int y = tiledHeight - (yTile + 1) * 16;
                if (width > 0 && height > 0) {
                    int maskTop = 16 - height;
                    int maskRight = 16 - width;
                    drawTextureWithMasking(matrix, (float) x, (float) y, sprite, maskTop, maskRight, 100.0F);
                }
            }
        }
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
    }

    private static void setGLColorFromInt(int color) {
        float red = (float) (color >> 16 & 0xFF) / 255.0F;
        float green = (float) (color >> 8 & 0xFF) / 255.0F;
        float blue = (float) (color & 0xFF) / 255.0F;
        float alpha = (float) (color >> 24 & 0xFF) / 255.0F;
        if (alpha == 0.0F) {
            alpha = 1.0F;
        }
        RenderSystem.setShaderColor(red, green, blue, alpha);
    }

    private static void drawTextureWithMasking(Matrix4f matrix, float xCoord, float yCoord, TextureAtlasSprite textureSprite, int maskTop, int maskRight, float zLevel) {
        float uMin = textureSprite.getU0();
        float uMax = textureSprite.getU1();
        float vMin = textureSprite.getV0();
        float vMax = textureSprite.getV1();
        uMax -= (float) maskRight / 16.0F * (uMax - uMin);
        vMax -= (float) maskTop / 16.0F * (vMax - vMin);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + 16.0F, zLevel).uv(uMin, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - (float) maskRight, yCoord + 16.0F, zLevel).uv(uMax, vMax).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord + 16.0F - (float) maskRight, yCoord + (float) maskTop, zLevel).uv(uMax, vMin).endVertex();
        bufferBuilder.m_252986_(matrix, xCoord, yCoord + (float) maskTop, zLevel).uv(uMin, vMin).endVertex();
        tessellator.end();
    }
}