package me.shedaniel.clothconfig2.gui.widget;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class ColorDisplayWidget extends AbstractWidget {

    protected EditBox textFieldWidget;

    protected int color;

    protected int size;

    public ColorDisplayWidget(EditBox textFieldWidget, int x, int y, int size, int color) {
        super(x, y, size, size, Component.empty());
        this.textFieldWidget = textFieldWidget;
        this.color = color;
        this.size = size;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        graphics.fillGradient(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.size, this.m_252907_() + this.size, this.textFieldWidget.m_93696_() ? -1 : -6250336, this.textFieldWidget.m_93696_() ? -1 : -6250336);
        graphics.fillGradient(this.m_252754_() + 1, this.m_252907_() + 1, this.m_252754_() + this.size - 1, this.m_252907_() + this.size - 1, -1, -1);
        graphics.fillGradient(this.m_252754_() + 1, this.m_252907_() + 1, this.m_252754_() + this.size - 1, this.m_252907_() + this.size - 1, this.color, this.color);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
    }

    @Override
    public void updateWidgetNarration(NarrationElementOutput narrationElementOutput) {
    }

    public void setColor(int color) {
        this.color = color;
    }
}