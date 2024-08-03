package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.impl.GuiHorizontalScrollable;

public class ScrollBarGui extends GuiElement {

    private final GuiHorizontalScrollable scrollable;

    private final boolean unscrollableHidden;

    public ScrollBarGui(int x, int y, int width, int height, GuiHorizontalScrollable scrollable) {
        this(x, y, width, height, scrollable, false);
    }

    public ScrollBarGui(int x, int y, int width, int height, GuiHorizontalScrollable scrollable, boolean unscrollableHidden) {
        super(x, y, width, height);
        this.scrollable = scrollable;
        this.unscrollableHidden = unscrollableHidden;
    }

    private boolean isActive() {
        return !this.unscrollableHidden || this.scrollable.getOffsetMax() > 0;
    }

    @Override
    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.isActive()) {
            super.draw(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
            this.drawBackground(graphics, refX + this.x, refY + this.y);
            int contentWidth = this.scrollable.getOffsetMax() + this.scrollable.getWidth();
            int handleWidth = Math.max(3, (int) ((float) this.scrollable.getWidth() * 1.0F / (float) contentWidth * (float) this.width) + 1);
            int handleOffset = (int) (this.scrollable.getOffset() / (double) contentWidth * (double) this.width);
            this.drawHandle(graphics, refX + this.x + handleOffset, refY + this.y, handleWidth);
        }
    }

    protected void drawBackground(GuiGraphics graphics, int x, int y) {
        drawRect(graphics, x, y, x + this.width, y + this.height, 16777215, this.opacity * 0.2F);
    }

    protected void drawHandle(GuiGraphics graphics, int x, int y, int handleWidth) {
        drawRect(graphics, x, y, x + handleWidth, y + this.height, 16777215, this.opacity * 0.7F);
    }
}