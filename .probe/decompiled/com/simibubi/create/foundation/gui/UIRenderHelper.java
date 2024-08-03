package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.pipeline.RenderTarget;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Window;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Axis;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class UIRenderHelper {

    public static UIRenderHelper.CustomRenderTarget framebuffer;

    public static void init() {
        RenderSystem.recordRenderCall(() -> {
            Window mainWindow = Minecraft.getInstance().getWindow();
            framebuffer = UIRenderHelper.CustomRenderTarget.create(mainWindow);
        });
    }

    public static void updateWindowSize(Window mainWindow) {
        if (framebuffer != null) {
            framebuffer.m_83941_(mainWindow.getWidth(), mainWindow.getHeight(), Minecraft.ON_OSX);
        }
    }

    public static void drawFramebuffer(float alpha) {
        framebuffer.renderWithAlpha(alpha);
    }

    public static void swapAndBlitColor(RenderTarget src, RenderTarget dst) {
        GlStateManager._glBindFramebuffer(36008, src.frameBufferId);
        GlStateManager._glBindFramebuffer(36009, dst.frameBufferId);
        GlStateManager._glBlitFrameBuffer(0, 0, src.viewWidth, src.viewHeight, 0, 0, dst.viewWidth, dst.viewHeight, 16384, 9729);
        GlStateManager._glBindFramebuffer(36160, dst.frameBufferId);
    }

    public static void streak(GuiGraphics graphics, float angle, int x, int y, int breadth, int length) {
        streak(graphics, angle, x, y, breadth, length, Theme.i(Theme.Key.STREAK));
    }

    public static void streak(GuiGraphics graphics, float angle, int x, int y, int breadth, int length, int color) {
        int a1 = -1610612736;
        int a2 = Integer.MIN_VALUE;
        int a3 = 268435456;
        int a4 = 0;
        color &= 16777215;
        int c1 = a1 | color;
        int c2 = a2 | color;
        int c3 = a3 | color;
        int c4 = a4 | color;
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) x, (float) y, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(angle - 90.0F));
        streak(graphics, breadth / 2, length, c1, c2, c3, c4);
        ms.popPose();
    }

    public static void streak(GuiGraphics graphics, float angle, int x, int y, int breadth, int length, Color c) {
        Color color = c.copy().setImmutable();
        int c1 = color.scaleAlpha(0.625F).getRGB();
        int c2 = color.scaleAlpha(0.5F).getRGB();
        int c3 = color.scaleAlpha(0.0625F).getRGB();
        int c4 = color.scaleAlpha(0.0F).getRGB();
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) x, (float) y, 0.0F);
        ms.mulPose(Axis.ZP.rotationDegrees(angle - 90.0F));
        streak(graphics, breadth / 2, length, c1, c2, c3, c4);
        ms.popPose();
    }

    private static void streak(GuiGraphics graphics, int width, int height, int c1, int c2, int c3, int c4) {
        double split1 = 0.5;
        double split2 = 0.75;
        graphics.fillGradient(-width, 0, width, (int) (split1 * (double) height), 0, c1, c2);
        graphics.fillGradient(-width, (int) (split1 * (double) height), width, (int) (split2 * (double) height), 0, c2, c3);
        graphics.fillGradient(-width, (int) (split2 * (double) height), width, height, 0, c3, c4);
    }

    public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, int breadth, int length, Couple<Color> c) {
        angledGradient(graphics, angle, x, y, 0, breadth, length, c);
    }

    public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, int z, int breadth, int length, Couple<Color> c) {
        angledGradient(graphics, angle, x, y, z, breadth, length, c.getFirst(), c.getSecond());
    }

    public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, int breadth, int length, Color color1, Color color2) {
        angledGradient(graphics, angle, x, y, 0, breadth, length, color1, color2);
    }

    public static void angledGradient(GuiGraphics graphics, float angle, int x, int y, int z, int breadth, int length, Color color1, Color color2) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) x, (float) y, (float) z);
        ms.mulPose(Axis.ZP.rotationDegrees(angle - 90.0F));
        int w = breadth / 2;
        graphics.fillGradient(-w, 0, w, length, 0, color1.getRGB(), color2.getRGB());
        ms.popPose();
    }

    public static void breadcrumbArrow(GuiGraphics graphics, int x, int y, int z, int width, int height, int indent, Couple<Color> colors) {
        breadcrumbArrow(graphics, x, y, z, width, height, indent, colors.getFirst(), colors.getSecond());
    }

    public static void breadcrumbArrow(GuiGraphics graphics, int x, int y, int z, int width, int height, int indent, Color startColor, Color endColor) {
        PoseStack matrixStack = graphics.pose();
        matrixStack.pushPose();
        matrixStack.translate((float) (x - indent), (float) y, (float) z);
        breadcrumbArrow(graphics, width, height, indent, startColor, endColor);
        matrixStack.popPose();
    }

    private static void breadcrumbArrow(GuiGraphics graphics, int width, int height, int indent, Color c1, Color c2) {
        float x0 = 0.0F;
        float y0 = (float) height / 2.0F;
        float x1 = (float) indent;
        float y1 = 0.0F;
        float x2 = (float) indent;
        float y2 = (float) height / 2.0F;
        float x3 = (float) indent;
        float y3 = (float) height;
        float x4 = (float) width;
        float y4 = 0.0F;
        float x5 = (float) width;
        float y5 = (float) height / 2.0F;
        float x6 = (float) width;
        float y6 = (float) height;
        float x7 = (float) (indent + width);
        float y7 = 0.0F;
        float x8 = (float) (indent + width);
        float y8 = (float) height;
        indent = Math.abs(indent);
        width = Math.abs(width);
        Color fc1 = Color.mixColors(c1, c2, 0.0F);
        Color fc2 = Color.mixColors(c1, c2, (float) indent / ((float) width + 2.0F * (float) indent));
        Color fc3 = Color.mixColors(c1, c2, (float) (indent + width) / ((float) width + 2.0F * (float) indent));
        Color fc4 = Color.mixColors(c1, c2, 1.0F);
        RenderSystem.enableBlend();
        RenderSystem.disableCull();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172811_);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        Matrix4f model = graphics.pose().last().pose();
        bufferbuilder.begin(VertexFormat.Mode.TRIANGLES, DefaultVertexFormat.POSITION_COLOR);
        bufferbuilder.m_252986_(model, x0, y0, 0.0F).color(fc1.getRed(), fc1.getGreen(), fc1.getBlue(), fc1.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x1, y1, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x2, y2, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x0, y0, 0.0F).color(fc1.getRed(), fc1.getGreen(), fc1.getBlue(), fc1.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x2, y2, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x3, y3, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x3, y3, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x1, y1, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x4, y4, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x3, y3, 0.0F).color(fc2.getRed(), fc2.getGreen(), fc2.getBlue(), fc2.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x4, y4, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x6, y6, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x5, y5, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x4, y4, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x7, y7, 0.0F).color(fc4.getRed(), fc4.getGreen(), fc4.getBlue(), fc4.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x6, y6, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x5, y5, 0.0F).color(fc3.getRed(), fc3.getGreen(), fc3.getBlue(), fc3.getAlpha()).endVertex();
        bufferbuilder.m_252986_(model, x8, y8, 0.0F).color(fc4.getRed(), fc4.getGreen(), fc4.getBlue(), fc4.getAlpha()).endVertex();
        tessellator.end();
        RenderSystem.enableCull();
        RenderSystem.disableBlend();
    }

    public static void drawColoredTexture(GuiGraphics graphics, Color c, int x, int y, int tex_left, int tex_top, int width, int height) {
        drawColoredTexture(graphics, c, x, y, 0, (float) tex_left, (float) tex_top, width, height, 256, 256);
    }

    public static void drawColoredTexture(GuiGraphics graphics, Color c, int x, int y, int z, float tex_left, float tex_top, int width, int height, int sheet_width, int sheet_height) {
        drawColoredTexture(graphics, c, x, x + width, y, y + height, z, width, height, tex_left, tex_top, sheet_width, sheet_height);
    }

    public static void drawStretched(GuiGraphics graphics, int left, int top, int w, int h, int z, AllGuiTextures tex) {
        tex.bind();
        drawTexturedQuad(graphics.pose().last().pose(), Color.WHITE, left, left + w, top, top + h, z, (float) tex.startX / 256.0F, (float) (tex.startX + tex.width) / 256.0F, (float) tex.startY / 256.0F, (float) (tex.startY + tex.height) / 256.0F);
    }

    public static void drawCropped(GuiGraphics graphics, int left, int top, int w, int h, int z, AllGuiTextures tex) {
        tex.bind();
        drawTexturedQuad(graphics.pose().last().pose(), Color.WHITE, left, left + w, top, top + h, z, (float) tex.startX / 256.0F, (float) (tex.startX + w) / 256.0F, (float) tex.startY / 256.0F, (float) (tex.startY + h) / 256.0F);
    }

    private static void drawColoredTexture(GuiGraphics graphics, Color c, int left, int right, int top, int bot, int z, int tex_width, int tex_height, float tex_left, float tex_top, int sheet_width, int sheet_height) {
        drawTexturedQuad(graphics.pose().last().pose(), c, left, right, top, bot, z, (tex_left + 0.0F) / (float) sheet_width, (tex_left + (float) tex_width) / (float) sheet_width, (tex_top + 0.0F) / (float) sheet_height, (tex_top + (float) tex_height) / (float) sheet_height);
    }

    private static void drawTexturedQuad(Matrix4f m, Color c, int left, int right, int top, int bot, int z, float u1, float u2, float v1, float v2) {
        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder bufferbuilder = tesselator.getBuilder();
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172814_);
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.m_252986_(m, (float) left, (float) bot, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u1, v2).endVertex();
        bufferbuilder.m_252986_(m, (float) right, (float) bot, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u2, v2).endVertex();
        bufferbuilder.m_252986_(m, (float) right, (float) top, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u2, v1).endVertex();
        bufferbuilder.m_252986_(m, (float) left, (float) top, (float) z).color(c.getRed(), c.getGreen(), c.getBlue(), c.getAlpha()).uv(u1, v1).endVertex();
        tesselator.end();
        RenderSystem.disableBlend();
    }

    public static void flipForGuiRender(PoseStack poseStack) {
        poseStack.mulPoseMatrix(new Matrix4f().scaling(1.0F, -1.0F, 1.0F));
    }

    public static class CustomRenderTarget extends RenderTarget {

        public CustomRenderTarget(boolean useDepth) {
            super(useDepth);
        }

        public static UIRenderHelper.CustomRenderTarget create(Window mainWindow) {
            UIRenderHelper.CustomRenderTarget framebuffer = new UIRenderHelper.CustomRenderTarget(true);
            framebuffer.m_83941_(mainWindow.getWidth(), mainWindow.getHeight(), Minecraft.ON_OSX);
            framebuffer.m_83931_(0.0F, 0.0F, 0.0F, 0.0F);
            framebuffer.enableStencil();
            return framebuffer;
        }

        public void renderWithAlpha(float alpha) {
            Window window = Minecraft.getInstance().getWindow();
            float vx = (float) window.getGuiScaledWidth();
            float vy = (float) window.getGuiScaledHeight();
            float tx = (float) this.f_83917_ / (float) this.f_83915_;
            float ty = (float) this.f_83918_ / (float) this.f_83916_;
            RenderSystem.enableDepthTest();
            RenderSystem.setShader(() -> Minecraft.getInstance().gameRenderer.blitShader);
            RenderSystem.getShader().setSampler("DiffuseSampler", this.f_83923_);
            this.m_83956_();
            Tesselator tessellator = Tesselator.getInstance();
            BufferBuilder bufferbuilder = tessellator.getBuilder();
            bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
            bufferbuilder.m_5483_(0.0, (double) vy, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, 0.0F).endVertex();
            bufferbuilder.m_5483_((double) vx, (double) vy, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, 0.0F).endVertex();
            bufferbuilder.m_5483_((double) vx, 0.0, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(tx, ty).endVertex();
            bufferbuilder.m_5483_(0.0, 0.0, 0.0).color(1.0F, 1.0F, 1.0F, alpha).uv(0.0F, ty).endVertex();
            tessellator.end();
            this.m_83963_();
        }
    }
}