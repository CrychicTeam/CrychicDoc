package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;

public class GuiTextSmall extends GuiText {

    public GuiTextSmall(int x, int y, int width, String string) {
        super(x, y, width, string);
    }

    @Override
    public void setString(String string) {
        this.string = string.replace("\\n", "\n");
        this.height = this.fontRenderer.wordWrapHeight(this.string, this.width * 2) / 2;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        graphics.pose().pushPose();
        graphics.pose().scale(0.5F, 0.5F, 0.5F);
        renderText(graphics, this.fontRenderer, this.string, (refX + this.x) * 2, (refY + this.y) * 2, this.width * 2, 16777215, opacity);
        graphics.pose().popPose();
        this.drawChildren(graphics, refX + this.x, refY + this.y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }
}