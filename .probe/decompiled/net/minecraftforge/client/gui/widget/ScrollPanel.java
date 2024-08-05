package net.minecraftforge.client.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import java.util.Collections;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.AbstractContainerEventHandler;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.renderer.GameRenderer;

public abstract class ScrollPanel extends AbstractContainerEventHandler implements Renderable, NarratableEntry {

    private final Minecraft client;

    protected final int width;

    protected final int height;

    protected final int top;

    protected final int bottom;

    protected final int right;

    protected final int left;

    private boolean scrolling;

    protected float scrollDistance;

    protected boolean captureMouse = true;

    protected final int border;

    private final int barWidth;

    private final int barLeft;

    private final int bgColorFrom;

    private final int bgColorTo;

    private final int barBgColor;

    private final int barColor;

    private final int barBorderColor;

    public ScrollPanel(Minecraft client, int width, int height, int top, int left) {
        this(client, width, height, top, left, 4);
    }

    public ScrollPanel(Minecraft client, int width, int height, int top, int left, int border) {
        this(client, width, height, top, left, border, 6);
    }

    public ScrollPanel(Minecraft client, int width, int height, int top, int left, int border, int barWidth) {
        this(client, width, height, top, left, border, barWidth, -1072689136, -804253680);
    }

    public ScrollPanel(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColor) {
        this(client, width, height, top, left, border, barWidth, bgColor, bgColor);
    }

    public ScrollPanel(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo) {
        this(client, width, height, top, left, border, barWidth, bgColorFrom, bgColorTo, -16777216, -8355712, -4144960);
    }

    public ScrollPanel(Minecraft client, int width, int height, int top, int left, int border, int barWidth, int bgColorFrom, int bgColorTo, int barBgColor, int barColor, int barBorderColor) {
        this.client = client;
        this.width = width;
        this.height = height;
        this.top = top;
        this.left = left;
        this.bottom = height + this.top;
        this.right = width + this.left;
        this.barLeft = this.left + this.width - barWidth;
        this.border = border;
        this.barWidth = barWidth;
        this.bgColorFrom = bgColorFrom;
        this.bgColorTo = bgColorTo;
        this.barBgColor = barBgColor;
        this.barColor = barColor;
        this.barBorderColor = barBorderColor;
    }

    protected abstract int getContentHeight();

    protected void drawBackground(GuiGraphics guiGraphics, Tesselator tess, float partialTick) {
        BufferBuilder worldr = tess.getBuilder();
        if (this.client.level != null) {
            this.drawGradientRect(guiGraphics, this.left, this.top, this.right, this.bottom, this.bgColorFrom, this.bgColorTo);
        } else {
            RenderSystem.setShader(GameRenderer::m_172820_);
            RenderSystem.setShaderTexture(0, Screen.BACKGROUND_LOCATION);
            float texScale = 32.0F;
            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
            worldr.m_5483_((double) this.left, (double) this.bottom, 0.0).uv((float) this.left / 32.0F, (float) (this.bottom + (int) this.scrollDistance) / 32.0F).color(32, 32, 32, 255).endVertex();
            worldr.m_5483_((double) this.right, (double) this.bottom, 0.0).uv((float) this.right / 32.0F, (float) (this.bottom + (int) this.scrollDistance) / 32.0F).color(32, 32, 32, 255).endVertex();
            worldr.m_5483_((double) this.right, (double) this.top, 0.0).uv((float) this.right / 32.0F, (float) (this.top + (int) this.scrollDistance) / 32.0F).color(32, 32, 32, 255).endVertex();
            worldr.m_5483_((double) this.left, (double) this.top, 0.0).uv((float) this.left / 32.0F, (float) (this.top + (int) this.scrollDistance) / 32.0F).color(32, 32, 32, 255).endVertex();
            tess.end();
        }
    }

    protected abstract void drawPanel(GuiGraphics var1, int var2, int var3, Tesselator var4, int var5, int var6);

    protected boolean clickPanel(double mouseX, double mouseY, int button) {
        return false;
    }

    private int getMaxScroll() {
        return this.getContentHeight() - (this.height - this.border);
    }

