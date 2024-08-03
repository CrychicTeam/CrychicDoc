package com.github.einjerjar.mc.widgets2;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;

public abstract class EScreen2 extends EScreen2Utils {

    protected EScreen2(Screen parent) {
        this(parent, Component.translatable("SCREEN"));
        assert this.f_96541_ != null;
    }

    protected EScreen2(Screen parent, Component text) {
        super(text);
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.m_7856_();
        int tw = this.targetScreenWidth == -1 ? this.f_96543_ : this.targetScreenWidth;
        this.scr = this.scrFromWidth(Math.min(tw, this.f_96543_));
        this.children.clear();
        this.onInit();
        for (EWidget2 child : this.children) {
            child.init();
        }
    }

    protected abstract void onInit();

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    protected boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMouseReleased(double mouseX, double mouseY, int button) {
        switch(button) {
            case 0:
                return this.onLeftMouseReleased(mouseX, mouseY, button);
            case 1:
                return this.onRightMouseReleased(mouseX, mouseY, button);
            case 2:
                return this.onMiddleMouseReleased(mouseX, mouseY, button);
            default:
                return this.onOtherMouseReleased(mouseX, mouseY, button);
        }
    }

    protected boolean onLeftMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMiddleMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onRightMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onOtherMouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    protected void preRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.m_280273_(guiGraphics);
    }

    protected void onRender(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.hoverWidget(null);
        for (EWidget2 w : this.children) {
            if (w.rect.contains((double) mouseX, (double) mouseY)) {
                this.hoverWidget(w);
            }
            w.render(guiGraphics, mouseX, mouseY, partialTick);
        }
    }

    protected void postRender(GuiGraphics poseStack, int mouseX, int mouseY, float partialTick) {
    }

    @Override
    public void onClose() {
        assert this.f_96541_ != null;
        this.f_96541_.setScreen(this.parent);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (this.focusWidget() != null && this.focusWidget().keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        } else if (keyCode == 256) {
            boolean r = false;
            if (this.focusWidget() != null) {
                if (this.focusWidget().escape()) {
                    r = true;
                }
                this.focusWidget(null);
            }
            if (!r) {
                this.onClose();
            }
            return true;
        } else {
            return this.onKeyPressed(keyCode, scanCode, modifiers);
        }
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.focusWidget() != null && this.focusWidget().charTyped(codePoint, modifiers) ? true : this.onCharTyped(codePoint, modifiers);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        this.clickState = true;
        if (this.focusWidget() != null && this.hoverWidget() != this.focusWidget()) {
            this.focusWidget().mouseClicked(mouseX, mouseY, button);
            this.focusWidget(null);
        }
        if (this.hoverWidget() != null && this.hoverWidget().mouseClicked(mouseX, mouseY, button)) {
            this.activeWidget(this.hoverWidget());
            this.focusWidget(this.hoverWidget());
            return true;
        } else {
            return this.onMouseClicked(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.clickState) {
            return false;
        } else {
            this.clickState = false;
            EWidget2 active = this.activeWidget();
            boolean r = false;
            if (this.hoverWidget() != active && active != null) {
                active.mouseReleased(mouseX, mouseY, button);
                this.focusWidget(null);
            } else if (this.hoverWidget() != null && this.hoverWidget().mouseReleased(mouseX, mouseY, button)) {
                r = true;
            }
            this.activeWidget(null);
            return r || this.onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return !this.clickState ? false : this.onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.onMouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.preRender(guiGraphics, mouseX, mouseY, partialTick);
        this.onRender(guiGraphics, mouseX, mouseY, partialTick);
        this.postRender(guiGraphics, mouseX, mouseY, partialTick);
    }
}