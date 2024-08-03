package dev.ftb.mods.ftblibrary.ui;

import dev.ftb.mods.ftblibrary.math.MathUtils;
import dev.ftb.mods.ftblibrary.ui.input.MouseButton;
import dev.ftb.mods.ftblibrary.util.TooltipList;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentContents;
import net.minecraft.util.Mth;

public class ScrollBar extends Widget {

    protected final ScrollBar.Plane plane;

    private final int scrollBarSize;

    private double value = 0.0;

    private double scrollStep = 20.0;

    private double grab = -10000.0;

    private double minValue = 0.0;

    private double maxValue = 100.0;

    private boolean canAlwaysScroll = false;

    private boolean canAlwaysScrollPlane = true;

    public ScrollBar(Panel parent, ScrollBar.Plane p, int ss) {
        super(parent);
        this.plane = p;
        this.scrollBarSize = Math.max(ss, 0);
    }

    public void setCanAlwaysScroll(boolean v) {
        this.canAlwaysScroll = v;
    }

    public void setCanAlwaysScrollPlane(boolean v) {
        this.canAlwaysScrollPlane = v;
    }

    public ScrollBar.Plane getPlane() {
        return this.plane;
    }

    public void setMinValue(double min) {
        this.minValue = min;
        this.setValue(this.getValue());
    }

    public double getMinValue() {
        return this.minValue;
    }

    public void setMaxValue(double max) {
        this.maxValue = max;
        this.setValue(this.getValue());
    }

    public double getMaxValue() {
        return this.maxValue;
    }

    public void setScrollStep(double s) {
        this.scrollStep = Math.max(0.0, s);
    }

    public int getScrollBarSize() {
        return this.scrollBarSize;
    }

    @Override
    public boolean mousePressed(MouseButton button) {
        if (this.isMouseOver()) {
            this.grab = this.plane.isVertical ? (double) this.getMouseY() - ((double) this.getY() + this.getMappedValue((double) (this.height - this.getScrollBarSize()))) : (double) this.getMouseX() - ((double) this.getX() + this.getMappedValue((double) (this.width - this.getScrollBarSize())));
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double scroll) {
        if (scroll != 0.0 && this.canMouseScrollPlane() && this.canMouseScroll()) {
            this.setValue(this.getValue() - this.getScrollStep() * scroll);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void addMouseOverText(TooltipList list) {
        if (this.showValueOnMouseOver()) {
            Component t = this.getTitle();
            list.string(t.getContents() == ComponentContents.EMPTY ? Double.toString(this.getValue()) : t + ": " + this.getValue());
        }
        if (Theme.renderDebugBoxes) {
            list.styledString("Size: " + this.getScrollBarSize(), ChatFormatting.DARK_GRAY);
            list.styledString("Max: " + this.getMaxValue(), ChatFormatting.DARK_GRAY);
            list.styledString("Value: " + this.getValue(), ChatFormatting.DARK_GRAY);
        }
    }

    public boolean showValueOnMouseOver() {
        return false;
    }

    @Override
    public void draw(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        int scrollBarSize = this.getScrollBarSize();
        if (scrollBarSize > 0) {
            double v = this.getValue();
            if (this.grab != -10000.0) {
                if (isMouseButtonDown(MouseButton.LEFT)) {
                    if (this.plane.isVertical) {
                        v = ((double) this.getMouseY() - ((double) y + this.grab)) * this.getMaxValue() / (double) (this.height - scrollBarSize);
                    } else {
                        v = ((double) this.getMouseX() - ((double) x + this.grab)) * this.getMaxValue() / (double) (this.width - scrollBarSize);
                    }
                } else {
                    this.grab = -10000.0;
                }
            }
            this.setValue(v);
        }
        this.drawBackground(graphics, theme, x, y, this.width, this.height);
        if (scrollBarSize > 0) {
            if (this.plane.isVertical) {
                this.drawScrollBar(graphics, theme, x, (int) ((double) y + this.getMappedValue((double) (this.height - scrollBarSize))), this.width, scrollBarSize);
            } else {
                this.drawScrollBar(graphics, theme, (int) ((double) x + this.getMappedValue((double) (this.width - scrollBarSize))), y, scrollBarSize, this.height);
            }
        }
    }

    public void drawBackground(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawScrollBarBackground(graphics, x, y, w, h, this.getWidgetType());
    }

    public void drawScrollBar(GuiGraphics graphics, Theme theme, int x, int y, int w, int h) {
        theme.drawScrollBar(graphics, x, y, w, h, WidgetType.mouseOver(this.grab != -10000.0), this.plane.isVertical);
    }

    public void onMoved() {
    }

    public boolean canMouseScrollPlane() {
        return this.canAlwaysScrollPlane || isShiftKeyDown() != this.plane.isVertical;
    }

    public boolean canMouseScroll() {
        return this.canAlwaysScroll || this.isMouseOver();
    }

    public void setValue(double v) {
        v = Mth.clamp(v, this.getMinValue(), this.getMaxValue());
        if (this.value != v) {
            this.value = v;
            this.onMoved();
        }
    }

    public double getValue() {
        return this.value;
    }

    public double getMappedValue(double max) {
        return MathUtils.map(this.getMinValue(), this.getMaxValue(), 0.0, max, this.value);
    }

    public double getScrollStep() {
        return this.scrollStep;
    }

    public static enum Plane {

        HORIZONTAL(false), VERTICAL(true);

        public final boolean isVertical;

        private Plane(boolean v) {
            this.isVertical = v;
        }
    }
}