package net.minecraftforge.client.gui.widget;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;

public class UnicodeGlyphButton extends ExtendedButton {

    public String glyph;

    public float glyphScale;

    public UnicodeGlyphButton(int xPos, int yPos, int width, int height, Component displayString, String glyph, float glyphScale, Button.OnPress handler) {
        super(xPos, yPos, width, height, displayString, handler);
        this.glyph = glyph;
        this.glyphScale = glyphScale;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTick) {
        if (this.f_93624_) {
            Minecraft mc = Minecraft.getInstance();
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            int k = !this.f_93623_ ? 0 : (this.m_198029_() ? 2 : 1);
            guiGraphics.blitWithBorder(f_93617_, this.m_252754_(), this.m_252907_(), 0, 46 + k * 20, this.f_93618_, this.f_93619_, 200, 20, 2, 3, 2, 2);
            Component buttonText = this.m_5646_();
            int glyphWidth = (int) ((float) mc.font.width(this.glyph) * this.glyphScale);
            int strWidth = mc.font.width(buttonText);
            int ellipsisWidth = mc.font.width("...");
            int totalWidth = strWidth + glyphWidth;
            if (totalWidth > this.f_93618_ - 6 && totalWidth > ellipsisWidth) {
                buttonText = Component.literal(mc.font.substrByWidth(buttonText, this.f_93618_ - 6 - ellipsisWidth).getString().trim() + "...");
            }
            strWidth = mc.font.width(buttonText);
            totalWidth = glyphWidth + strWidth;
            guiGraphics.pose().pushPose();
            guiGraphics.pose().scale(this.glyphScale, this.glyphScale, 1.0F);
            guiGraphics.drawCenteredString(mc.font, Component.literal(this.glyph), (int) ((float) (this.m_252754_() + this.f_93618_ / 2 - strWidth / 2) / this.glyphScale - (float) glyphWidth / (2.0F * this.glyphScale) + 2.0F), (int) (((float) this.m_252907_() + (float) (this.f_93619_ - 8) / this.glyphScale / 2.0F - 1.0F) / this.glyphScale), this.getFGColor());
            guiGraphics.pose().popPose();
            guiGraphics.drawCenteredString(mc.font, buttonText, (int) ((float) (this.m_252754_() + this.f_93618_ / 2) + (float) glyphWidth / this.glyphScale), this.m_252907_() + (this.f_93619_ - 8) / 2, this.getFGColor());
        }
    }
}