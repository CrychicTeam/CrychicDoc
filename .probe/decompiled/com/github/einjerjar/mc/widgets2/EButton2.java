package com.github.einjerjar.mc.widgets2;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EButton2<T extends EButton2> extends EWidget2 {

    protected Component text;

    protected EAction<T> onClick = null;

    protected EAction<T> onRightClick = null;

    public EButton2(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text(text);
    }

    @Override
    protected boolean onLeftMouseReleased(double mouseX, double mouseY, int button) {
        if (this.onClick != null) {
            this.onClick.run((T) this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected boolean onRightMouseReleased(double mouseX, double mouseY, int button) {
        if (this.onRightClick != null) {
            this.onRightClick.run((T) this);
            return true;
        } else {
            return false;
        }
    }

    @Override
    protected void preRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        U.bbg(guiGraphics, this.rect, ColorOption.baseBG, ColorOption.baseFG, this.state());
    }

    @Override
    protected void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawCenteredString(this.font, this.text, this.center().x(), this.center().y() - 9 / 2 + 1, this.tColor());
    }

    public Component text() {
        return this.text;
    }

    public EButton2<T> text(Component text) {
        this.text = text;
        return this;
    }

    public EButton2<T> onClick(EAction<T> onClick) {
        this.onClick = onClick;
        return this;
    }

    public EButton2<T> onRightClick(EAction<T> onRightClick) {
        this.onRightClick = onRightClick;
        return this;
    }
}