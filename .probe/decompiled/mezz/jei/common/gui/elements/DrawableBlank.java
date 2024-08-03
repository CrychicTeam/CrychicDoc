package mezz.jei.common.gui.elements;

import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.drawable.IDrawableStatic;
import net.minecraft.client.gui.GuiGraphics;

public class DrawableBlank implements IDrawableStatic, IDrawableAnimated {

    private final int width;

    private final int height;

    public DrawableBlank(int width, int height) {
        this.width = width;
        this.height = height;
    }

    @Override
    public int getWidth() {
        return this.width;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset, int maskTop, int maskBottom, int maskLeft, int maskRight) {
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
    }
}