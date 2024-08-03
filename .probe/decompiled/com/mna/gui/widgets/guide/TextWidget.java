package com.mna.gui.widgets.guide;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FormattedCharSequence;

public class TextWidget extends AbstractWidget {

    private final FormattedCharSequence line;

    private final int overrideColor;

    private final float scale_factor;

    private final Consumer<List<Component>> tooltipFunction;

    private final List<Component> tooltip;

    public TextWidget(int pX, int pY, int pWidth, int pHeight, FormattedCharSequence line, int overrideColor, float scale_factor, List<Component> tooltip, Consumer<List<Component>> tooltipFunction, Consumer<TextWidget> onClick) {
        super(pX, pY, pWidth, pHeight, Component.literal(""));
        this.line = line;
        this.overrideColor = overrideColor;
        this.scale_factor = scale_factor;
        this.tooltip = tooltip;
        this.tooltipFunction = tooltipFunction;
    }

    @Override
    public void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        Minecraft mc = Minecraft.getInstance();
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(this.scale_factor, this.scale_factor, this.scale_factor);
        pGuiGraphics.drawString(mc.font, this.line, (int) ((float) this.m_252754_() / this.scale_factor), (int) ((float) this.m_252907_() / this.scale_factor), this.overrideColor == -1 ? 4210752 : this.overrideColor, false);
        pGuiGraphics.pose().popPose();
        if (this.f_93622_ && this.tooltipFunction != null) {
            this.tooltipFunction.accept(this.tooltip);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}