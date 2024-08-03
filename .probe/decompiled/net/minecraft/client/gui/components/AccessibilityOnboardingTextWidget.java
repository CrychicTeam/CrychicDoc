package net.minecraft.client.gui.components;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;

public class AccessibilityOnboardingTextWidget extends MultiLineTextWidget {

    private static final int BORDER_COLOR_FOCUSED = -1;

    private static final int BORDER_COLOR = -6250336;

    private static final int BACKGROUND_COLOR = 1426063360;

    private static final int PADDING = 3;

    private static final int BORDER = 1;

    public AccessibilityOnboardingTextWidget(Font font0, Component component1, int int2) {
        super(component1, font0);
        this.m_269098_(int2);
        this.m_269484_(true);
        this.f_93623_ = true;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, this.m_6035_());
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        int $$4 = this.m_252754_() - 3;
        int $$5 = this.m_252907_() - 3;
        int $$6 = this.m_252754_() + this.m_5711_() + 3;
        int $$7 = this.m_252907_() + this.m_93694_() + 3;
        int $$8 = this.m_93696_() ? -1 : -6250336;
        guiGraphics0.fill($$4 - 1, $$5 - 1, $$4, $$7 + 1, $$8);
        guiGraphics0.fill($$6, $$5 - 1, $$6 + 1, $$7 + 1, $$8);
        guiGraphics0.fill($$4, $$5, $$6, $$5 - 1, $$8);
        guiGraphics0.fill($$4, $$7, $$6, $$7 + 1, $$8);
        guiGraphics0.fill($$4, $$5, $$6, $$7, 1426063360);
        super.renderWidget(guiGraphics0, int1, int2, float3);
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
    }
}