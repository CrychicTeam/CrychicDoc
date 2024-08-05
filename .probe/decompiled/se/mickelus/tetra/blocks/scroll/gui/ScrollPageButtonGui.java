package se.mickelus.tetra.blocks.scroll.gui;

import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import se.mickelus.mutil.gui.GuiClickable;
import se.mickelus.mutil.gui.GuiTexture;

@ParametersAreNonnullByDefault
public class ScrollPageButtonGui extends GuiClickable {

    static final ResourceLocation texture = new ResourceLocation("tetra", "textures/gui/pamphlet.png");

    GuiTexture regularTexture;

    GuiTexture hoverTexture;

    public ScrollPageButtonGui(int x, int y, boolean back, Runnable onClick) {
        super(x, y, 25, 14, onClick);
        if (back) {
            this.regularTexture = new GuiTexture(0, 0, this.width, this.height, 0, 205, texture);
            this.hoverTexture = new GuiTexture(0, 0, this.width, this.height, 23, 205, texture);
        } else {
            this.regularTexture = new GuiTexture(0, 0, this.width, this.height, 0, 191, texture);
            this.hoverTexture = new GuiTexture(0, 0, this.width, this.height, 23, 191, texture);
        }
    }

    @Override
    protected void drawChildren(GuiGraphics guiGraphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.hasFocus()) {
            this.hoverTexture.draw(guiGraphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        } else {
            this.regularTexture.draw(guiGraphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        }
    }
}