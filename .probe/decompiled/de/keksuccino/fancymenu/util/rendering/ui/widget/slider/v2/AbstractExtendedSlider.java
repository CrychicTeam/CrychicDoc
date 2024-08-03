package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v2;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractSliderButton;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableSlider;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.IExtendedWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.fancymenu.util.resource.RenderableResource;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractExtendedSlider extends AbstractSliderButton implements IExtendedWidget, NavigatableWidget {

    protected static final ResourceLocation SLIDER_LOCATION = new ResourceLocation("textures/gui/slider.png");

    @Nullable
    protected DrawableColor sliderBackgroundColorNormal;

    @Nullable
    protected DrawableColor sliderBackgroundColorHighlighted;

    @Nullable
    protected DrawableColor sliderBorderColorNormal;

    @Nullable
    protected DrawableColor sliderBorderColorHighlighted;

    @Nullable
    protected DrawableColor sliderHandleColorNormal;

    @Nullable
    protected DrawableColor sliderHandleColorHover;

    @Nullable
    protected DrawableColor sliderHandleColorInactive;

    @NotNull
    protected DrawableColor labelColorNormal = DrawableColor.of(new Color(16777215));

    @NotNull
    protected DrawableColor labelColorInactive = DrawableColor.of(new Color(10526880));

    protected boolean labelShadow = true;

    @Nullable
    protected AbstractExtendedSlider.SliderValueUpdateListener sliderValueUpdateListener;

    @NotNull
    protected ConsumingSupplier<AbstractExtendedSlider, Component> labelSupplier = slider -> Component.literal(slider.getValueDisplayText());

    protected boolean focusable = true;

    protected boolean navigatable = true;

    public AbstractExtendedSlider(int x, int y, int width, int height, Component label, double value) {
        super(x, y, width, height, label, value);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(graphics, mouseX, mouseY, partial);
        RenderingUtils.resetShaderColor(graphics);
        this.renderHandle(graphics, mouseX, mouseY, partial);
        RenderingUtils.resetShaderColor(graphics);
        this.renderLabel(graphics, mouseX, mouseY, partial);
        RenderingUtils.resetShaderColor(graphics);
    }

    protected void renderBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        boolean renderVanilla = this.renderColorBackground(graphics, mouseX, mouseY, partial);
        if (renderVanilla) {
            renderVanilla = this.getAsCustomizableSlider().renderSliderBackgroundFancyMenu(graphics, this, this.getAccessor().getCanChangeValueFancyMenu());
        }
        if (renderVanilla) {
            this.renderVanillaBackground(graphics, mouseX, mouseY, partial);
        }
    }

    protected boolean renderColorBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        RenderingUtils.resetShaderColor(graphics);
        if (this.m_93696_() && !this.getAccessor().getCanChangeValueFancyMenu() && this.sliderBackgroundColorHighlighted != null) {
            graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.sliderBackgroundColorHighlighted.getColorInt());
            if (this.sliderBorderColorHighlighted != null) {
                UIBase.renderBorder(graphics, (float) this.m_252754_(), (float) this.m_252907_(), (float) (this.m_252754_() + this.m_5711_()), (float) (this.m_252907_() + this.m_93694_()), 1.0F, this.sliderBorderColorHighlighted.getColorInt(), true, true, true, true);
            }
            return false;
        } else if (this.sliderBackgroundColorNormal != null) {
            graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.sliderBackgroundColorNormal.getColorInt());
            if (this.sliderBorderColorNormal != null) {
                UIBase.renderBorder(graphics, (float) this.m_252754_(), (float) this.m_252907_(), (float) (this.m_252754_() + this.m_5711_()), (float) (this.m_252907_() + this.m_93694_()), 1.0F, this.sliderBorderColorNormal.getColorInt(), true, true, true, true);
            }
            return false;
        } else {
            return true;
        }
    }

    protected void renderVanillaBackground(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(SLIDER_LOCATION, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 20, 4, 200, 20, 0, this.getBackgroundTextureY());
    }

    protected int getBackgroundTextureY() {
        int i = this.m_93696_() && !this.getAccessor().getCanChangeValueFancyMenu() ? 1 : 0;
        return i * 20;
    }

    protected void renderHandle(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        boolean renderVanilla = this.renderColorHandle(graphics, mouseX, mouseY, partial);
        if (renderVanilla) {
            renderVanilla = this.getAsCustomizableWidget().renderCustomBackgroundFancyMenu(this, graphics, this.getHandleX(), this.m_252907_(), this.getHandleWidth(), this.m_93694_());
        }
        if (renderVanilla) {
            this.renderVanillaHandle(graphics, mouseX, mouseY, partial);
        }
    }

    protected boolean renderColorHandle(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        RenderSystem.enableBlend();
        int handleX = this.getHandleX();
        int handleWidth = this.getHandleWidth();
        if (this.f_93623_) {
            if (this.m_198029_()) {
                if (this.sliderHandleColorHover != null) {
                    graphics.fill(handleX, this.m_252907_(), handleX + handleWidth, this.m_252907_() + this.m_93694_(), this.sliderHandleColorHover.getColorInt());
                    return false;
                }
            } else if (this.sliderHandleColorNormal != null) {
                graphics.fill(handleX, this.m_252907_(), handleX + handleWidth, this.m_252907_() + this.m_93694_(), this.sliderHandleColorNormal.getColorInt());
                return false;
            }
        } else if (this.sliderHandleColorInactive != null) {
            graphics.fill(handleX, this.m_252907_(), handleX + handleWidth, this.m_252907_() + this.m_93694_(), this.sliderHandleColorInactive.getColorInt());
            return false;
        }
        return true;
    }

    protected void renderVanillaHandle(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        graphics.blitNineSliced(SLIDER_LOCATION, this.getHandleX(), this.m_252907_(), this.getHandleWidth(), 20, 20, 4, 200, 20, 0, this.getHandleTextureY());
    }

    @Override
    protected int getHandleTextureY() {
        int i = !this.f_93622_ && !this.getAccessor().getCanChangeValueFancyMenu() ? 2 : 3;
        return i * 20;
    }

    protected void renderLabel(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        int textColor = this.f_93623_ ? this.labelColorNormal.getColorInt() : this.labelColorInactive.getColorInt();
        this.renderScrollingLabel(this, graphics, Minecraft.getInstance().font, 2, this.labelShadow, RenderingUtils.replaceAlphaInColor(textColor, this.f_93625_));
    }

    public int getHandleX() {
        return this.m_252754_() + (int) (this.f_93577_ * (double) (this.m_5711_() - this.getHandleWidth()));
    }

    public int getHandleWidth() {
        return 8;
    }

    @Override
    public void updateMessage() {
        Component label = this.labelSupplier.get(this);
        if (label == null) {
            label = Component.empty();
        }
        this.m_93666_(label);
    }

    @Override
    protected void applyValue() {
        if (this.sliderValueUpdateListener != null) {
            this.sliderValueUpdateListener.update(this, this.getValueDisplayText(), this.f_93577_);
        }
    }

    @NotNull
    public abstract String getValueDisplayText();

    public AbstractExtendedSlider setSliderValueUpdateListener(@Nullable AbstractExtendedSlider.SliderValueUpdateListener listener) {
        this.sliderValueUpdateListener = listener;
        this.updateMessage();
        return this;
    }

    @Override
    public void setValue(double value) {
        double d0 = this.f_93577_;
        this.f_93577_ = Mth.clamp(value, 0.0, 1.0);
        if (d0 != this.f_93577_) {
            this.applyValue();
        }
        this.updateMessage();
    }

    public double getValue() {
        return this.f_93577_;
    }

    @Nullable
    public RenderableResource getHandleTextureNormal() {
        return this.getAsCustomizableWidget().getCustomBackgroundNormalFancyMenu();
    }

    public AbstractExtendedSlider setHandleTextureNormal(@Nullable RenderableResource texture) {
        this.getAsCustomizableWidget().setCustomBackgroundNormalFancyMenu(texture);
        return this;
    }

    @Nullable
    public RenderableResource getHandleTextureHover() {
        return this.getAsCustomizableWidget().getCustomBackgroundHoverFancyMenu();
    }

    public AbstractExtendedSlider setHandleTextureHover(@Nullable RenderableResource texture) {
        this.getAsCustomizableWidget().setCustomBackgroundHoverFancyMenu(texture);
        return this;
    }

    @Nullable
    public RenderableResource getHandleTextureInactive() {
        return this.getAsCustomizableWidget().getCustomBackgroundInactiveFancyMenu();
    }

    public AbstractExtendedSlider setHandleTextureInactive(@Nullable RenderableResource texture) {
        this.getAsCustomizableWidget().setCustomBackgroundInactiveFancyMenu(texture);
        return this;
    }

    @Nullable
    public RenderableResource getBackgroundTextureNormal() {
        return this.getAsCustomizableSlider().getCustomSliderBackgroundNormalFancyMenu();
    }

    public AbstractExtendedSlider setBackgroundTextureNormal(@Nullable RenderableResource texture) {
        this.getAsCustomizableSlider().setCustomSliderBackgroundNormalFancyMenu(texture);
        return this;
    }

    @Nullable
    public RenderableResource getBackgroundTextureHighlighted() {
        return this.getAsCustomizableSlider().getCustomSliderBackgroundHighlightedFancyMenu();
    }

    public AbstractExtendedSlider setBackgroundTextureHighlighted(@Nullable RenderableResource texture) {
        this.getAsCustomizableSlider().setCustomSliderBackgroundHighlightedFancyMenu(texture);
        return this;
    }

    @Nullable
    public DrawableColor getSliderBackgroundColorNormal() {
        return this.sliderBackgroundColorNormal;
    }

    public AbstractExtendedSlider setSliderBackgroundColorNormal(@Nullable DrawableColor sliderBackgroundColorNormal) {
        this.sliderBackgroundColorNormal = sliderBackgroundColorNormal;
        return this;
    }

    @Nullable
    public DrawableColor getSliderBackgroundColorHighlighted() {
        return this.sliderBackgroundColorHighlighted;
    }

    public AbstractExtendedSlider setSliderBackgroundColorHighlighted(@Nullable DrawableColor sliderBackgroundColorHighlighted) {
        this.sliderBackgroundColorHighlighted = sliderBackgroundColorHighlighted;
        return this;
    }

    @Nullable
    public DrawableColor getSliderBorderColorNormal() {
        return this.sliderBorderColorNormal;
    }

    public AbstractExtendedSlider setSliderBorderColorNormal(@Nullable DrawableColor sliderBorderColorNormal) {
        this.sliderBorderColorNormal = sliderBorderColorNormal;
        return this;
    }

    @Nullable
    public DrawableColor getSliderBorderColorHighlighted() {
        return this.sliderBorderColorHighlighted;
    }

    public AbstractExtendedSlider setSliderBorderColorHighlighted(@Nullable DrawableColor sliderBorderColorHighlighted) {
        this.sliderBorderColorHighlighted = sliderBorderColorHighlighted;
        return this;
    }

    @Nullable
    public DrawableColor getSliderHandleColorNormal() {
        return this.sliderHandleColorNormal;
    }

    public AbstractExtendedSlider setSliderHandleColorNormal(@Nullable DrawableColor sliderHandleColorNormal) {
        this.sliderHandleColorNormal = sliderHandleColorNormal;
        return this;
    }

    @Nullable
    public DrawableColor getSliderHandleColorHover() {
        return this.sliderHandleColorHover;
    }

    public AbstractExtendedSlider setSliderHandleColorHover(@Nullable DrawableColor sliderHandleColorHover) {
        this.sliderHandleColorHover = sliderHandleColorHover;
        return this;
    }

    @Nullable
    public DrawableColor getSliderHandleColorInactive() {
        return this.sliderHandleColorInactive;
    }

    public AbstractExtendedSlider setSliderHandleColorInactive(@Nullable DrawableColor sliderHandleColorInactive) {
        this.sliderHandleColorInactive = sliderHandleColorInactive;
        return this;
    }

    @NotNull
    public DrawableColor getLabelColorNormal() {
        return this.labelColorNormal;
    }

    public AbstractExtendedSlider setLabelColorNormal(@NotNull DrawableColor labelColorNormal) {
        this.labelColorNormal = labelColorNormal;
        return this;
    }

    @NotNull
    public DrawableColor getLabelColorInactive() {
        return this.labelColorInactive;
    }

    public AbstractExtendedSlider setLabelColorInactive(@NotNull DrawableColor labelColorInactive) {
        this.labelColorInactive = labelColorInactive;
        return this;
    }

    public boolean isLabelShadow() {
        return this.labelShadow;
    }

    public AbstractExtendedSlider setLabelShadow(boolean labelShadow) {
        this.labelShadow = labelShadow;
        return this;
    }

    @NotNull
    public ConsumingSupplier<AbstractExtendedSlider, Component> getLabelSupplier() {
        return this.labelSupplier;
    }

    public AbstractExtendedSlider setLabelSupplier(@NotNull ConsumingSupplier<AbstractExtendedSlider, Component> labelSupplier) {
        this.labelSupplier = labelSupplier;
        return this;
    }

    public IMixinAbstractSliderButton getAccessor() {
        return (IMixinAbstractSliderButton) this;
    }

    public CustomizableSlider getAsCustomizableSlider() {
        return (CustomizableSlider) this;
    }

    public CustomizableWidget getAsCustomizableWidget() {
        return (CustomizableWidget) this;
    }

    @Override
    public boolean isFocusable() {
        return this.focusable;
    }

    @Override
    public void setFocusable(boolean focusable) {
        this.focusable = focusable;
    }

    @Override
    public boolean isNavigatable() {
        return this.navigatable;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        this.navigatable = navigatable;
    }

    @FunctionalInterface
    public interface SliderValueUpdateListener {

        void update(@NotNull AbstractExtendedSlider var1, @NotNull String var2, double var3);
    }
}