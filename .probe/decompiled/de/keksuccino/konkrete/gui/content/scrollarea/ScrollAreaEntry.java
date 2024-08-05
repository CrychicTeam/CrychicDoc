package de.keksuccino.konkrete.gui.content.scrollarea;

import de.keksuccino.konkrete.input.MouseInput;
import net.minecraft.client.gui.GuiGraphics;

public abstract class ScrollAreaEntry {

    public int x = 0;

    public int y = 0;

    public final ScrollArea parent;

    public ScrollAreaEntry(ScrollArea parent) {
        this.parent = parent;
    }

    public abstract void renderEntry(GuiGraphics var1);

    public void render(GuiGraphics graphics) {
        if (this.isVisible()) {
            this.renderEntry(graphics);
        }
    }

    public abstract int getHeight();

    public int getWidth() {
        return this.parent.width;
    }

    @Deprecated
    public boolean isHoveredOrFocused() {
        return this.isHovered();
    }

    public boolean isHovered() {
        int mx = MouseInput.getMouseX();
        int my = MouseInput.getMouseY();
        return this.x <= mx && this.y <= my && this.x + this.parent.width >= mx && this.y + this.getHeight() >= my;
    }

    public boolean isVisible() {
        return this.parent.y < this.y + this.getHeight() && this.parent.y + this.parent.height > this.y;
    }
}