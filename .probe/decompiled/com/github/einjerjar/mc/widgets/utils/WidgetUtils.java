package com.github.einjerjar.mc.widgets.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.GameRenderer;

public class WidgetUtils {

    private WidgetUtils() {
    }

    public static double clamp(double x, double min, double max) {
        return Math.max(Math.min(x, max), min);
    }

    public static int clamp(int x, int min, int max) {
        return Math.max(Math.min(x, max), min);
    }

    public static void drawQuad(Tesselator ts, BufferBuilder bb, int left, int right, int top, int bottom, int color, boolean initAndBuild) {
        if (initAndBuild) {
            RenderSystem.setShader(GameRenderer::m_172811_);
            bb.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        }
        WidgetUtils.SColor lColor = new WidgetUtils.SColor(color);
        int a = lColor.a;
        int r = lColor.r;
        int g = lColor.g;
        int b = lColor.b;
        bb.m_5483_((double) left, (double) bottom, 0.0).color(r, g, b, a).endVertex();
        bb.m_5483_((double) right, (double) bottom, 0.0).color(r, g, b, a).endVertex();
        bb.m_5483_((double) right, (double) top, 0.0).color(r, g, b, a).endVertex();
        bb.m_5483_((double) left, (double) top, 0.0).color(r, g, b, a).endVertex();
        if (initAndBuild) {
            ts.end();
        }
    }

    public static class SColor {

        public final int a;

        public final int r;

        public final int g;

        public final int b;

        public SColor(float a, float r, float g, float b) {
            this.a = (int) (a * 255.0F);
            this.r = (int) (r * 255.0F);
            this.g = (int) (g * 255.0F);
            this.b = (int) (b * 255.0F);
        }

        public SColor(int color) {
            this.a = color >> 24 & 0xFF;
            this.r = color >> 16 & 0xFF;
            this.g = color >> 8 & 0xFF;
            this.b = color & 0xFF;
        }
    }
}