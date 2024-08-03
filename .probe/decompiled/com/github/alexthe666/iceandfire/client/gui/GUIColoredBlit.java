package com.github.alexthe666.iceandfire.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class GUIColoredBlit {

    public static void blit(PoseStack poseStack0, int int1, int int2, int int3, int int4, float float5, float float6, int int7, int int8, int int9, int int10, float alpha) {
        innerBlit(poseStack0, int1, int1 + int3, int2, int2 + int4, 0, int7, int8, float5, float6, int9, int10, alpha);
    }

    public static void blit(PoseStack poseStack0, int int1, int int2, float float3, float float4, int int5, int int6, int int7, int int8, float alpha) {
        blit(poseStack0, int1, int2, int5, int6, float3, float4, int5, int6, int7, int8, alpha);
    }

    private static void innerBlit(PoseStack poseStack0, int int1, int int2, int int3, int int4, int int5, int int6, int int7, float float8, float float9, int int10, int int11, float alpha) {
        innerBlit(poseStack0.last().pose(), int1, int2, int3, int4, int5, (float8 + 0.0F) / (float) int10, (float8 + (float) int6) / (float) int10, (float9 + 0.0F) / (float) int11, (float9 + (float) int7) / (float) int11, alpha);
    }

    private static void innerBlit(Matrix4f matrixF0, int int1, int int2, int int3, int int4, int int5, float float6, float float7, float float8, float float9, float alpha) {
        RenderSystem.setShader(GameRenderer::m_172820_);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferbuilder.m_252986_(matrixF0, (float) int1, (float) int4, (float) int5).uv(float6, float9).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        bufferbuilder.m_252986_(matrixF0, (float) int2, (float) int4, (float) int5).uv(float7, float9).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        bufferbuilder.m_252986_(matrixF0, (float) int2, (float) int3, (float) int5).uv(float7, float8).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        bufferbuilder.m_252986_(matrixF0, (float) int1, (float) int3, (float) int5).uv(float6, float8).color(1.0F, 1.0F, 1.0F, alpha).endVertex();
        bufferbuilder.end();
        BufferUploader.reset();
    }
}