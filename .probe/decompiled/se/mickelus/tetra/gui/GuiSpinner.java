package se.mickelus.tetra.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiElement;

@ParametersAreNonnullByDefault
public class GuiSpinner extends GuiElement {

    public GuiSpinner(int x, int y) {
        super(x, y, 5, 5);
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.drawBlip(graphics, refX + this.x, refY + this.y, 0);
        this.drawBlip(graphics, refX + this.x + 2, refY + this.y - 1, 1);
        this.drawBlip(graphics, refX + this.x + 4, refY + this.y, 2);
        this.drawBlip(graphics, refX + this.x + 5, refY + this.y + 2, 3);
        this.drawBlip(graphics, refX + this.x + 4, refY + this.y + 4, 4);
        this.drawBlip(graphics, refX + this.x + 2, refY + this.y + 5, 5);
        this.drawBlip(graphics, refX + this.x, refY + this.y + 4, 6);
        this.drawBlip(graphics, refX + this.x - 1, refY + this.y + 2, 7);
    }

    private void drawBlip(GuiGraphics graphics, int x, int y, int offset) {
        float opacity = 1.0F - Math.max((float) ((System.currentTimeMillis() - (long) (offset * 200)) % 1600L) / 1600.0F, 0.0F);
        drawRect(graphics, x, y, x + 1, y + 1, 16777215, opacity);
    }
}