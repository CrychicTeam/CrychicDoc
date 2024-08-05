package mezz.jei.common.gui.elements;

import mezz.jei.api.gui.drawable.IDrawable;
import net.minecraft.client.gui.GuiGraphics;

public class OffsetDrawable implements IDrawable {

    private final IDrawable drawable;

    private final int xOffset;

    private final int yOffset;

    public static IDrawable create(IDrawable drawable, int xOffset, int yOffset) {
        return (IDrawable) (xOffset == 0 && yOffset == 0 ? drawable : new OffsetDrawable(drawable, xOffset, yOffset));
    }

    private OffsetDrawable(IDrawable drawable, int xOffset, int yOffset) {
        this.drawable = drawable;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    @Override
    public int getWidth() {
        return this.drawable.getWidth();
    }

    @Override
    public int getHeight() {
        return this.drawable.getHeight();
    }

    @Override
    public void draw(GuiGraphics guiGraphics, int xOffset, int yOffset) {
        this.drawable.draw(guiGraphics, this.xOffset + xOffset, this.yOffset + yOffset);
    }

    @Override
    public void draw(GuiGraphics guiGraphics) {
        this.drawable.draw(guiGraphics, this.xOffset, this.yOffset);
    }
}