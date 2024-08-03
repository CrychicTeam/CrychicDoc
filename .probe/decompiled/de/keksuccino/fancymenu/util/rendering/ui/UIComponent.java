package de.keksuccino.fancymenu.util.rendering.ui;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Renderable;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import org.jetbrains.annotations.NotNull;

public abstract class UIComponent extends UIBase implements FocuslessContainerEventHandler, Renderable, NarratableEntry {

    public float posZ = 0.0F;

    protected boolean hovered = false;

    protected boolean visible = true;

    protected Minecraft mc = Minecraft.getInstance();

    protected boolean dragging = false;

    protected final List<GuiEventListener> children = new ArrayList();

    public abstract void renderComponent(@NotNull GuiGraphics var1, double var2, double var4, float var6);

    @Override
    public void render(@NotNull GuiGraphics graphics, int ignoredMouseX, int ignoredMouseY, float partial) {
        if (this.isVisible()) {
            this.hovered = this.isMouseOver();
            graphics.pose().pushPose();
            graphics.pose().scale(this.getFixedComponentScale(), this.getFixedComponentScale(), this.getFixedComponentScale());
            graphics.pose().translate(this.getTranslatedX(), this.getTranslatedY(), this.posZ);
            this.renderComponent(graphics, this.getRealMouseX(), this.getRealMouseY(), partial);
            graphics.pose().popPose();
        }
    }

    protected float getRealX() {
        return 0.0F;
    }

    protected float getRealY() {
        return 0.0F;
    }

    public abstract float getTranslatedX();

    public abstract float getTranslatedY();

    public abstract float getWidth();

    public abstract float getHeight();

    public double getRealMouseX() {
        return (this.mc.mouseHandler.xpos() - (double) (this.getTranslatedX() * this.getComponentScale())) / (double) this.getComponentScale();
    }

    public double getRealMouseY() {
        return (this.mc.mouseHandler.ypos() - (double) (this.getTranslatedY() * this.getComponentScale())) / (double) this.getComponentScale();
    }

    public double getTranslatedMouseX() {
        return this.mc.mouseHandler.xpos() / (double) this.getComponentScale();
    }

    public double getTranslatedMouseY() {
        return this.mc.mouseHandler.ypos() / (double) this.getComponentScale();
    }

    protected float getScreenWidth() {
        return (float) this.mc.getWindow().getWidth() / this.getComponentScale();
    }

    protected float getScreenHeight() {
        return (float) this.mc.getWindow().getHeight() / this.getComponentScale();
    }

    protected boolean isComponentAreaHovered(float x, float y, float width, float height, boolean isRealPosition) {
        double mX = this.getRealMouseX();
        double mY = this.getRealMouseY();
        if (!isRealPosition) {
            x -= this.getTranslatedX();
            y -= this.getTranslatedY();
        }
        return isXYInArea(mX, mY, (double) x, (double) y, (double) (width + 1.0F), (double) (height + 1.0F));
    }

    public float getComponentScale() {
        return getUIScale();
    }

    public float getFixedComponentScale() {
        return calculateFixedScale(this.getComponentScale());
    }

    protected void enableComponentScissor(GuiGraphics graphics, int x, int y, int width, int height, boolean isRealPosition) {
        if (isRealPosition) {
            x = (int) ((float) x + this.getTranslatedX());
            y = (int) ((float) y + this.getTranslatedY());
        }
        int scissorX = (int) ((float) x * this.getFixedComponentScale());
        int scissorY = (int) ((float) y * this.getFixedComponentScale());
        int scissorWidth = (int) ((float) width * this.getFixedComponentScale());
        int scissorHeight = (int) ((float) height * this.getFixedComponentScale());
        graphics.enableScissor(scissorX, scissorY, scissorX + scissorWidth, scissorY + scissorHeight);
    }

    protected void disableComponentScissor(GuiGraphics graphics) {
        graphics.disableScissor();
    }

