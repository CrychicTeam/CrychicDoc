package se.mickelus.mutil.gui;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import se.mickelus.mutil.gui.animation.KeyframeAnimation;

public class GuiElement {

    protected int x;

    protected int y;

    protected GuiAttachment attachmentPoint = GuiAttachment.topLeft;

    protected GuiAttachment attachmentAnchor = GuiAttachment.topLeft;

    protected int width;

    protected int height;

    protected float opacity = 1.0F;

    protected boolean hasFocus = false;

    protected boolean isVisible = true;

    protected boolean shouldRemove = false;

    protected ArrayList<GuiElement> elements;

    protected Set<KeyframeAnimation> activeAnimations;

    public GuiElement(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.elements = new ArrayList();
        this.activeAnimations = new HashSet();
    }

    public void draw(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.drawChildren(graphics, refX + this.x, refY + this.y, screenWidth, screenHeight, mouseX, mouseY, opacity * this.opacity);
    }

    public void updateAnimations() {
        this.activeAnimations.removeIf(animation -> !animation.isActive());
        this.activeAnimations.forEach(KeyframeAnimation::preDraw);
    }

    protected void drawChildren(GuiGraphics graphics, int refX, int refY, int screenWidth, int screenHeight, int mouseX, int mouseY, float opacity) {
        this.elements.removeIf(GuiElement::shouldRemove);
        this.elements.stream().filter(GuiElement::isVisible).forEach(element -> {
            element.updateAnimations();
            element.draw(graphics, refX + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint), refY + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint), screenWidth, screenHeight, mouseX, mouseY, opacity);
        });
    }

    protected static int getXOffset(GuiElement element, GuiAttachment attachment) {
        switch(attachment) {
            case topLeft:
            case middleLeft:
            case bottomLeft:
                return 0;
            case topCenter:
            case middleCenter:
            case bottomCenter:
                return element.getWidth() / 2;
            case topRight:
            case middleRight:
            case bottomRight:
                return element.getWidth();
            default:
                return 0;
        }
    }

    protected static int getYOffset(GuiElement element, GuiAttachment attachment) {
        switch(attachment) {
            case topLeft:
            case topCenter:
            case topRight:
                return 0;
            case middleLeft:
            case middleCenter:
            case middleRight:
                return element.getHeight() / 2;
            case bottomLeft:
            case bottomCenter:
            case bottomRight:
                return element.getHeight();
            default:
                return 0;
        }
    }

    public boolean onMouseClick(int x, int y, int button) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onMouseClick(x, y, button)) {
                return true;
            }
        }
        return false;
    }

    public void onMouseRelease(int x, int y, int button) {
        this.elements.forEach(element -> element.onMouseRelease(x, y, button));
    }

    public boolean onMouseScroll(double mouseX, double mouseY, double distance) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onMouseScroll(mouseX, mouseY, distance)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyPress(int keyCode, int scanCode, int modifiers) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onKeyPress(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public boolean onKeyRelease(int keyCode, int scanCode, int modifiers) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onKeyRelease(keyCode, scanCode, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public boolean onCharType(char character, int modifiers) {
        for (int i = this.elements.size() - 1; i >= 0; i--) {
            if (((GuiElement) this.elements.get(i)).isVisible() && ((GuiElement) this.elements.get(i)).onCharType(character, modifiers)) {
                return true;
            }
        }
        return false;
    }

    public void updateFocusState(int refX, int refY, int mouseX, int mouseY) {
        this.elements.stream().filter(GuiElement::isVisible).forEach(element -> element.updateFocusState(refX + this.x + getXOffset(this, element.attachmentAnchor) - getXOffset(element, element.attachmentPoint), refY + this.y + getYOffset(this, element.attachmentAnchor) - getYOffset(element, element.attachmentPoint), mouseX, mouseY));
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

    protected void onFocus() {
    }

    protected void onBlur() {
    }

    public boolean hasFocus() {
        return this.hasFocus;
    }

    public int getX() {
        return this.x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return this.y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public GuiElement setAttachmentPoint(GuiAttachment attachment) {
        this.attachmentPoint = attachment;
        return this;
    }

    public GuiElement setAttachmentAnchor(GuiAttachment attachment) {
        this.attachmentAnchor = attachment;
        return this;
    }

    public GuiElement setAttachment(GuiAttachment attachment) {
        this.attachmentPoint = attachment;
        this.attachmentAnchor = attachment;
        return this;
    }

    public GuiAttachment getAttachmentPoint() {
        return this.attachmentPoint;
    }

    public GuiAttachment getAttachmentAnchor() {
        return this.attachmentAnchor;
    }

    public int getWidth() {
        return this.width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return this.height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public void setVisible(boolean visible) {
        if (this.isVisible != visible) {
            if (visible) {
                this.onShow();
            } else if (!this.onHide()) {
                return;
            }
            this.isVisible = visible;
        }
    }

    public boolean isVisible() {
        return this.isVisible;
    }

    protected void onShow() {
    }

    protected boolean onHide() {
        this.hasFocus = false;
        return true;
    }

    public GuiElement setOpacity(float opacity) {
        this.opacity = opacity;
        return this;
    }

    public float getOpacity() {
        return this.opacity;
    }

    public void addAnimation(KeyframeAnimation animation) {
        this.activeAnimations.add(animation);
    }

    public void removeAnimation(KeyframeAnimation animation) {
        this.activeAnimations.remove(animation);
    }

    public void remove() {
        this.shouldRemove = true;
    }

    public boolean shouldRemove() {
        return this.shouldRemove;
    }

    public void addChild(GuiElement child) {
        this.elements.add(child);
    }

    public void clearChildren() {
        this.elements.clear();
    }

    public int getNumChildren() {
        return this.elements.size();
    }

    public GuiElement getChild(int index) {
        return index >= 0 && index < this.elements.size() ? (GuiElement) this.elements.get(index) : null;
    }

    public List<GuiElement> getChildren() {
        return Collections.unmodifiableList(this.elements);
    }

    public <T> List<T> getChildren(Class<T> type) {
        return (List<T>) this.elements.stream().filter(type::isInstance).map(type::cast).collect(Collectors.toList());
    }

    public List<Component> getTooltipLines() {
        return this.isVisible() ? (List) this.elements.stream().map(GuiElement::getTooltipLines).filter(Objects::nonNull).findFirst().orElse(null) : null;
    }

    protected static void drawRect(GuiGraphics graphics, int left, int top, int right, int bottom, int color, float opacity) {
        graphics.fill(left, top, right, bottom, colorWithOpacity(color, opacity));
    }

    protected static int colorWithOpacity(int color, float opacity) {
        return colorWithOpacity(color, Math.round(opacity * 255.0F));
    }

    protected static int colorWithOpacity(int color, int opacity) {
        return color & 16777215 | opacity * (color >> 24 == 0 ? 255 : color >> 24 & 0xFF) / 255 << 24;
    }
}