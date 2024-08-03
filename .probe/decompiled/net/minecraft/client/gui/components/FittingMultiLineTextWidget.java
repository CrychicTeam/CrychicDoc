package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;

public class FittingMultiLineTextWidget extends AbstractScrollWidget {

    private final Font font;

    private final MultiLineTextWidget multilineWidget;

    public FittingMultiLineTextWidget(int int0, int int1, int int2, int int3, Component component4, Font font5) {
        super(int0, int1, int2, int3, component4);
        this.font = font5;
        this.multilineWidget = new MultiLineTextWidget(0, 0, component4, font5).setMaxWidth(this.m_5711_() - this.m_240012_());
    }

    public FittingMultiLineTextWidget setColor(int int0) {
        this.multilineWidget.setColor(int0);
        return this;
    }

    @Override
    public void setWidth(int int0) {
        super.m_93674_(int0);
        this.multilineWidget.setMaxWidth(this.m_5711_() - this.m_240012_());
    }

    @Override
    protected int getInnerHeight() {
        return this.multilineWidget.getHeight();
    }

    @Override
    protected double scrollRate() {
        return 9.0;
    }

    @Override
    protected void renderBackground(GuiGraphics guiGraphics0) {
        if (this.m_239656_()) {
            super.renderBackground(guiGraphics0);
        } else if (this.m_93696_()) {
            this.m_289749_(guiGraphics0, this.m_252754_() - this.m_239244_(), this.m_252907_() - this.m_239244_(), this.m_5711_() + this.m_240012_(), this.m_93694_() + this.m_240012_());
        }
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.f_93624_) {
            if (!this.m_239656_()) {
                this.renderBackground(guiGraphics0);
                guiGraphics0.pose().pushPose();
                guiGraphics0.pose().translate((float) this.m_252754_(), (float) this.m_252907_(), 0.0F);
                this.multilineWidget.m_88315_(guiGraphics0, int1, int2, float3);
                guiGraphics0.pose().popPose();
            } else {
                super.renderWidget(guiGraphics0, int1, int2, float3);
            }
        }
    }

    @Override
    protected void renderContents(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        guiGraphics0.pose().pushPose();
        guiGraphics0.pose().translate((float) (this.m_252754_() + this.m_239244_()), (float) (this.m_252907_() + this.m_239244_()), 0.0F);
        this.multilineWidget.m_88315_(guiGraphics0, int1, int2, float3);
        guiGraphics0.pose().popPose();
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.m_6035_());
    }
}