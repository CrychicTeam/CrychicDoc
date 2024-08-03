package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.keymap.Keymap;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.sounds.SoundEvents;
import org.jetbrains.annotations.NotNull;

public abstract class EWidget2 extends EWidget2Utils {

    protected EWidget2(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    protected void init() {
    }

    protected void updateTooltips() {
    }

    protected boolean onMouseClicked(double mouseX, double mouseY, int button) {
        return true;
    }

    private boolean handleMouseClick(double mouseX, double mouseY, int button) {
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

    protected boolean onMouseReleased(double mouseX, double mouseY, int button) {
        if (this.handleMouseClick(mouseX, mouseY, button)) {
            this.playSound(SoundEvents.UI_BUTTON_CLICK.value());
            return true;
        } else {
            return false;
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

    protected boolean onKeyPressed(int keyCode, int scanCode, int modifiers) {
        return false;
    }

    protected boolean onCharTyped(char codePoint, int modifiers) {
        return false;
    }

    protected boolean onMouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return false;
    }

    protected boolean onMouseScrolled(double mouseX, double mouseY, double delta) {
        return false;
    }

    protected void preRenderWidget(@NotNull GuiGraphics poseStack, int mouseX, int mouseY, float partialTick) {
    }

    protected abstract void onRenderWidget(@NotNull GuiGraphics var1, int var2, int var3, float var4);

    protected void postRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
    }

    private boolean onEscape() {
        return false;
    }

    public boolean escape() {
        return this.onEscape();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return !this.rect.contains(mouseX, mouseY) ? false : this.onMouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (!this.active()) {
            return false;
        } else if (!this.rect.contains(mouseX, mouseY)) {
            Keymap.logger().warn("1111");
            return false;
        } else {
            return this.onMouseReleased(mouseX, mouseY, button);
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double dragX, double dragY) {
        return this.onMouseDragged(mouseX, mouseY, button, dragX, dragY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return this.onMouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return this.onKeyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char codePoint, int modifiers) {
        return this.onCharTyped(codePoint, modifiers);
    }

    @Override
    public void render(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        this.preRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
        this.onRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
        this.postRenderWidget(guiGraphics, mouseX, mouseY, partialTick);
    }
}