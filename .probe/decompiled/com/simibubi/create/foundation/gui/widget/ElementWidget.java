package com.simibubi.create.foundation.gui.widget;

import com.mojang.blaze3d.vertex.PoseStack;
import com.simibubi.create.foundation.gui.element.RenderElement;
import com.simibubi.create.foundation.gui.element.ScreenElement;
import com.simibubi.create.foundation.utility.animation.LerpedFloat;
import java.util.function.Consumer;
import java.util.function.UnaryOperator;
import net.minecraft.client.gui.GuiGraphics;

public class ElementWidget extends AbstractSimiWidget {

    protected RenderElement element = RenderElement.EMPTY;

    protected boolean usesFade = false;

    protected int fadeModX;

    protected int fadeModY;

    protected LerpedFloat fade = LerpedFloat.linear().startWithValue(1.0);

    protected boolean rescaleElement = false;

    protected float rescaleSizeX;

    protected float rescaleSizeY;

    protected float paddingX = 0.0F;

    protected float paddingY = 0.0F;

    public ElementWidget(int x, int y) {
        super(x, y);
    }

    public ElementWidget(int x, int y, int width, int height) {
        super(x, y, width, height);
    }

    public <T extends ElementWidget> T showingElement(RenderElement element) {
        this.element = element;
        return (T) this;
    }

    public <T extends ElementWidget> T showing(ScreenElement renderable) {
        return this.showingElement(RenderElement.of(renderable));
    }

    public <T extends ElementWidget> T modifyElement(Consumer<RenderElement> consumer) {
        if (this.element != null) {
            consumer.accept(this.element);
        }
        return (T) this;
    }

    public <T extends ElementWidget> T mapElement(UnaryOperator<RenderElement> function) {
        if (this.element != null) {
            this.element = (RenderElement) function.apply(this.element);
        }
        return (T) this;
    }

    public <T extends ElementWidget> T withPadding(float paddingX, float paddingY) {
        this.paddingX = paddingX;
        this.paddingY = paddingY;
        return (T) this;
    }

    public <T extends ElementWidget> T enableFade(int fadeModifierX, int fadeModifierY) {
        this.fade.startWithValue(0.0);
        this.usesFade = true;
        this.fadeModX = fadeModifierX;
        this.fadeModY = fadeModifierY;
        return (T) this;
    }

    public <T extends ElementWidget> T disableFade() {
        this.fade.startWithValue(1.0);
        this.usesFade = false;
        return (T) this;
    }

    public LerpedFloat fade() {
        return this.fade;
    }

    public <T extends ElementWidget> T fade(float target) {
        this.fade.chase((double) target, 0.1, LerpedFloat.Chaser.EXP);
        return (T) this;
    }

    @Deprecated
    public <T extends ElementWidget> T rescaleElement(float rescaleSizeX, float rescaleSizeY) {
        this.rescaleElement = true;
        this.rescaleSizeX = rescaleSizeX;
        this.rescaleSizeY = rescaleSizeY;
        return (T) this;
    }

    public <T extends ElementWidget> T disableRescale() {
        this.rescaleElement = false;
        return (T) this;
    }

    @Override
    public void tick() {
        super.tick();
        this.fade.tickChaser();
    }

    @Override
    protected void beforeRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        super.beforeRender(graphics, mouseX, mouseY, partialTicks);
        this.f_93622_ = this.m_5953_((double) mouseX, (double) mouseY);
        float fadeValue = this.fade.getValue(partialTicks);
        this.element.withAlpha(fadeValue);
        if (fadeValue < 1.0F) {
            graphics.pose().translate((1.0F - fadeValue) * (float) this.fadeModX, (1.0F - fadeValue) * (float) this.fadeModY, 0.0F);
        }
    }

    @Override
    public void doRender(GuiGraphics graphics, int mouseX, int mouseY, float partialTicks) {
        PoseStack ms = graphics.pose();
        ms.pushPose();
        ms.translate((float) this.m_252754_() + this.paddingX, (float) this.m_252907_() + this.paddingY, this.z);
        float innerWidth = (float) this.f_93618_ - 2.0F * this.paddingX;
        float innerHeight = (float) this.f_93619_ - 2.0F * this.paddingY;
        float eX = this.element.getX();
        float eY = this.element.getY();
        if (this.rescaleElement) {
            float xScale = innerWidth / this.rescaleSizeX;
            float yScale = innerHeight / this.rescaleSizeY;
            ms.scale(xScale, yScale, 1.0F);
            this.element.at(eX / xScale, eY / yScale);
            innerWidth /= xScale;
            innerHeight /= yScale;
        }
        this.element.<RenderElement>withBounds((int) innerWidth, (int) innerHeight).render(graphics);
        ms.popPose();
        if (this.rescaleElement) {
            this.element.at(eX, eY);
        }
    }

    public RenderElement getRenderElement() {
        return this.element;
    }
}