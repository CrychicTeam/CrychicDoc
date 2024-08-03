package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.Rect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ELabel extends EWidget {

    Component text;

    boolean centerX;

    boolean centerY;

    public ELabel(int x, int y, int w, int h) {
        super(x, y, w, h);
    }

    public ELabel(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text = text;
    }

    public ELabel(Rect rect) {
        super(rect);
    }

    public ELabel(Component text, Rect rect) {
        super(rect);
        this.text = text;
    }

    public void center(boolean x, boolean y) {
        this.centerX = x;
        this.centerY = y;
    }

    public void center(boolean xy) {
        this.centerX = xy;
        this.centerY = xy;
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int x = this.centerX ? this.midX() : this.left();
        int y = this.centerY ? this.midY() - 9 / 2 : this.top();
        guiGraphics.drawCenteredString(this.font, this.text, x, y, this.colorVariant().text());
    }

    public Component text() {
        return this.text;
    }

    public boolean centerX() {
        return this.centerX;
    }

    public boolean centerY() {
        return this.centerY;
    }

    public ELabel text(Component text) {
        this.text = text;
        return this;
    }

    public ELabel centerX(boolean centerX) {
        this.centerX = centerX;
        return this;
    }

    public ELabel centerY(boolean centerY) {
        this.centerY = centerY;
        return this;
    }
}