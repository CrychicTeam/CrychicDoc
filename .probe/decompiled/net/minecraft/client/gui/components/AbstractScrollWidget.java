package net.minecraft.client.gui.components;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;

public abstract class AbstractScrollWidget extends AbstractWidget implements Renderable, GuiEventListener {

    private static final int BORDER_COLOR_FOCUSED = -1;

    private static final int BORDER_COLOR = -6250336;

    private static final int BACKGROUND_COLOR = -16777216;

    private static final int INNER_PADDING = 4;

    private double scrollAmount;

    private boolean scrolling;

    public AbstractScrollWidget(int int0, int int1, int int2, int int3, Component component4) {
        super(int0, int1, int2, int3, component4);
    }

    @Override
    public boolean mouseClicked(double double0, double double1, int int2) {
        if (!this.f_93624_) {
            return false;
        } else {
            boolean $$3 = this.withinContentAreaPoint(double0, double1);
            boolean $$4 = this.scrollbarVisible() && double0 >= (double) (this.m_252754_() + this.f_93618_) && double0 <= (double) (this.m_252754_() + this.f_93618_ + 8) && double1 >= (double) this.m_252907_() && double1 < (double) (this.m_252907_() + this.f_93619_);
            if ($$4 && int2 == 0) {
                this.scrolling = true;
                return true;
            } else {
                return $$3 || $$4;
            }
        }
    }

    @Override
    public boolean mouseReleased(double double0, double double1, int int2) {
        if (int2 == 0) {
            this.scrolling = false;
        }
        return super.mouseReleased(double0, double1, int2);
    }

    @Override
    public boolean mouseDragged(double double0, double double1, int int2, double double3, double double4) {
        if (this.f_93624_ && this.m_93696_() && this.scrolling) {
            if (double1 < (double) this.m_252907_()) {
                this.setScrollAmount(0.0);
            } else if (double1 > (double) (this.m_252907_() + this.f_93619_)) {
                this.setScrollAmount((double) this.getMaxScrollAmount());
            } else {
                int $$5 = this.getScrollBarHeight();
                double $$6 = (double) Math.max(1, this.getMaxScrollAmount() / (this.f_93619_ - $$5));
                this.setScrollAmount(this.scrollAmount + double4 * $$6);
            }
            return true;
        } else {
            return false;
        }
    }

    @Override
    public boolean mouseScrolled(double double0, double double1, double double2) {
        if (!this.f_93624_) {
            return false;
        } else {
            this.setScrollAmount(this.scrollAmount - double2 * this.scrollRate());
            return true;
        }
    }

    @Override
    public boolean keyPressed(int int0, int int1, int int2) {
        boolean $$3 = int0 == 265;
        boolean $$4 = int0 == 264;
        if ($$3 || $$4) {
            double $$5 = this.scrollAmount;
            this.setScrollAmount(this.scrollAmount + (double) ($$3 ? -1 : 1) * this.scrollRate());
            if ($$5 != this.scrollAmount) {
                return true;
            }
        }
        return super.m_7933_(int0, int1, int2);
    }

    @Override
    public void renderWidget(GuiGraphics guiGraphics0, int int1, int int2, float float3) {
        if (this.f_93624_) {
            this.renderBackground(guiGraphics0);
            guiGraphics0.enableScissor(this.m_252754_() + 1, this.m_252907_() + 1, this.m_252754_() + this.f_93618_ - 1, this.m_252907_() + this.f_93619_ - 1);
            guiGraphics0.pose().pushPose();
            guiGraphics0.pose().translate(0.0, -this.scrollAmount, 0.0);
            this.renderContents(guiGraphics0, int1, int2, float3);
            guiGraphics0.pose().popPose();
            guiGraphics0.disableScissor();
            this.renderDecorations(guiGraphics0);
        }
    }

    private int getScrollBarHeight() {
        return Mth.clamp((int) ((float) (this.f_93619_ * this.f_93619_) / (float) this.getContentHeight()), 32, this.f_93619_);
    }

    protected void renderDecorations(GuiGraphics guiGraphics0) {
        if (this.scrollbarVisible()) {
            this.renderScrollBar(guiGraphics0);
        }
    }

    protected int innerPadding() {
        return 4;
    }

    protected int totalInnerPadding() {
        return this.innerPadding() * 2;
    }

    protected double scrollAmount() {
        return this.scrollAmount;
    }

    protected void setScrollAmount(double double0) {
        this.scrollAmount = Mth.clamp(double0, 0.0, (double) this.getMaxScrollAmount());
    }

    protected int getMaxScrollAmount() {
        return Math.max(0, this.getContentHeight() - (this.f_93619_ - 4));
    }

    private int getContentHeight() {
        return this.getInnerHeight() + 4;
    }

    protected void renderBackground(GuiGraphics guiGraphics0) {
        this.renderBorder(guiGraphics0, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_());
    }

    protected void renderBorder(GuiGraphics guiGraphics0, int int1, int int2, int int3, int int4) {
        int $$5 = this.m_93696_() ? -1 : -6250336;
        guiGraphics0.fill(int1, int2, int1 + int3, int2 + int4, $$5);
        guiGraphics0.fill(int1 + 1, int2 + 1, int1 + int3 - 1, int2 + int4 - 1, -16777216);
    }

    private void renderScrollBar(GuiGraphics guiGraphics0) {
        int $$1 = this.getScrollBarHeight();
        int $$2 = this.m_252754_() + this.f_93618_;
        int $$3 = this.m_252754_() + this.f_93618_ + 8;
        int $$4 = Math.max(this.m_252907_(), (int) this.scrollAmount * (this.f_93619_ - $$1) / this.getMaxScrollAmount() + this.m_252907_());
        int $$5 = $$4 + $$1;
        guiGraphics0.fill($$2, $$4, $$3, $$5, -8355712);
        guiGraphics0.fill($$2, $$4, $$3 - 1, $$5 - 1, -4144960);
    }

    protected boolean withinContentAreaTopBottom(int int0, int int1) {
        return (double) int1 - this.scrollAmount >= (double) this.m_252907_() && (double) int0 - this.scrollAmount <= (double) (this.m_252907_() + this.f_93619_);
    }

    protected boolean withinContentAreaPoint(double double0, double double1) {
        return double0 >= (double) this.m_252754_() && double0 < (double) (this.m_252754_() + this.f_93618_) && double1 >= (double) this.m_252907_() && double1 < (double) (this.m_252907_() + this.f_93619_);
    }

    protected boolean scrollbarVisible() {
        return this.getInnerHeight() > this.m_93694_();
    }

    protected abstract int getInnerHeight();

    protected abstract double scrollRate();

    protected abstract void renderContents(GuiGraphics var1, int var2, int var3, float var4);
}