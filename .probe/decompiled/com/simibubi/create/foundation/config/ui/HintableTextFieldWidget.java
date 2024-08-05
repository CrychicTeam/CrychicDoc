package com.simibubi.create.foundation.config.ui;

import com.mojang.blaze3d.platform.InputConstants;
import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.utility.Components;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;

public class HintableTextFieldWidget extends EditBox {

    protected Font font;

    protected String hint;

    public HintableTextFieldWidget(Font font, int x, int y, int width, int height) {
        super(font, x, y, width, height, Components.immutableEmpty());
        this.font = font;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public void renderWidget(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.renderWidget(graphics, mouseX, mouseY, partialTicks);
        if (this.hint != null && !this.hint.isEmpty()) {
            if (this.m_94155_().isEmpty()) {
                graphics.drawString(this.font, this.hint, this.m_252754_() + 5, this.m_252907_() + (this.f_93619_ - 8) / 2, Theme.c(Theme.Key.TEXT).scaleAlpha(0.75F).getRGB(), false);
            }
        }
    }

    @Override
    public boolean mouseClicked(double x, double y, int button) {
        if (!this.m_5953_(x, y)) {
            return false;
        } else if (button == 1) {
            this.m_94144_("");
            return true;
        } else {
            return super.m_6375_(x, y, button);
        }
    }

    @Override
    public boolean keyPressed(int code, int p_keyPressed_2_, int p_keyPressed_3_) {
        InputConstants.Key mouseKey = InputConstants.getKey(code, p_keyPressed_2_);
        return Minecraft.getInstance().options.keyInventory.isActiveAndMatches(mouseKey) ? true : super.keyPressed(code, p_keyPressed_2_, p_keyPressed_3_);
    }
}