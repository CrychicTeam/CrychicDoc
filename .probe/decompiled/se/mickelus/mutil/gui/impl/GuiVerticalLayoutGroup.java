package se.mickelus.mutil.gui.impl;

import net.minecraft.client.gui.GuiGraphics;
import se.mickelus.mutil.gui.GuiElement;

public class GuiVerticalLayoutGroup extends GuiElement {

    private boolean needsLayout = false;

    private int spacing;

    public GuiVerticalLayoutGroup(int x, int y, int width, int spacing) {
        super(x, y, 0, width);
        this.spacing = spacing;
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        this.triggerLayout();
    }

    public void triggerLayout() {
        this.needsLayout = true;
    }

    public void forceLayout() {
        this.layoutChildren();
    }

    private void layoutChildren() {
        int offset = 0;
        for (GuiElement child : this.getChildren()) {
            child.setY(offset);
            offset += child.getHeight() + this.spacing;
        }
        this.setHeight(offset - this.spacing);
        this.needsLayout = false;
    }

    @Override
    protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        if (this.needsLayout) {
            this.layoutChildren();
        }
        super.drawChildren(graphics, refX, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
    }
}