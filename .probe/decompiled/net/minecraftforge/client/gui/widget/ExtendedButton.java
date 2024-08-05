package net.minecraftforge.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;

public class ExtendedButton extends Button {

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, Button.OnPress handler) {
        this(xPos, yPos, width, height, displayString, handler, f_252438_);
    }

    public ExtendedButton(int xPos, int yPos, int width, int height, Component displayString, Button.OnPress handler, Button.CreateNarration createNarration) {
        super(xPos, yPos, width, height, displayString, handler, createNarration);
    }

    public ExtendedButton(Button.Builder builder) {
        super(builder);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        Minecraft mc = Minecraft.getInstance();
        int k = !this.f_93623_ ? 0 : (this.m_198029_() ? 2 : 1);
        guiGraphics.blitWithBorder(f_93617_, this.m_252754_(), this.m_252907_(), 0, 46 + k * 20, this.f_93618_, this.f_93619_, 200, 20, 2, 3, 2, 2);
        FormattedText buttonText = mc.font.ellipsize(this.m_6035_(), this.f_93618_ - 6);
        guiGraphics.drawCenteredString(mc.font, Language.getInstance().getVisualOrder(buttonText), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, this.getFGColor());
    }
}