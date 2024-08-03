package com.simibubi.create.foundation.gui;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.utility.Components;
import java.util.Collection;
import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractSimiScreen extends Screen {

    protected int windowWidth;

    protected int windowHeight;

    protected int windowXOffset;

    protected int windowYOffset;

    protected int guiLeft;

    protected int guiTop;

    protected AbstractSimiScreen(Component title) {
        super(title);
    }

    protected AbstractSimiScreen() {
        this(Components.immutableEmpty());
    }

    protected void setWindowSize(int width, int height) {
        this.windowWidth = width;
        this.windowHeight = height;
    }

    protected void setWindowOffset(int xOffset, int yOffset) {
        this.windowXOffset = xOffset;
        this.windowYOffset = yOffset;
    }

    @Override
    protected void init() {
        this.guiLeft = (this.f_96543_ - this.windowWidth) / 2;
        this.guiTop = (this.f_96544_ - this.windowHeight) / 2;
        this.guiLeft = this.guiLeft + this.windowXOffset;
        this.guiTop = this.guiTop + this.windowYOffset;
    }

    @Override
    public void tick() {
        for (GuiEventListener listener : this.m_6702_()) {
            if (listener instanceof TickableGuiEventListener tickable) {
                tickable.tick();
            }
        }
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.getFocused() != null && !this.getFocused().isMouseOver(pMouseX, pMouseY)) {
            this.m_7522_(null);
        }
        return super.m_6375_(pMouseX, pMouseY, pButton);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    protected <W extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidgets(W... widgets) {
        for (W widget : widgets) {
            this.m_142416_(widget);
        }
    }

    protected <W extends GuiEventListener & Renderable & NarratableEntry> void addRenderableWidgets(Collection<W> widgets) {
        for (W widget : widgets) {
            this.m_142416_(widget);
        }
    }

    protected void removeWidgets(GuiEventListener... widgets) {
        for (GuiEventListener widget : widgets) {
            this.m_169411_(widget);
        }
    }

    protected void removeWidgets(Collection<? extends GuiEventListener> widgets) {
        for (GuiEventListener widget : widgets) {
            this.m_169411_(widget);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.f_96541_.getFrameTime();
        PoseStack ms = graphics.pose();
        ms.pushPose();
        this.prepareFrame();
        this.renderWindowBackground(graphics, mouseX, mouseY, partialTicks);
        this.renderWindow(graphics, mouseX, mouseY, partialTicks);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderWindowForeground(graphics, mouseX, mouseY, partialTicks);
        this.endFrame();
        ms.popPose();
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        boolean keyPressed = super.keyPressed(keyCode, scanCode, modifiers);
        if (!keyPressed && !(this.getFocused() instanceof EditBox)) {
            InputConstants.Key mouseKey = InputConstants.getKey(keyCode, scanCode);
            if (this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey)) {
                this.m_7379_();
                return true;
            } else {
                return false;
            }
        } else {
            return keyPressed;
        }
    }

    protected void prepareFrame() {
    }

    protected void renderWindowBackground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280273_(graphics);
    }

    protected abstract void renderWindow(GuiGraphics var1, int var2, int var3, float var4);

    protected void renderWindowForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        for (Renderable widget : this.f_169369_) {
            if (widget instanceof AbstractSimiWidget) {
                AbstractSimiWidget simiWidget = (AbstractSimiWidget) widget;
                if (simiWidget.m_5953_((double) mouseX, (double) mouseY) && simiWidget.f_93624_) {
                    List<Component> tooltip = simiWidget.getToolTip();
                    if (!tooltip.isEmpty()) {
                        int ttx = simiWidget.lockedTooltipX == -1 ? mouseX : simiWidget.lockedTooltipX + simiWidget.m_252754_();
                        int tty = simiWidget.lockedTooltipY == -1 ? mouseY : simiWidget.lockedTooltipY + simiWidget.m_252907_();
                        graphics.renderComponentTooltip(this.f_96547_, tooltip, ttx, tty);
                    }
                }
            }
        }
    }

    protected void endFrame() {
    }

    @Deprecated
    protected void debugWindowArea(GuiGraphics graphics) {
        graphics.fill(this.guiLeft + this.windowWidth, this.guiTop + this.windowHeight, this.guiLeft, this.guiTop, -741092397);
    }

    @Override
    public GuiEventListener getFocused() {
        GuiEventListener focused = super.m_7222_();
        if (focused instanceof AbstractWidget && !((AbstractWidget) focused).isFocused()) {
            focused = null;
        }
        this.m_7522_(focused);
        return focused;
    }
}