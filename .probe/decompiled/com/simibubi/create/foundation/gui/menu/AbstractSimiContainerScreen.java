package com.simibubi.create.foundation.gui.menu;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.TickableGuiEventListener;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.Rect2i;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
@ParametersAreNonnullByDefault
public abstract class AbstractSimiContainerScreen<T extends AbstractContainerMenu> extends AbstractContainerScreen<T> {

    protected int windowXOffset;

    protected int windowYOffset;

    public AbstractSimiContainerScreen(T container, Inventory inv, Component title) {
        super(container, inv, title);
    }

    protected void setWindowSize(int width, int height) {
        this.f_97726_ = width;
        this.f_97727_ = height;
    }

    protected void setWindowOffset(int xOffset, int yOffset) {
        this.windowXOffset = xOffset;
        this.windowYOffset = yOffset;
    }

    @Override
    protected void init() {
        super.init();
        this.f_97735_ = this.f_97735_ + this.windowXOffset;
        this.f_97736_ = this.f_97736_ + this.windowYOffset;
    }

    @Override
    protected void containerTick() {
        for (GuiEventListener listener : this.m_6702_()) {
            if (listener instanceof TickableGuiEventListener tickable) {
                tickable.tick();
            }
        }
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
        this.m_280273_(graphics);
        super.render(graphics, mouseX, mouseY, partialTicks);
        this.renderForeground(graphics, mouseX, mouseY, partialTicks);
    }

    @Override
    protected void renderLabels(GuiGraphics graphics, int mouseX, int mouseY) {
    }

    protected void renderForeground(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        this.m_280072_(graphics, mouseX, mouseY);
        for (Renderable widget : this.f_169369_) {
            if (widget instanceof AbstractSimiWidget) {
                AbstractSimiWidget simiWidget = (AbstractSimiWidget) widget;
                if (simiWidget.m_5953_((double) mouseX, (double) mouseY)) {
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

    public int getLeftOfCentered(int textureWidth) {
        return this.f_97735_ - this.windowXOffset + (this.f_97726_ - textureWidth) / 2;
    }

    public void renderPlayerInventory(GuiGraphics graphics, int x, int y) {
        AllGuiTextures.PLAYER_INVENTORY.render(graphics, x, y);
        graphics.drawString(this.f_96547_, this.f_169604_, x + 8, y + 6, 4210752, false);
    }

    @Override
    public boolean keyPressed(int pKeyCode, int pScanCode, int pModifiers) {
        InputConstants.Key mouseKey = InputConstants.getKey(pKeyCode, pScanCode);
        return this.getFocused() instanceof EditBox && this.f_96541_.options.keyInventory.isActiveAndMatches(mouseKey) ? false : super.keyPressed(pKeyCode, pScanCode, pModifiers);
    }

    @Override
    public boolean mouseClicked(double pMouseX, double pMouseY, int pButton) {
        if (this.getFocused() != null && !this.getFocused().isMouseOver(pMouseX, pMouseY)) {
            this.m_7522_(null);
        }
        return super.mouseClicked(pMouseX, pMouseY, pButton);
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

    public List<Rect2i> getExtraAreas() {
        return Collections.emptyList();
    }

    @Deprecated
    protected void debugWindowArea(GuiGraphics graphics) {
        graphics.fill(this.f_97735_ + this.f_97726_, this.f_97736_ + this.f_97727_, this.f_97735_, this.f_97736_, -741092397);
    }

    @Deprecated
    protected void debugExtraAreas(GuiGraphics graphics) {
        for (Rect2i area : this.getExtraAreas()) {
            graphics.fill(area.getX() + area.getWidth(), area.getY() + area.getHeight(), area.getX(), area.getY(), -741092397);
        }
    }
}