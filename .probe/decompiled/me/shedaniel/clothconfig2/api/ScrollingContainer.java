package me.shedaniel.clothconfig2.api;

import me.shedaniel.clothconfig2.ClothConfigInitializer;
import me.shedaniel.clothconfig2.impl.EasingMethod;
import me.shedaniel.math.Rectangle;
import me.shedaniel.math.impl.PointHelper;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;

@Deprecated
public abstract class ScrollingContainer {

    public double scrollAmount;

    public double scrollTarget;

    public long start;

    public long duration;

    public boolean draggingScrollBar = false;

    public abstract Rectangle getBounds();

    public Rectangle getScissorBounds() {
        Rectangle bounds = this.getBounds();
        return this.hasScrollBar() ? new Rectangle(bounds.x, bounds.y, bounds.width - 6, bounds.height) : bounds;
    }

    public int getScrollBarX() {
        return this.hasScrollBar() ? this.getBounds().getMaxX() - 6 : this.getBounds().getMaxX();
    }

    public boolean hasScrollBar() {
        return this.getMaxScrollHeight() > this.getBounds().height;
    }

    public abstract int getMaxScrollHeight();

    public final int getMaxScroll() {
        return Math.max(0, this.getMaxScrollHeight() - this.getBounds().height);
    }

    public final double clamp(double v) {
        return this.clamp(v, 200.0);
    }

    public final double clamp(double v, double clampExtension) {
        return Mth.clamp(v, -clampExtension, (double) this.getMaxScroll() + clampExtension);
    }

    public final void offset(double value, boolean animated) {
        this.scrollTo(this.scrollTarget + value, animated);
    }

    public final void scrollTo(double value, boolean animated) {
        this.scrollTo(value, animated, ClothConfigInitializer.getScrollDuration());
    }

    public final void scrollTo(double value, boolean animated, long duration) {
        this.scrollTarget = this.clamp(value);
        if (animated) {
            this.start = System.currentTimeMillis();
            this.duration = duration;
        } else {
            this.scrollAmount = this.scrollTarget;
        }
    }

    public void updatePosition(float delta) {
        double[] target = new double[] { this.scrollTarget };
        this.scrollAmount = handleScrollingPosition(target, this.scrollAmount, (double) this.getMaxScroll(), delta, (double) this.start, (double) this.duration);
        this.scrollTarget = target[0];
    }

    public static double handleScrollingPosition(double[] target, double scroll, double maxScroll, float delta, double start, double duration) {
        return handleScrollingPosition(target, scroll, maxScroll, delta, start, duration, ClothConfigInitializer.getBounceBackMultiplier(), ClothConfigInitializer.getEasingMethod());
    }

    public static double handleScrollingPosition(double[] target, double scroll, double maxScroll, float delta, double start, double duration, double bounceBackMultiplier, EasingMethod easingMethod) {
        if (bounceBackMultiplier >= 0.0) {
            target[0] = clampExtension(target[0], maxScroll);
            if (target[0] < 0.0) {
                target[0] -= target[0] * (1.0 - bounceBackMultiplier) * (double) delta / 3.0;
            } else if (target[0] > maxScroll) {
                target[0] = (target[0] - maxScroll) * (1.0 - (1.0 - bounceBackMultiplier) * (double) delta / 3.0) + maxScroll;
            }
        } else {
            target[0] = clampExtension(target[0], maxScroll, 0.0);
        }
        return ease(scroll, target[0], Math.min(((double) System.currentTimeMillis() - start) / duration * (double) delta * 3.0, 1.0), easingMethod);
    }

    public static double ease(double start, double end, double amount, EasingMethod easingMethod) {
        return start + (end - start) * easingMethod.apply(amount);
    }

    public static double clampExtension(double value, double maxScroll) {
        return clampExtension(value, maxScroll, 200.0);
    }

    public static double clampExtension(double v, double maxScroll, double clampExtension) {
        return Mth.clamp(v, -clampExtension, maxScroll + clampExtension);
    }

