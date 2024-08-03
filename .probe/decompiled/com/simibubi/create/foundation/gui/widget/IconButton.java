package com.simibubi.create.foundation.gui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import com.simibubi.create.foundation.gui.AllGuiTextures;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;

public class IconButton extends AbstractSimiWidget {

    protected ScreenElement icon;

    public IconButton(int x, int y, ScreenElement icon) {
        this(x, y, 18, 18, icon);
    }

    public IconButton(int x, int y, int w, int h, ScreenElement icon) {
        super(x, y, w, h);
        this.icon = icon;
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            AllGuiTextures button = !this.f_93623_ ? AllGuiTextures.BUTTON_DOWN : (this.m_5953_((double) mouseX, (double) mouseY) ? AllGuiTextures.BUTTON_HOVER : AllGuiTextures.BUTTON);
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawBg(graphics, button);
            this.icon.render(graphics, this.m_252754_() + 1, this.m_252907_() + 1);
        }
    }

    protected void drawBg(GuiGraphics graphics, AllGuiTextures button) {
        graphics.blit(button.location, this.m_252754_(), this.m_252907_(), button.startX, button.startY, button.width, button.height);
    }

    public void setToolTip(Component text) {
        this.toolTip.clear();
        this.toolTip.add(text);
    }

    public void setIcon(ScreenElement icon) {
        this.icon = icon;
    }
}