package me.jellysquid.mods.sodium.client.gui.options.control;

import me.jellysquid.mods.sodium.client.gui.options.Option;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.navigation.CommonInputs;
import net.minecraft.client.renderer.Rect2i;

public class TickBoxControl implements Control<Boolean> {

    private final Option<Boolean> option;

    public TickBoxControl(Option<Boolean> option) {
        this.option = option;
    }

    @Override
    public ControlElement<Boolean> createElement(Dim2i dim) {
        return new TickBoxControl.TickBoxControlElement(this.option, dim);
    }

    @Override
    public int getMaxWidth() {
        return 30;
    }

    @Override
    public Option<Boolean> getOption() {
        return this.option;
    }

    private static class TickBoxControlElement extends ControlElement<Boolean> {

        private final Rect2i button;

        public TickBoxControlElement(Option<Boolean> option, Dim2i dim) {
            super(option, dim);
            this.button = new Rect2i(dim.getLimitX() - 16, dim.getCenterY() - 5, 10, 10);
        }

        @Override
        public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
            super.render(drawContext, mouseX, mouseY, delta);
            int x = this.button.getX();
            int y = this.button.getY();
            int w = x + this.button.getWidth();
            int h = y + this.button.getHeight();
            boolean enabled = this.option.isAvailable();
            boolean ticked = enabled && this.option.getValue();
            int color;
            if (enabled) {
                color = ticked ? -3179338 : -1;
            } else {
                color = -5592406;
            }
            if (ticked) {
                this.drawRect(drawContext, x + 2, y + 2, w - 2, h - 2, color);
            }
            this.drawBorder(drawContext, x, y, w, h, color);
        }

        @Override
        public boolean mouseClicked(double mouseX, double mouseY, int button) {
            if (this.option.isAvailable() && button == 0 && this.dim.containsCursor(mouseX, mouseY)) {
                this.toggleControl();
                this.playClickSound();
                return true;
            } else {
                return false;
            }
        }

        @Override
        public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
            if (!this.m_93696_()) {
                return false;
            } else if (CommonInputs.selected(keyCode)) {
                this.toggleControl();
                this.playClickSound();
                return true;
            } else {
                return false;
            }
        }

        public void toggleControl() {
            this.option.setValue(!this.option.getValue());
        }
    }
}