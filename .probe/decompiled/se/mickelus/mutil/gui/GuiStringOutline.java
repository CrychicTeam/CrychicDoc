package se.mickelus.mutil.gui;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;

public class GuiStringOutline extends GuiString {

    private String cleanString;

    public GuiStringOutline(int x, int y, String string) {
        super(x, y, string);
        this.drawShadow = false;
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, int width, String string) {
        super(x, y, width, string);
        this.drawShadow = false;
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, GuiAttachment attachment) {
        super(x, y, string, attachment);
        this.drawShadow = false;
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color) {
        super(x, y, string, color);
        this.drawShadow = false;
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    public GuiStringOutline(int x, int y, String string, int color, GuiAttachment attachment) {
        super(x, y, string, color, attachment);
        this.drawShadow = false;
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    @Override
    public void setString(String string) {
        super.setString(string);
        this.cleanString = ChatFormatting.stripFormatting(this.string);
    }

    @Override
    protected void drawString(GuiGraphics graphics, String text, int x, int y, int color, float opacity, boolean drawShadow) {
        graphics.pose().pushPose();
        super.drawString(graphics, this.cleanString, x - 1, y - 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x, y - 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x + 1, y - 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x - 1, y + 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x, y + 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x + 1, y + 1, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x + 1, y, 0, opacity, false);
        super.drawString(graphics, this.cleanString, x - 1, y, 0, opacity, false);
        graphics.pose().translate(0.0, 0.0, 0.002F);
        super.drawString(graphics, text, x, y, color, opacity, false);
        graphics.pose().popPose();
    }
}