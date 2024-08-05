package com.github.einjerjar.mc.widgets2;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

public class EToggleButton extends EButton2<EToggleButton> implements EValueContainer<Boolean> {

    protected boolean value = false;

    EAction<EToggleButton> onToggle = null;

    public EToggleButton(Component text, int x, int y, int w, int h) {
        super(text, x, y, w, h);
        this.onClick(self -> {
            self.value(!self.value());
            if (this.onToggle != null) {
                this.onToggle.run(this);
            }
        });
    }

    public Boolean value() {
        return this.value;
    }

    public void value(Boolean v) {
        this.value = v;
    }

    @Override
    protected void onRenderWidget(@NotNull GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        guiGraphics.drawCenteredString(this.font, Component.literal("").append(this.text).append(": ").append(Boolean.TRUE.equals(this.value()) ? "ON" : "OFF"), this.center().x(), this.center().y() - 9 / 2 + 1, this.tColor());
    }

    public EAction<EToggleButton> onToggle() {
        return this.onToggle;
    }

    public EToggleButton onToggle(EAction<EToggleButton> onToggle) {
        this.onToggle = onToggle;
        return this;
    }
}