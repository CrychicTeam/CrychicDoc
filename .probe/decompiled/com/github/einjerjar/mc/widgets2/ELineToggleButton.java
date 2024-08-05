package com.github.einjerjar.mc.widgets2;

import com.github.einjerjar.mc.widgets.utils.Rect;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class ELineToggleButton extends EToggleButton {

    protected Rect checkBox;

    public ELineToggleButton(Component text, int x, int y, int w, int h) {
        super(text, x, y, w, h);
    }

    @Override
    protected void init() {
        super.init();
        int boxS = this.rect.h() - this.padding.y() * 2;
        this.checkBox = new Rect(this.rect.right() - this.padding.x() - boxS, this.rect.top() + this.padding.y(), boxS, boxS);
    }

    @Override
    protected void preRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        U.bg(guiGraphics, this.rect, ColorOption.baseBG.fromState(this.state()));
    }

    @Override
    protected void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawString(this.font, this.text, this.padding.x() + this.rect.left(), this.center().y() - 9 / 2 + 1, this.tColor());
        U.outline(guiGraphics, this.checkBox, ColorOption.baseFG.fromState(this.state()));
        if (this.value()) {
            U.bg(guiGraphics, this.checkBox, ColorOption.baseFG.base());
        }
    }
}