package com.mna.gui.widgets;

import com.mna.gui.GuiTextures;
import com.mna.tools.math.MathUtils;
import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Consumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.util.FastColor;

public class RGBPicker extends AbstractWidget {

    private static final int SLIDER_START_X = 48;

    private static final int SLIDER_END_X = 90;

    private static final int INDEX_R = 0;

    private static final int INDEX_G = 1;

    private static final int INDEX_B = 2;

    private static final int INDEX_A = 3;

    private int r = 255;

    private int g = 255;

    private int b = 255;

    private int a = 255;

    private Consumer<Integer[]> onChange;

    private int dragIndex = -1;

    public RGBPicker(int x, int y, Consumer<Integer[]> onChange) {
        super(x, y, 95, 60, Component.literal(""));
        this.onChange = onChange;
    }

    private void renderNubbin(GuiGraphics pGuiGraphics, int index) {
        if (index >= 0 && index <= 3) {
            int value = this.getValue(index);
            int yOffset = 6 + 13 * index;
            int xOffset = (int) (42.0F * ((float) value / 255.0F)) - 4;
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, this.m_252754_() + 48 + xOffset, this.m_252907_() + yOffset, 10, 88, 4, 8);
        }
    }

    private void renderNudgeButtons(GuiGraphics pGuiGraphics, int index) {
        if (index >= 0 && index <= 3) {
            int yOffset = 6 + 13 * index;
            RenderSystem.enableBlend();
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, this.m_252754_() + 15, this.m_252907_() + yOffset, 0, 88, 5, 8);
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, this.m_252754_() + 38, this.m_252907_() + yOffset, 5, 88, 5, 8);
        }
    }

    private void renderValues(GuiGraphics pGuiGraphics) {
        Minecraft mc = Minecraft.getInstance();
        float scale = mc.options.forceUnicodeFont.get() ? 1.0F : 0.7F;
        Font font = mc.font;
        float textX = ((float) this.m_252754_() + 29.5F) / scale;
        float textY = ((float) this.m_252907_() + (mc.options.forceUnicodeFont.get() ? 6.0F : 7.5F)) / scale;
        pGuiGraphics.pose().pushPose();
        pGuiGraphics.pose().scale(scale, scale, scale);
        for (int i = 0; i < 4; i++) {
            int valueNumeric = this.getValue(i);
            String value = valueNumeric == -1 ? "â®Œ" : String.format("%d", valueNumeric);
            int len = font.width(value);
            pGuiGraphics.drawString(font, value, (int) (textX - (float) (len / 2)), (int) textY, FastColor.ARGB32.color(255, 49, 49, 49), false);
            textY += 13.0F / scale;
        }
        pGuiGraphics.pose().popPose();
    }

    private void broadcastChanges() {
        if (this.onChange != null) {
            this.onChange.accept(new Integer[] { this.r, this.g, this.b, this.a });
        }
    }

    private int getValue(int index) {
        switch(index) {
            case 0:
                return this.r;
            case 1:
                return this.g;
            case 2:
                return this.b;
            case 3:
                return this.a;
            default:
                return 0;
        }
    }

    public void setValue(int r, int g, int b, int a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    private void setValue(int index, int value) {
        switch(index) {
            case 0:
                this.r = MathUtils.clamp(value, -1, 255);
                this.broadcastChanges();
                break;
            case 1:
                this.g = MathUtils.clamp(value, -1, 255);
                this.broadcastChanges();
                break;
            case 2:
                this.b = MathUtils.clamp(value, -1, 255);
                this.broadcastChanges();
                break;
            case 3:
                this.a = MathUtils.clamp(value, -1, 255);
                this.broadcastChanges();
        }
    }

    private boolean handleNudgeButtonClick(double mouseX, double mouseY) {
        int index = -1;
        if (mouseY >= (double) (this.m_252907_() + 6) && mouseY <= (double) (this.m_252907_() + 14)) {
            index = 0;
        } else if (mouseY >= (double) (this.m_252907_() + 19) && mouseY <= (double) (this.m_252907_() + 27)) {
            index = 1;
        } else if (mouseY >= (double) (this.m_252907_() + 32) && mouseY <= (double) (this.m_252907_() + 40)) {
            index = 2;
        } else if (mouseY >= (double) (this.m_252907_() + 45) && mouseY <= (double) (this.m_252907_() + 53)) {
            index = 3;
        }
        if (index != -1) {
            if (mouseX >= (double) (this.m_252754_() + 15) && mouseX <= (double) (this.m_252754_() + 20)) {
                this.setValue(index, this.getValue(index) - 1);
                return true;
            }
            if (mouseX >= (double) (this.m_252754_() + 38) && mouseX <= (double) (this.m_252754_() + 43)) {
                this.setValue(index, this.getValue(index) + 1);
                return true;
            }
        }
        return false;
    }

    private boolean handleResetClick(double mouseX, double mouseY) {
        int index = -1;
        if (mouseY >= (double) (this.m_252907_() + 6) && mouseY <= (double) (this.m_252907_() + 14)) {
            index = 0;
        } else if (mouseY >= (double) (this.m_252907_() + 19) && mouseY <= (double) (this.m_252907_() + 27)) {
            index = 1;
        } else if (mouseY >= (double) (this.m_252907_() + 32) && mouseY <= (double) (this.m_252907_() + 40)) {
            index = 2;
        } else if (mouseY >= (double) (this.m_252907_() + 45) && mouseY <= (double) (this.m_252907_() + 53)) {
            index = 3;
        }
        if (index != -1 && mouseX >= (double) (this.m_252754_() + 6) && mouseX <= (double) (this.m_252754_() + 14)) {
            this.setValue(index, -1);
            return true;
        } else {
            return false;
        }
    }

    private int getClickedSlider(double mouseX, double mouseY) {
        if (mouseX >= (double) (this.m_252754_() + 48 - 4) && mouseX <= (double) (this.m_252754_() + 90 + 4)) {
            if (mouseY >= (double) (this.m_252907_() + 6) && mouseY <= (double) (this.m_252907_() + 14)) {
                return 0;
            }
            if (mouseY >= (double) (this.m_252907_() + 19) && mouseY <= (double) (this.m_252907_() + 27)) {
                return 1;
            }
            if (mouseY >= (double) (this.m_252907_() + 32) && mouseY <= (double) (this.m_252907_() + 40)) {
                return 2;
            }
            if (mouseY >= (double) (this.m_252907_() + 45) && mouseY <= (double) (this.m_252907_() + 53)) {
                return 3;
            }
        }
        return -1;
    }

    private void updateDraggedPosition(double mouseX, double mouseY) {
        double relativeMouseX = mouseX - (double) this.m_252754_() - 48.0;
        double delta = 42.0;
        double pct = MathUtils.clamp01(relativeMouseX / delta);
        int value = (int) (256.0 * pct) - 1;
        this.setValue(this.dragIndex, value);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (!this.m_7972_(button)) {
            return false;
        } else if (this.handleNudgeButtonClick(mouseX, mouseY)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            return true;
        } else if (this.handleResetClick(mouseX, mouseY)) {
            this.m_7435_(Minecraft.getInstance().getSoundManager());
            return true;
        } else {
            int clickedSlider = this.getClickedSlider(mouseX, mouseY);
            if (clickedSlider == -1) {
                return false;
            } else {
                this.m_7435_(Minecraft.getInstance().getSoundManager());
                this.dragIndex = clickedSlider;
                return true;
            }
        }
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.f_93623_ || !this.f_93624_) {
            return false;
        } else if (!this.m_7972_(button)) {
            this.dragIndex = -1;
            return false;
        } else if (this.dragIndex == -1) {
            return false;
        } else {
            this.updateDraggedPosition(mouseX, mouseY);
            return true;
        }
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        this.dragIndex = -1;
        return super.mouseReleased(mouseX, mouseY, button);
    }

    @Override
    protected void renderWidget(GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        if (this.f_93623_ && this.f_93624_) {
            pGuiGraphics.blit(GuiTextures.Items.BOOK_OF_ROTE_EXTRAS, this.m_252754_(), this.m_252907_(), 161, 0, this.f_93618_, this.f_93619_);
            for (int i = 0; i < 4; i++) {
                this.renderNubbin(pGuiGraphics, i);
                this.renderNudgeButtons(pGuiGraphics, i);
            }
            this.renderValues(pGuiGraphics);
        }
    }

    @Override
    protected void updateWidgetNarration(NarrationElementOutput pNarrationElementOutput) {
    }
}