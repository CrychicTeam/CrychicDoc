package dev.ftb.mods.ftblibrary.ui;

public class PanelScrollBar extends ScrollBar {

    private final Panel panel;

    public PanelScrollBar(Panel parent, ScrollBar.Plane plane, Panel p) {
        super(parent, plane, 0);
        this.panel = p;
    }

    public PanelScrollBar(Panel parent, Panel panel) {
        this(parent, ScrollBar.Plane.VERTICAL, panel);
    }

    public Panel getPanel() {
        return this.panel;
    }

    @Override
    public void setMinValue(double min) {
    }

    @Override
    public double getMinValue() {
        return 0.0;
    }

    @Override
    public void setMaxValue(double max) {
        throw new UnsupportedOperationException("attempt to set max value of panel scrollbar");
    }

    @Override
    public double getMaxValue() {
        return this.plane.isVertical ? (double) (this.panel.getContentHeight() - this.panel.getHeight()) : (double) (this.panel.getContentWidth() - this.panel.getWidth());
    }

    @Override
    public void setScrollStep(double s) {
        this.panel.setScrollStep(s);
    }

    @Override
    public double getScrollStep() {
        return this.panel.getScrollStep();
    }

    @Override
    public int getScrollBarSize() {
        double max = this.getMaxValue();
        if (max <= 0.0) {
            return 0;
        } else {
            int size = this.plane.isVertical ? (int) ((double) this.panel.height / (max + (double) this.panel.height) * (double) this.height) : (int) ((double) this.panel.width / (max + (double) this.panel.width) * (double) this.width);
            return Math.max(size, 10);
        }
    }

    @Override
    public void onMoved() {
        double value = this.getMaxValue() <= 0.0 ? 0.0 : this.getValue();
        if (this.plane.isVertical) {
            this.panel.setScrollY(value);
        } else {
            this.panel.setScrollX(value);
        }
    }

    @Override
    public boolean canMouseScroll() {
        return super.canMouseScroll() || this.panel.isMouseOver();
    }

    @Override
    public boolean shouldDraw() {
        return this.getScrollBarSize() > 0;
    }

    @Override
    public boolean isEnabled() {
        return this.getScrollBarSize() > 0;
    }
}