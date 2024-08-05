package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;

public class GuiRect extends GuiElement {

    private int color;

    private final boolean offset;

    public GuiRect(int x, int y, int width, int height, int color) {
        this(x, y, width, height, color, false);
    }

    public GuiRect(int x, int y, int width, int height, int color, boolean offset) {
        super(x, y, offset ? width + 1 : width, offset ? height + 1 : height);
        this.color = color;
        this.offset = offset;
    }

    public GuiRect setColor(int color) {
        this.color = color;
        return this;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.offset) {
            graphics.pose().pushPose();
            graphics.pose().translate(0.5F, 0.5F, 0.0F);
            drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width - 1, refY + this.y + this.height - 1, this.color, opacity * this.getOpacity());
            graphics.pose().popPose();
        } else {
            drawRect(graphics, refX + this.x, refY + this.y, refX + this.x + this.width, refY + this.y + this.height, this.color, opacity * this.getOpacity());
        }
    }
}