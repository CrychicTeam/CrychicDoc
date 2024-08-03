package me.shedaniel.clothconfig2.gui.widget;

import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.Tesselator;
import me.shedaniel.clothconfig2.ClothConfigInitializer;
import me.shedaniel.clothconfig2.api.animator.NumberAnimator;
import me.shedaniel.clothconfig2.api.animator.ValueAnimator;
import me.shedaniel.clothconfig2.api.scroll.ScrollingContainer;
import me.shedaniel.math.Rectangle;
import me.shedaniel.math.impl.PointHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FastColor;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class DynamicSmoothScrollingEntryListWidget<E extends DynamicEntryListWidget.Entry<E>> extends DynamicEntryListWidget<E> {

    protected boolean smoothScrolling = true;

    protected final NumberAnimator<Double> scrollAnimator = ValueAnimator.ofDouble();

    public DynamicSmoothScrollingEntryListWidget(Minecraft client, int width, int height, int top, int bottom, ResourceLocation backgroundLocation) {
        super(client, width, height, top, bottom, backgroundLocation);
    }

    public boolean isSmoothScrolling() {
        return this.smoothScrolling;
    }

    public void setSmoothScrolling(boolean smoothScrolling) {
        this.smoothScrolling = smoothScrolling;
    }

    @Override
    public void capYPosition(double scroll) {
        if (!this.smoothScrolling) {
            this.scrollAnimator.setAs(Mth.clamp(scroll, 0.0, (double) this.getMaxScroll()));
        } else {
            this.scrollAnimator.setAs(ScrollingContainer.clampExtension(scroll, (double) this.getMaxScroll()));
        }
        this.scroll = (Double) this.scrollAnimator.value();
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (!this.smoothScrolling) {
            return super.mouseDragged(mouseX, mouseY, button, deltaX, deltaY);
        } else if (this.getFocused() != null && this.m_7282_() && button == 0 && this.getFocused().m_7979_(mouseX, mouseY, button, deltaX, deltaY)) {
            return true;
        } else if (button == 0 && this.scrolling) {
            if (mouseY < (double) this.top) {
                this.capYPosition(0.0);
            } else if (mouseY > (double) this.bottom) {
                this.capYPosition((double) this.getMaxScroll());
            } else {
                double double_5 = (double) Math.max(1, this.getMaxScroll());
                int int_2 = this.bottom - this.top;
                int int_3 = Mth.clamp((int) ((float) (int_2 * int_2) / (float) this.getMaxScrollPosition()), 32, int_2 - 8);
                double double_6 = Math.max(1.0, double_5 / (double) (int_2 - int_3));
                this.capYPosition(Mth.clamp(this.getScroll() + deltaY * double_6, 0.0, (double) this.getMaxScroll()));
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (E entry : this.visibleChildren()) {
            if (entry.m_6050_(mouseX, mouseY, amount)) {
                return true;
            }
        }
        if (!this.smoothScrolling) {
            this.scroll += 16.0 * -amount;
            this.scroll = Mth.clamp(amount, 0.0, (double) this.getMaxScroll());
            return true;
        } else {
            this.offset(ClothConfigInitializer.getScrollStep() * -amount, true);
            return true;
        }
    }

    public void offset(double value, boolean animated) {
        this.scrollTo((Double) this.scrollAnimator.target() + value, animated);
    }

    public void scrollTo(double value, boolean animated) {
        this.scrollTo(value, animated, ClothConfigInitializer.getScrollDuration());
    }

    public void scrollTo(double value, boolean animated, long duration) {
        if (animated) {
            this.scrollAnimator.setTo(value, duration);
        } else {
            this.scrollAnimator.setAs(value);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY, float delta) {
        this.scrollAnimator.setTarget(ScrollingContainer.handleBounceBack((Double) this.scrollAnimator.target(), (double) this.getMaxScroll(), delta));
        this.scrollAnimator.update((double) delta);
        this.scroll = (Double) this.scrollAnimator.value();
        super.render(graphics, mouseX, mouseY, delta);
    }

    @Override
    protected void renderScrollBar(GuiGraphics graphics, Tesselator tessellator, BufferBuilder buffer, int maxScroll, int scrollbarPositionMinX, int scrollbarPositionMaxX) {
        if (!this.smoothScrolling) {
            super.renderScrollBar(graphics, tessellator, buffer, maxScroll, scrollbarPositionMinX, scrollbarPositionMaxX);
        } else if (maxScroll > 0) {
            int height = (this.bottom - this.top) * (this.bottom - this.top) / this.getMaxScrollPosition();
            height = Mth.clamp(height, 32, this.bottom - this.top - 8);
            height = (int) ((double) height - Math.min((double) (this.scroll < 0.0 ? (int) (-this.scroll) : (this.scroll > (double) this.getMaxScroll() ? (int) this.scroll - this.getMaxScroll() : 0)), (double) height * 0.95));
            height = Math.max(10, height);
            int minY = Math.min(Math.max((int) this.getScroll() * (this.bottom - this.top - height) / maxScroll + this.top, this.top), this.bottom - height);
            int bottomc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.ofMouse()) ? 168 : 128;
            int topc = new Rectangle(scrollbarPositionMinX, minY, scrollbarPositionMaxX - scrollbarPositionMinX, height).contains(PointHelper.ofMouse()) ? 222 : 172;
            graphics.fill(scrollbarPositionMinX, this.top, scrollbarPositionMaxX, this.bottom, -16777216);
            graphics.fill(scrollbarPositionMinX, minY, scrollbarPositionMaxX, minY + height, FastColor.ARGB32.color(255, bottomc, bottomc, bottomc));
            graphics.fill(scrollbarPositionMinX, minY, scrollbarPositionMaxX - 1, minY + height - 1, FastColor.ARGB32.color(255, topc, topc, topc));
        }
    }

    public static class Interpolation {

        public static double expoEase(double start, double end, double amount) {
            return start + (end - start) * ClothConfigInitializer.getEasingMethod().apply(amount);
        }
    }

    public static class Precision {

        public static final float FLOAT_EPSILON = 0.001F;

        public static final double DOUBLE_EPSILON = 1.0E-7;

        public static boolean almostEquals(float value1, float value2, float acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }

        public static boolean almostEquals(double value1, double value2, double acceptableDifference) {
            return Math.abs(value1 - value2) <= acceptableDifference;
        }
    }
}