package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Point;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ELabel2 extends EWidget2 {

    protected Component text;

    protected Point<Boolean> centered = new Point<>(true);

    public ELabel2(@NotNull Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text(text);
        this.focusable(false);
    }

    @Override
    protected void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        int y = (this.centered.y() ? this.center().y() - 9 / 2 : this.rect().top()) + 1;
        int lColor = this.tColor();
        if (this.centered.x()) {
            guiGraphics.drawCenteredString(this.font, this.text, this.center().x(), y, lColor);
        } else {
            guiGraphics.drawString(this.font, this.text, this.rect().left(), y, lColor);
        }
    }

    public Component text() {
        return this.text;
    }

    public Point<Boolean> centered() {
        return this.centered;
    }

    public ELabel2 text(Component text) {
        this.text = text;
        return this;
    }

    public ELabel2 centered(Point<Boolean> centered) {
        this.centered = centered;
        return this;
    }
}