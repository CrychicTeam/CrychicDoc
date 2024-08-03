package se.mickelus.mutil.gui.impl;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.Mth;
import se.mickelus.mutil.gui.GuiElement;

public class GuiHorizontalScrollable extends GuiElement {

    private boolean dirty = false;

    private double scrollOffset = 0.0;

    private double scrollVelocity = 0.0;

    private boolean isGlobal = false;

    private int min;

    private int max;

    private long lastDraw = System.currentTimeMillis();

    public GuiHorizontalScrollable(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public GuiHorizontalScrollable setGlobal(boolean isGlobal) {
        this.isGlobal = isGlobal;
        return this;
    }

    public double getOffset() {
        return this.scrollOffset;
    }

    public void setOffset(double offset) {
        this.scrollOffset = Mth.clamp(offset, (double) this.min, (double) this.max);
    }

    public int getOffsetMax() {
        return this.max;
    }

    public void markDirty() {
        this.dirty = true;
    }

    public void forceRefreshBounds() {
        this.calculateBounds();
    }

    private void calculateBounds() {
        int tempMax = 0;
        this.min = 0;
        for (GuiElement element : this.getChildren()) {
            int x = getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint());
            this.min = Math.min(x, this.min);
            tempMax = Math.max(x + element.getWidth(), tempMax);
        }
        this.max = Math.max(tempMax - this.width, 0);
        this.scrollOffset = Mth.clamp(this.scrollOffset, (double) this.min, (double) this.max);
        this.dirty = false;
    }

    @Override
    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        if (super.onMouseScroll(mouseX, mouseY, distance)) {
            return true;
        } else if (!this.isGlobal && !this.hasFocus()) {
            return false;
        } else {
            if (Math.signum(this.scrollVelocity) != Math.signum(-distance)) {
                this.scrollVelocity = 0.0;
            }
            this.scrollVelocity -= distance * 12.0;
            this.scrollOffset = Mth.clamp(this.scrollOffset - distance * 6.0, (double) this.min, (double) this.max);
            return true;
        }
    }

    @Override
    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        this.elements.stream().filter(GuiElement::isVisible).forEach(element -> element.updateFocusState(refX + this.x + getXOffset(this, element.getAttachmentAnchor()) - getXOffset(element, element.getAttachmentPoint()) - (int) this.scrollOffset, refY + this.y + getYOffset(this, element.getAttachmentAnchor()) - getYOffset(element, element.getAttachmentPoint()), mouseX, mouseY));
        boolean gainFocus = mouseX >= this.getX() + refX && mouseX < this.getX() + refX + this.getWidth() && mouseY >= this.getY() + refY && mouseY < this.getY() + refY + this.getHeight();
        if (gainFocus != this.hasFocus) {
            this.hasFocus = gainFocus;
            if (this.hasFocus) {
                this.onFocus();
            } else {
                this.onBlur();
            }
        }
    }

    @Override
    protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        long now = System.currentTimeMillis();
        if (this.scrollVelocity != 0.0) {
            double dist = (this.scrollVelocity * 0.2 + Math.signum(this.scrollVelocity) * 1.0) * (double) (now - this.lastDraw) / 1000.0 * 50.0;
            if (Math.signum(this.scrollVelocity) != Math.signum(this.scrollVelocity - dist)) {
                dist = this.scrollVelocity;
                this.scrollVelocity = 0.0;
            } else {
                this.scrollVelocity -= dist;
            }
            this.scrollOffset = Mth.clamp(this.scrollOffset + dist, (double) this.min, (double) this.max);
        }
        this.lastDraw = now;
        super.drawChildren(graphics, refX - (int) this.scrollOffset, refY, screenWidth, screenHeight, mouseX, mouseY, opacity);
        if (this.dirty) {
            this.calculateBounds();
        }
    }

    @Override
    public void addChild(GuiElement child) {
        super.addChild(child);
        this.markDirty();
    }

    @Override
    public void clearChildren() {
        super.clearChildren();
        this.markDirty();
    }
}