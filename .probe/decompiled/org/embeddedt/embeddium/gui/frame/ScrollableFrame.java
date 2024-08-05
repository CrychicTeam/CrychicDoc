package org.embeddedt.embeddium.gui.frame;

import java.util.concurrent.atomic.AtomicReference;
import me.jellysquid.mods.sodium.client.gui.options.control.ControlElement;
import me.jellysquid.mods.sodium.client.util.Dim2i;
import net.minecraft.client.gui.GuiGraphics;
import org.embeddedt.embeddium.gui.frame.components.ScrollBarComponent;

public class ScrollableFrame extends AbstractFrame {

    protected final Dim2i frameOrigin;

    protected final AbstractFrame frame;

    private boolean canScrollHorizontal;

    private boolean canScrollVertical;

    private Dim2i viewPortDimension = null;

    private ScrollBarComponent verticalScrollBar = null;

    private ScrollBarComponent horizontalScrollBar = null;

    public ScrollableFrame(Dim2i dim, AbstractFrame frame, boolean renderOutline, AtomicReference<Integer> verticalScrollBarOffset, AtomicReference<Integer> horizontalScrollBarOffset) {
        super(dim, renderOutline);
        this.frame = frame;
        this.frameOrigin = new Dim2i(frame.dim.x(), frame.dim.y(), 0, 0);
        this.setupFrame(verticalScrollBarOffset, horizontalScrollBarOffset);
        this.buildFrame();
    }

    public static ScrollableFrame.Builder createBuilder() {
        return new ScrollableFrame.Builder();
    }