    private void applyScrollLimits() {
        int max = this.getMaxScroll();
        if (max < 0) {
            max /= 2;
        }
        if (this.scrollDistance < 0.0F) {
            this.scrollDistance = 0.0F;
        }
        if (this.scrollDistance > (float) max) {
            this.scrollDistance = (float) max;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double scroll) {
        if (scroll != 0.0) {
            this.scrollDistance = (float) ((double) this.scrollDistance + -scroll * (double) this.getScrollAmount());
            this.applyScrollLimits();
            return true;
        } else {
            return false;
        }
    }

    protected int getScrollAmount() {
        return 20;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return mouseX >= (double) this.left && mouseX <= (double) (this.left + this.width) && mouseY >= (double) this.top && mouseY <= (double) this.bottom;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (super.m_6375_(mouseX, mouseY, button)) {
            return true;
        } else {
            this.scrolling = button == 0 && mouseX >= (double) this.barLeft && mouseX < (double) (this.barLeft + this.barWidth);
            if (this.scrolling) {
                return true;
            } else {
                int mouseListY = (int) mouseY - this.top - this.getContentHeight() + (int) this.scrollDistance - this.border;
                return mouseX >= (double) this.left && mouseX <= (double) this.right && mouseListY < 0 ? this.clickPanel(mouseX - (double) this.left, mouseY - (double) this.top + (double) ((int) this.scrollDistance) - (double) this.border, button) : false;
            }
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (super.m_6348_(mouseX, mouseY, button)) {
            return true;
        } else {
            boolean ret = this.scrolling;
            this.scrolling = false;
            return ret;
        }
    }

    private int getBarHeight() {
        int barHeight = this.height * this.height / this.getContentHeight();
        if (barHeight < 32) {
            barHeight = 32;
        }
        if (barHeight > this.height - this.border * 2) {
            barHeight = this.height - this.border * 2;
        }
        return barHeight;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (this.scrolling) {
            int maxScroll = this.height - this.getBarHeight();
            double moved = deltaY / (double) maxScroll;
            this.scrollDistance = (float) ((double) this.scrollDistance + (double) this.getMaxScroll() * moved);
            this.applyScrollLimits();
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Tesselator tess = Tesselator.getInstance();
        BufferBuilder worldr = tess.getBuilder();
        double scale = this.client.getWindow().getGuiScale();
        RenderSystem.enableScissor((int) ((double) this.left * scale), (int) ((double) this.client.getWindow().getHeight() - (double) this.bottom * scale), (int) ((double) this.width * scale), (int) ((double) this.height * scale));
        this.drawBackground(guiGraphics, tess, partialTick);
        int baseY = this.top + this.border - (int) this.scrollDistance;
        this.drawPanel(guiGraphics, this.right, baseY, tess, mouseX, mouseY);
        RenderSystem.disableDepthTest();
        int extraHeight = this.getContentHeight() + this.border - this.height;
        if (extraHeight > 0) {
            int barHeight = this.getBarHeight();
            int barTop = (int) this.scrollDistance * (this.height - barHeight) / extraHeight + this.top;
            if (barTop < this.top) {
                barTop = this.top;
            }
            int barBgAlpha = this.barBgColor >> 24 & 0xFF;
            int barBgRed = this.barBgColor >> 16 & 0xFF;
            int barBgGreen = this.barBgColor >> 8 & 0xFF;
            int barBgBlue = this.barBgColor & 0xFF;
            RenderSystem.setShader(GameRenderer::m_172811_);
            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            worldr.m_5483_((double) this.barLeft, (double) this.bottom, 0.0).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth), (double) this.bottom, 0.0).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth), (double) this.top, 0.0).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).endVertex();
            worldr.m_5483_((double) this.barLeft, (double) this.top, 0.0).color(barBgRed, barBgGreen, barBgBlue, barBgAlpha).endVertex();
            tess.end();
            int barAlpha = this.barColor >> 24 & 0xFF;
            int barRed = this.barColor >> 16 & 0xFF;
            int barGreen = this.barColor >> 8 & 0xFF;
            int barBlue = this.barColor & 0xFF;
            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            worldr.m_5483_((double) this.barLeft, (double) (barTop + barHeight), 0.0).color(barRed, barGreen, barBlue, barAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth), (double) (barTop + barHeight), 0.0).color(barRed, barGreen, barBlue, barAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth), (double) barTop, 0.0).color(barRed, barGreen, barBlue, barAlpha).endVertex();
            worldr.m_5483_((double) this.barLeft, (double) barTop, 0.0).color(barRed, barGreen, barBlue, barAlpha).endVertex();
            tess.end();
            int barBorderAlpha = this.barBorderColor >> 24 & 0xFF;
            int barBorderRed = this.barBorderColor >> 16 & 0xFF;
            int barBorderGreen = this.barBorderColor >> 8 & 0xFF;
            int barBorderBlue = this.barBorderColor & 0xFF;
            worldr.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
            worldr.m_5483_((double) this.barLeft, (double) (barTop + barHeight - 1), 0.0).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth - 1), (double) (barTop + barHeight - 1), 0.0).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).endVertex();
            worldr.m_5483_((double) (this.barLeft + this.barWidth - 1), (double) barTop, 0.0).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).endVertex();
            worldr.m_5483_((double) this.barLeft, (double) barTop, 0.0).color(barBorderRed, barBorderGreen, barBorderBlue, barBorderAlpha).endVertex();
            tess.end();
        }
        RenderSystem.disableBlend();
        RenderSystem.disableScissor();
    }

    protected void drawGradientRect(GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color1, int color2) {
        guiGraphics.fillGradient(left, top, right, bottom, color1, color2);
    }

    @Override
    public List<? extends GuiEventListener> children() {
        return Collections.emptyList();
    }
}