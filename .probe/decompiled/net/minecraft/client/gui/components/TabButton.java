package net.minecraft.client.gui.components;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.tabs.Tab;
import net.minecraft.client.gui.components.tabs.TabManager;
import net.minecraft.client.gui.narration.NarratedElementType;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class TabButton extends AbstractWidget {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation("textures/gui/tab_button.png");

    private static final int TEXTURE_WIDTH = 130;

    private static final int TEXTURE_HEIGHT = 24;

    private static final int TEXTURE_BORDER = 2;

    private static final int TEXTURE_BORDER_BOTTOM = 0;

    private static final int SELECTED_OFFSET = 3;

    private static final int TEXT_MARGIN = 1;

    private static final int UNDERLINE_HEIGHT = 1;

    private static final int UNDERLINE_MARGIN_X = 4;

    private static final int UNDERLINE_MARGIN_BOTTOM = 2;

    private final TabManager tabManager;

    private final Tab tab;

    public TabButton(TabManager tabManager0, Tab tab1, int int2, int int3) {
        super(0, 0, int2, int3, tab1.getTabTitle());
        this.tabManager = tabManager0;
        this.tab = tab1;
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        guiGraphics0.blitNineSliced(TEXTURE_LOCATION, this.m_252754_(), this.m_252907_(), this.f_93618_, this.f_93619_, 2, 2, 2, 0, 130, 24, 0, this.getTextureY());
        Font $$4 = Minecraft.getInstance().font;
        int $$5 = this.f_93623_ ? -1 : -6250336;
        this.renderString(guiGraphics0, $$4, $$5);
        if (this.isSelected()) {
            this.renderFocusUnderline(guiGraphics0, $$4, $$5);
        }
    }

    public void renderString(GuiGraphics guiGraphics0, Font font1, int int2) {
        int $$3 = this.m_252754_() + 1;
        int $$4 = this.m_252907_() + (this.isSelected() ? 0 : 3);
        int $$5 = this.m_252754_() + this.m_5711_() - 1;
        int $$6 = this.m_252907_() + this.m_93694_();
        m_280138_(guiGraphics0, font1, this.m_6035_(), $$3, $$4, $$5, $$6, int2);
    }

    private void renderFocusUnderline(GuiGraphics guiGraphics0, Font font1, int int2) {
        int $$3 = Math.min(font1.width(this.m_6035_()), this.m_5711_() - 4);
        int $$4 = this.m_252754_() + (this.m_5711_() - $$3) / 2;
        int $$5 = this.m_252907_() + this.m_93694_() - 2;
        guiGraphics0.fill($$4, $$5, $$4 + $$3, $$5 + 1, int2);
    }

    protected int getTextureY() {
        int $$0 = 2;
        if (this.isSelected() && this.m_198029_()) {
            $$0 = 1;
        } else if (this.isSelected()) {
            $$0 = 0;
        } else if (this.m_198029_()) {
            $$0 = 3;
        }
        return $$0 * 24;
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput narrationElementOutput0) {
        narrationElementOutput0.add(NarratedElementType.TITLE, Component.translatable("gui.narrate.tab", this.tab.getTabTitle()));
    }

    @Override
    public void playDownSound(SoundManager soundManager0) {
    }

    public Tab tab() {
        return this.tab;
    }

    public boolean isSelected() {
        return this.tabManager.getCurrentTab() == this.tab;
    }
}