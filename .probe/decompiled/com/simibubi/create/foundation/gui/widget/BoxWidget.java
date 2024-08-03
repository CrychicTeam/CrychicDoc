package com.simibubi.create.foundation.gui.widget;

import com.simibubi.create.foundation.gui.Theme;
import com.simibubi.create.foundation.gui.UIRenderHelper;
import com.simibubi.create.foundation.gui.element.BoxElement;
import com.simibubi.create.foundation.gui.element.DelegatedStencilElement;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.utility.Color;
import com.simibubi.create.foundation.utility.Couple;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.function.Function;
import net.minecraft.client.gui.GuiGraphics;

public class BoxWidget extends ElementWidget {

    public static final Function<BoxWidget, DelegatedStencilElement.ElementRenderer> gradientFactory = box -> (ms, w, h, alpha) -> UIRenderHelper.angledGradient(ms, 90.0F, w / 2, -2, w + 4, h + 4, box.gradientColor1, box.gradientColor2);

    protected BoxElement box;

    protected Color customBorderTop;

    protected Color customBorderBot;

    protected Color customBackground;

    protected boolean animateColors = true;

    protected LerpedFloat colorAnimation = LerpedFloat.linear();

    protected Color gradientColor1;

    protected Color gradientColor2;

    private Color previousColor1;

    private Color previousColor2;

    private Color colorTarget1 = Theme.c(this.getIdleTheme(), true).copy();

    private Color colorTarget2 = Theme.c(this.getIdleTheme(), false).copy();

    public BoxWidget() {
        this(0, 0);
    }

    public BoxWidget(int x, int y) {
        this(x, y, 16, 16);
    }

    public BoxWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
        this.box = new BoxElement().<RenderElement>at((float) x, (float) y).withBounds(width, height);
        this.gradientColor1 = this.colorTarget1;
        this.gradientColor2 = this.colorTarget2;
    }

    public <T extends BoxWidget> T withBounds(int width, int height) {
        this.f_93618_ = width;
        this.f_93619_ = height;
        return (T) this;
    }

    public <T extends BoxWidget> T withBorderColors(Couple<Color> colors) {
        this.customBorderTop = colors.getFirst();
        this.customBorderBot = colors.getSecond();
        this.updateColorsFromState();
        return (T) this;
    }

    public <T extends BoxWidget> T withBorderColors(Color top, Color bot) {
        this.customBorderTop = top;
        this.customBorderBot = bot;
        this.updateColorsFromState();
        return (T) this;
    }

    public <T extends BoxWidget> T withCustomBackground(Color color) {
        this.customBackground = color;
        return (T) this;
    }

    public <T extends BoxWidget> T animateColors(boolean b) {
        this.animateColors = b;
        return (T) this;
    }

    @Override
    public void tick() {
        super.tick();
        this.colorAnimation.tickChaser();
    }

    @Override
    public void onClick(double x, double y) {
        super.m_5716_(x, y);
        this.gradientColor1 = Theme.c(this.getClickTheme(), true);
        this.gradientColor2 = Theme.c(this.getClickTheme(), false);
        this.startGradientAnimation(this.getColorForState(true), this.getColorForState(false), true, 0.15);
    }

    @Override
    protected void beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.beforeRender(graphics, mouseX, mouseY, partialTicks);
        if (this.f_93622_ != this.wasHovered) {
            this.startGradientAnimation(this.getColorForState(true), this.getColorForState(false), this.f_93622_);
        }
        if (this.colorAnimation.settled()) {
            this.gradientColor1 = this.colorTarget1;
            this.gradientColor2 = this.colorTarget2;
        } else {
            float animationValue = 1.0F - Math.abs(this.colorAnimation.getValue(partialTicks));
            this.gradientColor1 = Color.mixColors(this.previousColor1, this.colorTarget1, animationValue);
            this.gradientColor2 = Color.mixColors(this.previousColor2, this.colorTarget2, animationValue);
        }
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        float fadeValue = this.fade.getValue(partialTicks);
        if (!(fadeValue < 0.1F)) {
            this.box.withAlpha(fadeValue);
            this.box.<BoxElement>withBackground(this.customBackground != null ? this.customBackground : Theme.c(Theme.Key.PONDER_BACKGROUND_TRANSPARENT)).<BoxElement>gradientBorder(this.gradientColor1, this.gradientColor2).<RenderElement>at((float) this.m_252754_(), (float) this.m_252907_(), this.z).<RenderElement>withBounds(this.f_93618_, this.f_93619_).render(graphics);
            super.doRender(graphics, mouseX, mouseY, partialTicks);
            this.wasHovered = this.f_93622_;
        }
    }

    @Override
    public boolean isMouseOver(double mX, double mY) {
        if (this.f_93623_ && this.f_93624_) {
            float padX = 2.0F + this.paddingX;
            float padY = 2.0F + this.paddingY;
            return (double) ((float) this.m_252754_() - padX) <= mX && (double) ((float) this.m_252907_() - padY) <= mY && mX < (double) ((float) this.m_252754_() + padX + (float) this.f_93618_) && mY < (double) ((float) this.m_252907_() + padY + (float) this.f_93619_);
        } else {
            return false;
        }
    }

    @Override
    protected boolean clicked(double pMouseX, double pMouseY) {
        return this.f_93623_ && this.f_93624_ ? this.isMouseOver(pMouseX, pMouseY) : false;
    }

    public BoxElement getBox() {
        return this.box;
    }

    public void updateColorsFromState() {
        this.colorTarget1 = this.getColorForState(true);
        this.colorTarget2 = this.getColorForState(false);
    }

    public void animateGradientFromState() {
        this.startGradientAnimation(this.getColorForState(true), this.getColorForState(false), true);
    }

    private void startGradientAnimation(Color c1, Color c2, boolean positive, double expSpeed) {
        if (this.animateColors) {
            this.colorAnimation.startWithValue(positive ? 1.0 : -1.0);
            this.colorAnimation.chase(0.0, expSpeed, LerpedFloat.Chaser.EXP);
            this.colorAnimation.tickChaser();
            this.previousColor1 = this.gradientColor1;
            this.previousColor2 = this.gradientColor2;
            this.colorTarget1 = c1;
            this.colorTarget2 = c2;
        }
    }

    private void startGradientAnimation(Color c1, Color c2, boolean positive) {
        this.startGradientAnimation(c1, c2, positive, 0.6);
    }

    private Color getColorForState(boolean first) {
        if (!this.f_93623_) {
            return Theme.p(this.getDisabledTheme()).get(first);
        } else if (this.f_93622_) {
            if (first) {
                return this.customBorderTop != null ? this.customBorderTop.darker() : Theme.c(this.getHoverTheme(), true);
            } else {
                return this.customBorderBot != null ? this.customBorderBot.darker() : Theme.c(this.getHoverTheme(), false);
            }
        } else if (first) {
            return this.customBorderTop != null ? this.customBorderTop : Theme.c(this.getIdleTheme(), true);
        } else {
            return this.customBorderBot != null ? this.customBorderBot : Theme.c(this.getIdleTheme(), false);
        }
    }

    public Theme.Key getDisabledTheme() {
        return Theme.Key.BUTTON_DISABLE;
    }

    public Theme.Key getIdleTheme() {
        return Theme.Key.BUTTON_IDLE;
    }

    public Theme.Key getHoverTheme() {
        return Theme.Key.BUTTON_HOVER;
    }

    public Theme.Key getClickTheme() {
        return Theme.Key.BUTTON_CLICK;
    }
}