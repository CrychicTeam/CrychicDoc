package com.github.einjerjar.mc.widgets;

import com.github.einjerjar.mc.widgets.utils.ColorSet;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EButton extends EWidget {

    Component text;

    EWidget.SimpleWidgetAction<EWidget> clickAction;

    public EButton(Component text, int x, int y, int w, int h) {
        super(x, y, w, h);
        this.text = text;
    }

    @Override
    public void setTooltip(Component tip) {
        super.setTooltip(tip);
    }

    @Override
    protected void renderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        ColorSet colors = this.colorVariant();
        this.drawBg(guiGraphics, colors.bg());
        this.drawOutline(guiGraphics, colors.border());
        guiGraphics.drawCenteredString(this.font, this.text, this.midX(), this.midY() - 9 / 2 + 1, colors.text());
    }

    @Override
    public boolean onMouseReleased(boolean inside, double mouseX, double mouseY, int button) {
        if (this.clickAction != null) {
            this.clickAction.run(this);
            return true;
        } else {
            return false;
        }
    }

    public Component text() {
        return this.text;
    }

    public EButton text(Component text) {
        this.text = text;
        return this;
    }

    public EButton clickAction(EWidget.SimpleWidgetAction<EWidget> clickAction) {
        this.clickAction = clickAction;
        return this;
    }
}