    public void renderScrollBar(GuiGraphics graphics) {
        this.renderScrollBar(graphics, 0, 1.0F, 1.0F);
    }

    public void renderScrollBar(GuiGraphics graphics, int background, float alpha, float scrollBarAlphaOffset) {
        if (this.hasScrollBar()) {
            Rectangle bounds = this.getBounds();
            int maxScroll = this.getMaxScroll();
            int height = bounds.height * bounds.height / this.getMaxScrollHeight();
            height = Mth.clamp(height, 32, bounds.height);
            height = (int) ((double) height - Math.min((double) (this.scrollAmount < 0.0 ? (int) (-this.scrollAmount) : (this.scrollAmount > (double) maxScroll ? (int) this.scrollAmount - maxScroll : 0)), (double) height * 0.95));
            height = Math.max(10, height);
            int minY = Math.min(Math.max((int) this.scrollAmount * (bounds.height - height) / maxScroll + bounds.y, bounds.y), bounds.getMaxY() - height);
            int scrollbarPositionMinX = this.getScrollBarX();
            int scrollbarPositionMaxX = scrollbarPositionMinX + 6;
            boolean hovered = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.ofMouse());
            float bottomC = (hovered ? 0.67F : 0.5F) * scrollBarAlphaOffset;
            float topC = (hovered ? 0.87F : 0.67F) * scrollBarAlphaOffset;
            graphics.fill(scrollbarPositionMinX, bounds.y, scrollbarPositionMaxX, bounds.getMaxY(), background);
            graphics.fill(scrollbarPositionMinX, minY, scrollbarPositionMaxX, minY + height, FastColor.ARGB32.color(Math.round(alpha * 255.0F), Math.round(bottomC * 255.0F), Math.round(bottomC * 255.0F), Math.round(bottomC * 255.0F)));
            graphics.fill(scrollbarPositionMinX, minY, scrollbarPositionMaxX - 1, minY + height - 1, FastColor.ARGB32.color(Math.round(alpha * 255.0F), Math.round(topC * 255.0F), Math.round(topC * 255.0F), Math.round(topC * 255.0F)));
        }
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy) {
        return this.mouseDragged(mouseX, mouseY, button, dx, dy, false, 0.0);
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double dx, double dy, boolean snapToRows, double rowSize) {
        if (button == 0 && this.draggingScrollBar) {
            float height = (float) this.getMaxScrollHeight();
            Rectangle bounds = this.getBounds();
            int actualHeight = bounds.height;
            if (mouseY >= (double) bounds.y && mouseY <= (double) bounds.getMaxY()) {
                double maxScroll = (double) Math.max(1, this.getMaxScroll());
                double int_3 = Mth.clamp((double) (actualHeight * actualHeight) / (double) height, 32.0, (double) (actualHeight - 8));
                double double_6 = Math.max(1.0, maxScroll / ((double) actualHeight - int_3));
                float to = Mth.clamp((float) (this.scrollAmount + dy * double_6), 0.0F, (float) this.getMaxScroll());
                if (snapToRows) {
                    double nearestRow = (double) Math.round((double) to / rowSize) * rowSize;
                    this.scrollTo(nearestRow, false);
                } else {
                    this.scrollTo((double) to, false);
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean updateDraggingState(double mouseX, double mouseY, int button) {
        if (!this.hasScrollBar()) {
            return false;
        } else {
            double height = (double) this.getMaxScroll();
            Rectangle bounds = this.getBounds();
            int actualHeight = bounds.height;
            if (height > (double) actualHeight && mouseY >= (double) bounds.y && mouseY <= (double) bounds.getMaxY()) {
                double scrollbarPositionMinX = (double) this.getScrollBarX();
                if (mouseX >= scrollbarPositionMinX - 1.0 & mouseX <= scrollbarPositionMinX + 8.0) {
                    this.draggingScrollBar = true;
                    return true;
                }
            }
            this.draggingScrollBar = false;
            return false;
        }
    }
}