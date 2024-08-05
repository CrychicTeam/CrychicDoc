package com.simibubi.create.foundation.ponder.ui;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.widget.AbstractSimiWidget;
import com.simibubi.create.foundation.ponder.PonderScene;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.sounds.SoundManager;

public class PonderProgressBar extends AbstractSimiWidget {

    LerpedFloat progress;

    PonderUI ponder;

    public PonderProgressBar(PonderUI ponder, int xIn, int yIn, int widthIn, int heightIn) {
        super(xIn, yIn, widthIn, heightIn);
        this.ponder = ponder;
        this.progress = LerpedFloat.linear().startWithValue(0.0);
    }

    @Override
    public void tick() {
        this.progress.chase((double) this.ponder.getActiveScene().getSceneProgress(), 0.5, LerpedFloat.Chaser.EXP);
        this.progress.tickChaser();
    }

    @Override
    protected boolean clicked(double mouseX, double mouseY) {
        return this.f_93623_ && this.f_93624_ && this.ponder.getActiveScene().getKeyframeCount() > 0 && mouseX >= (double) this.m_252754_() && mouseX < (double) (this.m_252754_() + this.f_93618_ + 4) && mouseY >= (double) this.m_252907_() - 3.0 && mouseY < (double) (this.m_252907_() + this.f_93619_ + 20);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        PonderScene activeScene = this.ponder.getActiveScene();
        int keyframeIndex = this.getHoveredKeyframeIndex(activeScene, mouseX);
        if (keyframeIndex == -1) {
            this.ponder.seekToTime(0);
        } else if (keyframeIndex == activeScene.getKeyframeCount()) {
            this.ponder.seekToTime(activeScene.getTotalTime());
        } else {
            this.ponder.seekToTime(activeScene.getKeyframeTime(keyframeIndex));
        }
    }

    public int getHoveredKeyframeIndex(PonderScene activeScene, double mouseX) {
        int totalTime = activeScene.getTotalTime();
        int clickedAtTime = (int) ((mouseX - (double) this.m_252754_()) / ((double) this.f_93618_ + 4.0) * (double) totalTime);
        int lastKeyframeTime = activeScene.getKeyframeTime(activeScene.getKeyframeCount() - 1);
        int diffToEnd = totalTime - clickedAtTime;
        int diffToLast = clickedAtTime - lastKeyframeTime;
        if (diffToEnd > 0 && diffToEnd < diffToLast / 2) {
            return activeScene.getKeyframeCount();
        } else {
            lastKeyframeTime = -1;
            for (int i = 0; i < activeScene.getKeyframeCount(); lastKeyframeTime = i++) {
                diffToLast = activeScene.getKeyframeTime(i);
                if (diffToLast > clickedAtTime) {
                    break;
                }
            }
            return lastKeyframeTime;
        }
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack ms = graphics.pose();
        this.f_93622_ = this.clicked((double) mouseX, (double) mouseY);
        new BoxElement().<BoxElement>withBackground(Theme.c(Theme.Key.PONDER_BACKGROUND_FLAT)).<BoxElement>gradientBorder(Theme.p(Theme.Key.PONDER_IDLE)).<RenderElement>at((float) this.m_252754_(), (float) this.m_252907_(), 400.0F).<RenderElement>withBounds(this.f_93618_, this.f_93619_).render(graphics);
        ms.pushPose();
        ms.translate((float) (this.m_252754_() - 2), (float) (this.m_252907_() - 2), 100.0F);
        ms.pushPose();
        ms.scale((float) (this.f_93618_ + 4) * this.progress.getValue(partialTicks), 1.0F, 1.0F);
        int c1 = Theme.i(Theme.Key.PONDER_PROGRESSBAR, true);
        int c2 = Theme.i(Theme.Key.PONDER_PROGRESSBAR, false);
        graphics.fillGradient(0, 3, 1, 4, 310, c1, c1);
        graphics.fillGradient(0, 4, 1, 5, 310, c2, c2);
        ms.popPose();
        this.renderKeyframes(graphics, mouseX, partialTicks);
        ms.popPose();
    }

    private void renderKeyframes(GuiGraphics graphics, int mouseX, float partialTicks) {
        PonderScene activeScene = this.ponder.getActiveScene();
        int hoverStartColor = Theme.i(Theme.Key.PONDER_HOVER, true) | -1610612736;
        int hoverEndColor = Theme.i(Theme.Key.PONDER_HOVER, false) | -1610612736;
        int idleStartColor = Theme.i(Theme.Key.PONDER_IDLE, true) | 1073741824;
        int idleEndColor = Theme.i(Theme.Key.PONDER_IDLE, false) | 1073741824;
        int hoverIndex;
        if (this.f_93622_) {
            hoverIndex = this.getHoveredKeyframeIndex(activeScene, (double) mouseX);
        } else {
            hoverIndex = -2;
        }
        if (hoverIndex == -1) {
            this.drawKeyframe(graphics, activeScene, true, 0, 0, hoverStartColor, hoverEndColor, 8);
        } else if (hoverIndex == activeScene.getKeyframeCount()) {
            this.drawKeyframe(graphics, activeScene, true, activeScene.getTotalTime(), this.f_93618_ + 4, hoverStartColor, hoverEndColor, 8);
        }
        for (int i = 0; i < activeScene.getKeyframeCount(); i++) {
            int keyframeTime = activeScene.getKeyframeTime(i);
            int keyframePos = (int) ((float) keyframeTime / (float) activeScene.getTotalTime() * (float) (this.f_93618_ + 4));
            boolean selected = i == hoverIndex;
            int startColor = selected ? hoverStartColor : idleStartColor;
            int endColor = selected ? hoverEndColor : idleEndColor;
            int height = selected ? 8 : 4;
            this.drawKeyframe(graphics, activeScene, selected, keyframeTime, keyframePos, startColor, endColor, height);
        }
    }

    private void drawKeyframe(GuiGraphics graphics, PonderScene activeScene, boolean selected, int keyframeTime, int keyframePos, int startColor, int endColor, int height) {
        PoseStack ms = graphics.pose();
        if (selected) {
            Font font = Minecraft.getInstance().font;
            graphics.fillGradient(keyframePos, 10, keyframePos + 1, 10 + height, 600, endColor, startColor);
            ms.pushPose();
            ms.translate(0.0F, 0.0F, 200.0F);
            String text;
            int offset;
            if (activeScene.getCurrentTime() < keyframeTime) {
                text = ">";
                offset = -1 - font.width(text);
            } else {
                text = "<";
                offset = 3;
            }
            graphics.drawString(font, text, keyframePos + offset, 10, endColor, false);
            ms.popPose();
        }
        graphics.fillGradient(keyframePos, -1, keyframePos + 1, 2 + height, 400, startColor, endColor);
    }

    @Override
    public void playDownSound(SoundManager handler) {
    }
}