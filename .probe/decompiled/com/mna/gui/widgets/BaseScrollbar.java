package com.mna.gui.widgets;

import com.mna.gui.GuiTextures;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;

public abstract class BaseScrollbar extends AbstractWidget {

    static ResourceLocation SCROLLBAR_TEXTURE = new ResourceLocation("mna", "textures/gui/scrollbar.png");

    static int sizeY = 53;

    static int sizeX = 12;

    protected double value = 0.0;

    int totalHeight;

    protected BaseScrollbar(int xpos, int ypos, int scrollbarTotalHeight) {
        super(xpos, ypos, sizeX, scrollbarTotalHeight, Component.literal(""));
        this.totalHeight = scrollbarTotalHeight;
    }

    public int getScrollBarY() {
        int val = (int) ((double) this.m_252907_() + this.value * (double) this.totalHeight);
        return Mth.clamp(val - sizeY / 2, this.m_252907_(), this.m_252907_() + this.totalHeight - sizeY);
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        int xadd = sizeX + (this.m_198029_() ? 0 : 1) * sizeX;
        int yPos = this.getScrollBarY();
        pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, this.m_252754_(), this.m_252907_(), 0, 0, sizeX, this.totalHeight);
        pGuiGraphics.blit(GuiTextures.Widgets.WIDGETS, this.m_252754_(), yPos, xadd, 0, sizeX, 53);
    }

    @Override
    public void onClick(double x, double y) {
        this.setValueFromMouse(y);
    }

    private void setValueFromMouse(double y) {
        this.setValue((y - (double) this.m_252907_()) / (double) this.totalHeight);
    }

    private void setValue(double y) {
        double val = this.value;
        this.value = Mth.clamp(y, 0.0, 1.0);
        if (val != this.value) {
            this.applyValue();
        }
    }

    public void setValueFromElement(int element, int max) {
        this.value = (double) ((float) element / (float) max);
    }

    @Override
    protected void onDrag(double x, double y, double f3, double f4) {
        this.setValueFromMouse(y);
        super.onDrag(x, y, f3, f4);
    }

    @Override
    public void onRelease(double x, double y) {
    }

    protected abstract void applyValue();
}