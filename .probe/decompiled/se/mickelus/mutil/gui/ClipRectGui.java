package se.mickelus.mutil.gui;

import net.minecraft.client.gui.GuiGraphics;

public class ClipRectGui extends GuiElement {

    public ClipRectGui(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    @Override
    protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        graphics.enableScissor(refX, refY, refX + this.width, refY + this.height);
        super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        graphics.disableScissor();
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        boolean gainFocus = mouseX >= this.getX() + refX && mouseX < this.getX() + refX + this.getWidth() && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
        if (this.hasFocus) {
            this.elements.stream().filter(GuiElement::isVisible).forEach(element -> element.updateFocusState(refX + this.x + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint), refY + this.y + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint), mouseX, mouseY));
        }
    }
}