    public boolean isHovered() {
        return !this.isVisible() ? false : this.hovered;
    }

    public boolean isVisible() {
        return this.visible;
    }

    public void setVisible(boolean visible) {
        this.visible = visible;
    }

    @NotNull
    @Override
    public List<GuiEventListener> children() {
        return this.children;
    }

    protected boolean mouseClickedComponent(double realMouseX, double realMouseY, double translatedMouseX, double translatedMouseY, int button) {
        for (GuiEventListener child : this.children()) {
            if (child.mouseClicked(realMouseX, realMouseY, button)) {
                this.m_7522_(child);
                if (button == 0) {
                    this.setDragging(true);
                }
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean mouseClicked(double ignoredMouseX, double ignoredMouseY, int button) {
        return this.mouseClickedComponent(this.getRealMouseX(), this.getRealMouseY(), this.getTranslatedMouseX(), this.getTranslatedMouseY(), button);
    }

    protected boolean mouseReleasedComponent(double realMouseX, double realMouseY, double translatedMouseX, double translatedMouseY, int button) {
        this.setDragging(false);
        for (GuiEventListener child : this.children()) {
            if (child.mouseReleased(realMouseX, realMouseY, button)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean mouseReleased(double ignoredMouseX, double ignoredMouseY, int button) {
        return this.mouseReleasedComponent(this.getRealMouseX(), this.getRealMouseY(), this.getTranslatedMouseX(), this.getTranslatedMouseY(), button);
    }

    protected boolean mouseDraggedComponent(double translatedMouseX, double translatedMouseY, int button, double d1, double d2) {
        if (this.isDragging() && button == 0) {
            for (GuiEventListener child : this.children()) {
                if (child.mouseDragged(this.getRealMouseX(), this.getRealMouseY(), button, d1, d2)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean mouseDragged(double ignoredMouseX, double ignoredMouseY, int button, double d1, double d2) {
        return this.mouseDraggedComponent(this.getTranslatedMouseX(), this.getTranslatedMouseY(), button, d1, d2);
    }

    @Override
    public boolean isDragging() {
        return this.dragging;
    }

    @Override
    public void setDragging(boolean dragging) {
        this.dragging = dragging;
    }

    protected boolean mouseScrolledComponent(double realMouseX, double realMouseY, double translatedMouseX, double translatedMouseY, double scrollDelta) {
        for (GuiEventListener child : this.children()) {
            if (child.mouseScrolled(realMouseX, realMouseY, scrollDelta)) {
                return true;
            }
        }
        return false;
    }

    @Deprecated
    @Override
    public boolean mouseScrolled(double ignoredMouseX, double ignoredMouseY, double scrollDelta) {
        return this.mouseScrolledComponent(this.getRealMouseX(), this.getRealMouseY(), this.getTranslatedMouseX(), this.getTranslatedMouseY(), scrollDelta);
    }

    protected void mouseMovedComponent(double realMouseX, double realMouseY) {
    }

    @Deprecated
    @Override
    public void mouseMoved(double ignoredMouseX, double ignoredMouseY) {
        this.mouseMovedComponent(this.getRealMouseX(), this.getRealMouseY());
    }

    public boolean isMouseOver() {
        return !this.isVisible() ? false : this.isComponentAreaHovered(this.getTranslatedX(), this.getTranslatedY(), this.getWidth(), this.getHeight(), false);
    }

    @Deprecated
    @Override
    public boolean isMouseOver(double ignoredMouseX, double ignoredMouseY) {
        return this.isMouseOver();
    }

    @Override
    public void setFocused(boolean var1) {
    }

    @Override
    public boolean isFocused() {
        return false;
    }

    @NotNull
    @Override
    public NarratableEntry.NarrationPriority narrationPriority() {
        return NarratableEntry.NarrationPriority.NONE;
    }

    @Override
    public void updateNarration(@NotNull NarrationElementOutput var1) {
    }
}