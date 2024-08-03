package com.github.alexmodguy.alexscaves.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraft.util.StringDecomposer;

public class SpelunkeryTableWordButton extends AbstractWidget {

    public static final ResourceLocation TEXTURE = new ResourceLocation("alexscaves", "textures/gui/spelunkery_table.png");

    private SpelunkeryTableScreen parent;

    private Font font;

    private Component glyphText;

    private Component normalText;

    public SpelunkeryTableWordButton(SpelunkeryTableScreen parent, Font font, int x, int y, int height, int width, Component text) {
        super(x, y, height, width, text);
        this.parent = parent;
        this.font = font;
        this.normalText = text.plainCopy().withStyle(Style.EMPTY);
        this.glyphText = text.copy().withStyle(SpelunkeryTableScreen.GLYPH_FONT);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.parent.hasTablet()) {
            float revealWordsAmount = this.parent.getRevealWordsAmount(Minecraft.getInstance().getFrameTime());
            int textColor = this.f_93623_ ? 4210752 : 12566463;
            int revealTextColor = this.parent.isTargetWord(this) ? this.parent.getHighlightColor() : 12566463;
            int alpha = (int) ((1.0F - revealWordsAmount) * 255.0F);
            int r = textColor >> 16 & 0xFF;
            int g = textColor >> 8 & 0xFF;
            int b = textColor & 0xFF;
            int revealAlpha = (int) (revealWordsAmount * 255.0F);
            int revealR = revealTextColor >> 16 & 0xFF;
            int revealG = revealTextColor >> 8 & 0xFF;
            int revealB = revealTextColor & 0xFF;
            if (alpha >= 1) {
                this.drawEquidistantWord(this.font, guiGraphics, this.glyphText, this.getX(), this.getY(), FastColor.ARGB32.color(alpha, r, g, b));
            }
            if (revealAlpha >= 1) {
                this.drawEquidistantWord(this.font, guiGraphics, this.normalText, this.getX(), this.getY(), FastColor.ARGB32.color(revealAlpha, revealR, revealG, revealB));
            }
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput output) {
    }

    @Override
    public int getX() {
        return super.getX() + this.parent.getGuiLeft();
    }

    @Override
    public void setX(int x) {
        super.setX(x - this.parent.getGuiLeft());
    }

    @Override
    public int getY() {
        return super.getY() + this.parent.getGuiTop();
    }

    @Override
    public void setY(int y) {
        super.setY(y - this.parent.getGuiTop());
    }

    @Override
    public void onClick(double x, double y) {
        if (this.parent.hasPaper()) {
            this.parent.onClickWord(this);
            this.f_93623_ = false;
        }
    }

    @Override
    public void playDownSound(SoundManager soundManager) {
    }

    public Component getNormalText() {
        return this.normalText;
    }

    public void renderTranslationText(int tickCount, int textColor, GuiGraphics guiGraphics, Font font, float magnifyingXMin, float magnifyingXMax, float magnifyingYMin, float magnifyingYMax) {
        if (!this.f_93623_) {
            guiGraphics.pose().pushPose();
            guiGraphics.enableScissor((int) magnifyingXMin, (int) magnifyingYMin, (int) magnifyingXMax, (int) magnifyingYMax);
            float age = (float) (Math.sin((double) (((float) tickCount + Minecraft.getInstance().getFrameTime()) * 0.2F)) + 1.0) * 0.5F;
            int alpha = (int) (Mth.clamp(age, 0.1F, 1.0F) * 255.0F);
            int r = textColor >> 16 & 0xFF;
            int g = textColor >> 8 & 0xFF;
            int b = textColor & 0xFF;
            this.drawEquidistantWord(font, guiGraphics, this.normalText, this.getX(), this.getY(), FastColor.ARGB32.color(alpha, r, g, b));
            guiGraphics.disableScissor();
            guiGraphics.pose().popPose();
        }
    }

    private void drawEquidistantWord(Font font, GuiGraphics guiGraphics, Component component, int x, int y, int color) {
        int letterWidth = 6;
        StringDecomposer.iterateFormatted(component, Style.EMPTY, (position, style, j) -> {
            guiGraphics.drawString(font, Component.literal(String.valueOf((char) j)).withStyle(style), x + letterWidth * position, y, color, false);
            return true;
        });
    }
}