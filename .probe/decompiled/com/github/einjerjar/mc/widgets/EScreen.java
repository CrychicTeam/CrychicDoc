package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.keymap.config.KeymapConfig;
import com.github.einjerjar.mc.widgets.utils.ColorGroups;
import com.github.einjerjar.mc.widgets.utils.Point;
import com.github.einjerjar.mc.widgets.utils.Rect;
import java.util.List;
import java.util.Optional;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public abstract class EScreen extends Screen {

    protected final Point<Integer> margin = new Point<>(6);

    protected final Point<Integer> padding = new Point<>(4);

    protected boolean autoRenderChild = true;

    protected boolean clickState = false;

    protected boolean renderBg = true;

    protected EWidget hoveredWidget = null;

    protected ELabel debugFocus;

    protected ELabel debugHover;

    protected Rect scr;

    Screen parent;

    protected EScreen(Screen parent, Component text) {
        super(text);
        this.parent = parent;
    }

    @Override
    protected void init() {
        this.debugFocus = new ELabel(Component.literal("focused"), 0, 4, this.f_96543_, 9);
        this.debugHover = new ELabel(Component.literal("hovered"), 0, 14, this.f_96543_, 9);
        this.debugFocus.color(ColorGroups.WHITE);
        this.debugHover.color(ColorGroups.WHITE);
        this.debugFocus.center(true);
        this.debugHover.center(true);
        this.onInit();
    }

    protected Rect scrFromWidth(int w) {
        return new Rect(Math.max((this.f_96543_ - w) / 2, 0) + this.margin.x(), this.margin.y(), w - this.margin.x() * 2, this.f_96544_ - this.margin.y() * 2);
    }

    protected abstract void onInit();

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        EWidget focus = (EWidget) this.m_7222_();
        if (focus != null && focus.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (focus != null && keyCode == 256) {
            focus.focused(false);
            this.m_7522_(null);
            return true;
        } else if (keyCode == 256) {
            this.onClose();
            return true;
        } else {
            return false;
        }
    }

    protected boolean onCharTyped(char chr, int modifiers) {
        EWidget focus = (EWidget) this.m_7222_();
        return focus != null ? focus.charTyped(chr, modifiers) : false;
    }

    @Override
    public boolean charTyped(char chr, int modifiers) {
        return this.onCharTyped(chr, modifiers);
    }

    @Override
    public void onClose() {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(this.parent);
    }

    public boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clickState = true;
        EWidget focus = (EWidget) this.m_7222_();
        this.m_7522_(null);
        if (focus != null && this.hoveredWidget != focus) {
            focus.focused(false);
            focus.mouseClicked(mouseX, mouseY, button);
        }
        boolean ret = false;
        if (this.hoveredWidget != null) {
            this.hoveredWidget.focused(true);
            ret = this.hoveredWidget.mouseClicked(mouseX, mouseY, button);
            if (ret) {
                this.m_7522_(this.hoveredWidget);
            }
        }
        this.m_7897_(true);
        return this.onMouseClicked(mouseX, mouseY, button) || ret;
    }

    public boolean onMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.m_7897_(false);
        if (!this.clickState) {
            return false;
        } else {
            this.clickState = false;
            EWidget focus = (EWidget) this.m_7222_();
            boolean ret = false;
            if (this.hoveredWidget != focus && focus != null) {
                focus.mouseReleased(mouseX, mouseY, button);
            }
            if (this.hoveredWidget != null) {
                ret = this.hoveredWidget.mouseReleased(mouseX, mouseY, button);
            }
            return this.onMouseReleased(mouseX, mouseY, button) || ret;
        }
    }

    public boolean onMouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        EWidget focus = (EWidget) this.m_7222_();
        return focus == null ? false : focus.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.onMouseDragged(mouseX, mouseY, button, deltaX, deltaY);
    }

    public boolean onMouseScrolled(double mouseX, double mouseY, double amount) {
        return this.hoveredWidget == null ? false : this.hoveredWidget.mouseScrolled(mouseX, mouseY, amount);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        return this.onMouseScrolled(mouseX, mouseY, amount);
    }

    public List<EWidget> widgets() {
        return this.m_6702_().stream().filter(EWidget.class::isInstance).map(EWidget.class::cast).toList();
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.renderBg) {
            this.m_280273_(guiGraphics);
        }
        this.preRenderScreen(guiGraphics, mouseX, mouseY, partialTick);
        this.hoveredWidget = null;
        if (this.autoRenderChild) {
            for (EWidget d : this.widgets()) {
                d.render(guiGraphics, mouseX, mouseY, partialTick);
                if (d.rect().contains((double) mouseX, (double) mouseY)) {
                    this.hoveredWidget = d;
                }
            }
        }
        this.postRenderScreen(guiGraphics, mouseX, mouseY, partialTick);
        if (KeymapConfig.instance().debug2()) {
            guiGraphics.fill(0, 0, this.f_96543_, 30, 1711276032);
            this.debugHover.text(Component.literal(this.hoveredWidget != null ? this.hoveredWidget.getClass().getName() : "none"));
            this.debugFocus.text(Component.literal(this.m_7222_() != null ? this.m_7222_().getClass().getName() : "none"));
            this.debugHover.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
            this.debugFocus.m_88315_(guiGraphics, mouseX, mouseY, partialTick);
        }
        this.postRenderDebugScreen(guiGraphics, mouseX, mouseY, partialTick);
        if (this.hoveredWidget != null && this.hoveredWidget.getTooltips() != null) {
            guiGraphics.renderTooltip(this.f_96547_, this.hoveredWidget.getTooltips(), Optional.empty(), mouseX, mouseY);
        }
    }

    protected void preRenderScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    protected void postRenderScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    protected void postRenderDebugScreen(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    public int left() {
        return 0;
    }

    public int top() {
        return 0;
    }

    public int right() {
        return this.f_96543_;
    }

    public int bottom() {
        return this.f_96544_;
    }

    public void drawOutline(GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.hLine(left, right, top, color);
        guiGraphics.hLine(left, right, bottom, color);
        guiGraphics.vLine(left, top, bottom, color);
        guiGraphics.vLine(right, top, bottom, color);
    }

    public void drawOutline(GuiGraphics guiGraphics, int color) {
        this.drawOutline(guiGraphics, this.left(), this.top(), this.right(), this.bottom(), color);
    }

    public void drawOutline(GuiGraphics guiGraphics, Rect r, int color) {
        this.drawOutline(guiGraphics, r.left(), r.top(), r.right(), r.bottom(), color);
    }

    public void drawBg(GuiGraphics guiGraphics, int left, int top, int right, int bottom, int color) {
        guiGraphics.fill(left, top, right, bottom, color);
    }

    public void drawBg(GuiGraphics guiGraphics, int color) {
        this.drawBg(guiGraphics, this.left(), this.top(), this.right(), this.bottom(), color);
    }

    public Screen parent() {
        return this.parent;
    }
}