    public void setupFrame(AtomicReference<Integer> verticalScrollBarOffset, AtomicReference<Integer> horizontalScrollBarOffset) {
        int maxWidth = 0;
        int maxHeight = 0;
        if (!this.dim.canFitDimension(this.frame.dim)) {
            if (this.dim.getLimitX() < this.frame.dim.getLimitX()) {
                int value = this.frame.dim.x() - this.dim.x() + this.frame.dim.width();
                if (maxWidth < value) {
                    maxWidth = value;
                }
            }
            if (this.dim.getLimitY() < this.frame.dim.getLimitY()) {
                int value = this.frame.dim.y() - this.dim.y() + this.frame.dim.height();
                if (maxHeight < value) {
                    maxHeight = value;
                }
            }
        }
        if (maxWidth > 0) {
            this.canScrollHorizontal = true;
        }
        if (maxHeight > 0) {
            this.canScrollVertical = true;
        }
        if (this.canScrollHorizontal && this.canScrollVertical) {
            this.viewPortDimension = new Dim2i(this.dim.x(), this.dim.y(), this.dim.width() - 11, this.dim.height() - 11);
        } else if (this.canScrollHorizontal) {
            this.viewPortDimension = new Dim2i(this.dim.x(), this.dim.y(), this.dim.width(), this.dim.height() - 11);
            this.frame.dim = this.frame.dim.withHeight(this.frame.dim.height() - 11);
        } else if (this.canScrollVertical) {
            this.viewPortDimension = new Dim2i(this.dim.x(), this.dim.y(), this.dim.width() - 11, this.dim.height());
            this.frame.dim = this.frame.dim.withWidth(this.frame.dim.width() - 11);
        }
        if (this.canScrollHorizontal) {
            this.horizontalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.x(), this.viewPortDimension.getLimitY() + 1, this.viewPortDimension.width(), 10), ScrollBarComponent.Mode.HORIZONTAL, this.frame.dim.width(), this.viewPortDimension.width(), offset -> horizontalScrollBarOffset.set(offset));
            this.horizontalScrollBar.setOffset((Integer) horizontalScrollBarOffset.get());
        }
        if (this.canScrollVertical) {
            this.verticalScrollBar = new ScrollBarComponent(new Dim2i(this.viewPortDimension.getLimitX() + 1, this.viewPortDimension.y(), 10, this.viewPortDimension.height()), ScrollBarComponent.Mode.VERTICAL, this.frame.dim.height(), this.viewPortDimension.height(), offset -> verticalScrollBarOffset.set(offset), this.viewPortDimension);
            this.verticalScrollBar.setOffset((Integer) verticalScrollBarOffset.get());
        }
    }

    @Override
    public void buildFrame() {
        this.children.clear();
        this.drawable.clear();
        this.controlElements.clear();
        if (this.canScrollHorizontal) {
            this.horizontalScrollBar.updateThumbPosition();
        }
        if (this.canScrollVertical) {
            this.verticalScrollBar.updateThumbPosition();
        }
        this.frame.buildFrame();
        this.children.add(this.frame);
        super.buildFrame();
        this.frame.registerFocusListener(element -> {
            if (element instanceof ControlElement<?> controlElement && this.canScrollVertical) {
                Dim2i dim = controlElement.getDimensions();
                int inputOffset = this.verticalScrollBar.getOffset();
                int elementTop = dim.y() - inputOffset;
                int elementBottom = dim.getLimitY() - inputOffset;
                if (elementTop <= this.viewPortDimension.y()) {
                    inputOffset += elementTop - this.viewPortDimension.y();
                } else if (elementBottom >= this.viewPortDimension.getLimitY()) {
                    inputOffset += elementBottom - this.viewPortDimension.getLimitY();
                }
                this.verticalScrollBar.setOffset(inputOffset);
            }
        });
    }

    private double applyOffset(ScrollBarComponent component, double coord, boolean negate) {
        return component == null ? coord : coord + (double) (component.getOffset() * (negate ? -1 : 1));
    }

    @Override
    public void render(GuiGraphics drawContext, int mouseX, int mouseY, float delta) {
        if (!this.canScrollHorizontal && !this.canScrollVertical) {
            super.render(drawContext, mouseX, mouseY, delta);
        } else {
            if (this.renderOutline) {
                this.drawBorder(drawContext, this.dim.x(), this.dim.y(), this.dim.getLimitX(), this.dim.getLimitY(), -5592406);
            }
            boolean mouseInViewport = this.viewPortDimension.containsCursor((double) mouseX, (double) mouseY);
            this.applyScissor(this.viewPortDimension.x(), this.viewPortDimension.y(), this.viewPortDimension.width(), this.viewPortDimension.height(), () -> {
                drawContext.pose().pushPose();
                drawContext.pose().translate(this.applyOffset(this.horizontalScrollBar, 0.0, true), this.applyOffset(this.verticalScrollBar, 0.0, true), 0.0);
                super.render(drawContext, mouseInViewport ? (int) this.applyOffset(this.horizontalScrollBar, (double) mouseX, false) : -1, mouseInViewport ? (int) this.applyOffset(this.verticalScrollBar, (double) mouseY, false) : -1, delta);
                drawContext.pose().popPose();
            });
        }
        if (this.canScrollHorizontal) {
            this.horizontalScrollBar.render(drawContext, mouseX, mouseY, delta);
        }
        if (this.canScrollVertical) {
            this.verticalScrollBar.render(drawContext, mouseX, mouseY, delta);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return this.canScrollHorizontal && this.horizontalScrollBar.mouseClicked(mouseX, mouseY, button) || this.canScrollVertical && this.verticalScrollBar.mouseClicked(mouseX, mouseY, button) || super.m_6375_(this.applyOffset(this.horizontalScrollBar, mouseX, false), this.applyOffset(this.verticalScrollBar, mouseY, false), button);
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return this.canScrollHorizontal && this.horizontalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || this.canScrollVertical && this.verticalScrollBar.mouseDragged(mouseX, mouseY, button, deltaX, deltaY) || super.m_7979_(this.applyOffset(this.horizontalScrollBar, mouseX, false), this.applyOffset(this.verticalScrollBar, mouseY, false), button, deltaX, deltaY);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return this.canScrollHorizontal && this.horizontalScrollBar.mouseReleased(mouseX, mouseY, button) || this.canScrollVertical && this.verticalScrollBar.mouseReleased(mouseX, mouseY, button) || super.m_6348_(this.applyOffset(this.horizontalScrollBar, mouseX, false), this.applyOffset(this.verticalScrollBar, mouseY, false), button);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double verticalAmount) {
        return this.canScrollHorizontal && this.horizontalScrollBar.mouseScrolled(mouseX, mouseY, verticalAmount) || this.canScrollVertical && this.verticalScrollBar.mouseScrolled(mouseX, mouseY, verticalAmount) || super.m_6050_(this.applyOffset(this.horizontalScrollBar, mouseX, false), this.applyOffset(this.verticalScrollBar, mouseY, false), verticalAmount);
    }

    public static class Builder {

        private boolean renderOutline = false;

        private Dim2i dim = null;

        private AbstractFrame frame = null;

        private AtomicReference<Integer> verticalScrollBarOffset = new AtomicReference(0);

        private AtomicReference<Integer> horizontalScrollBarOffset = new AtomicReference(0);

        public ScrollableFrame.Builder setDimension(Dim2i dim) {
            this.dim = dim;
            return this;
        }

        public ScrollableFrame.Builder shouldRenderOutline(boolean state) {
            this.renderOutline = state;
            return this;
        }

        public ScrollableFrame.Builder setVerticalScrollBarOffset(AtomicReference<Integer> verticalScrollBarOffset) {
            this.verticalScrollBarOffset = verticalScrollBarOffset;
            return this;
        }

        public ScrollableFrame.Builder setHorizontalScrollBarOffset(AtomicReference<Integer> horizontalScrollBarOffset) {
            this.horizontalScrollBarOffset = horizontalScrollBarOffset;
            return this;
        }

        public ScrollableFrame.Builder setFrame(AbstractFrame frame) {
            this.frame = frame;
            return this;
        }

        public ScrollableFrame build() {
            return new ScrollableFrame(this.dim, this.frame, this.renderOutline, this.verticalScrollBarOffset, this.horizontalScrollBarOffset);
        }
    }
}