package com.simibubi.create.foundation.gui.element;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.GameRenderer;
import org.joml.Matrix4f;

public class BoxElement extends RenderElement {

    protected Color background = new Color(-16777216, true);

    protected Color borderTop = new Color(1090514653, true);

    protected Color borderBot = new Color(553643741, true);

    protected int borderOffset = 2;

    public <T extends BoxElement> T withBackground(Color color) {
        this.background = color;
        return (T) this;
    }

    public <T extends BoxElement> T withBackground(int color) {
        return this.withBackground(new Color(color, true));
    }

    public <T extends BoxElement> T flatBorder(Color color) {
        this.borderTop = color;
        this.borderBot = color;
        return (T) this;
    }

    public <T extends BoxElement> T flatBorder(int color) {
        return this.flatBorder(new Color(color, true));
    }

    public <T extends BoxElement> T gradientBorder(Couple<Color> colors) {
        this.borderTop = colors.getFirst();
        this.borderBot = colors.getSecond();
        return (T) this;
    }

    public <T extends BoxElement> T gradientBorder(Color top, Color bot) {
        this.borderTop = top;
        this.borderBot = bot;
        return (T) this;
    }

    public <T extends BoxElement> T gradientBorder(int top, int bot) {
        return this.gradientBorder(new Color(top, true), new Color(bot, true));
    }

    public <T extends BoxElement> T withBorderOffset(int offset) {
        this.borderOffset = offset;
        return (T) this;
    }

    @Override
    public void render(GuiGraphics graphics) {
        this.renderBox(graphics.pose());
    }

    protected void renderBox(PoseStack ms) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::m_172811_);
        Matrix4f model = ms.last().pose();
        int f = this.borderOffset;
        Color c1 = this.background.copy().scaleAlpha(this.alpha);
        Color c2 = this.borderTop.copy().scaleAlpha(this.alpha);
        Color c3 = this.borderBot.copy().scaleAlpha(this.alpha);
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder b = tessellator.getBuilder();
        b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f - 2.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f - 2.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 2.0F, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 2.0F, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + 2.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + 2.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 2.0F + (float) this.width, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 2.0F + (float) this.width, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f - 1.0F, this.z).color(c1.getRed(), c1.getGreen(), c1.getBlue(), c1.getAlpha()).endVertex();
        tessellator.end();
        b.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f - 1.0F, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f - 1.0F, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x - (float) f - 1.0F, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + 1.0F + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + (float) this.width, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + (float) this.width, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y + (float) f + (float) this.height, this.z).color(c3.getRed(), c3.getGreen(), c3.getBlue(), c3.getAlpha()).endVertex();
        b.m_252986_(model, this.x + (float) f + 1.0F + (float) this.width, this.y - (float) f, this.z).color(c2.getRed(), c2.getGreen(), c2.getBlue(), c2.getAlpha()).endVertex();
        tessellator.end();
        RenderSystem.disableBlend();
    }
}