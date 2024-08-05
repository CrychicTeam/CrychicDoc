package com.github.alexmodguy.alexscaves.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.resources.ResourceLocation;
import org.joml.Matrix4f;

public class ColorBlitHelper {

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, resourceLocation0, int1, int2, 0, (float) int3, (float) int4, int5, int6, 256, 256, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation resourceLocation0, int int1, int int2, int int3, float float4, float float5, int int6, int int7, int int8, int int9, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, resourceLocation0, int1, int1 + int6, int2, int2 + int7, int3, int6, int7, float4, float5, int8, int9, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, float float5, float float6, int int7, int int8, int int9, int int10, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, resourceLocation0, int1, int1 + int3, int2, int2 + int4, 0, int7, int8, float5, float6, int9, int10, r, g, b, a);
    }

    public static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation resourceLocation0, int int1, int int2, float float3, float float4, int int5, int int6, int int7, int int8, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, resourceLocation0, int1, int2, int5, int6, float3, float4, int5, int6, int7, int8, r, g, b, a);
    }

    private static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation resourceLocation0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, float float8, float float9, int int10, int int11, float r, float g, float b, float a) {
        blitWithColor(guiGraphics, resourceLocation0, int1, int2, int3, int4, int5, (float8 + 0.0F) / (float) int10, (float8 + (float) int6) / (float) int10, (float9 + 0.0F) / (float) int11, (float9 + (float) int7) / (float) int11, r, g, b, a);
    }

    private static void blitWithColor(GuiGraphics guiGraphics, ResourceLocation texture, int startX, int endX, int startY, int endY, int zLevel, float u0, float u1, float v0, float v1, float r, float g, float b, float a) {
        RenderSystem.setShaderTexture(0, texture);
        RenderSystem.setShader(GameRenderer::m_172814_);
        RenderSystem.enableBlend();
        Matrix4f matrix4f = guiGraphics.pose().last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.m_252986_(matrix4f, (float) startX, (float) startY, (float) zLevel).color(r, g, b, a).uv(u0, v0).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) startX, (float) endY, (float) zLevel).color(r, g, b, a).uv(u0, v1).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) endX, (float) endY, (float) zLevel).color(r, g, b, a).uv(u1, v1).endVertex();
        bufferbuilder.m_252986_(matrix4f, (float) endX, (float) startY, (float) zLevel).color(r, g, b, a).uv(u1, v0).endVertex();
        BufferUploader.drawWithShader(bufferbuilder.end());
        RenderSystem.disableBlend();
    }
}