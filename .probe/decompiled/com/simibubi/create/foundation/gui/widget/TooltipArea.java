package com.simibubi.create.foundation.gui.widget;

import java.util.List;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class TooltipArea extends AbstractSimiWidget {

    public TooltipArea(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
        }
    }

    public TooltipArea withTooltip(List<Component> tooltip) {
        this.toolTip = tooltip;
        return this;
    }
}