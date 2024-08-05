package de.keksuccino.fancymenu.util.rendering.ui.widget.slider.v1;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractSliderButton;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueWidget;
import de.keksuccino.konkrete.input.MouseInput;
import java.awt.Color;
import java.util.function.Consumer;
import net.minecraft.Util;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@Deprecated
public abstract class ExtendedSliderButton extends AbstractSliderButton implements UniqueWidget, NavigatableWidget {

    protected static final ResourceLocation SLIDER_LOCATION = new ResourceLocation("textures/gui/slider.png");

    protected static boolean leftDownGlobal = false;

    public boolean handleClick;

    public boolean enableRightClick = false;

    public boolean ignoreBlockedInput = false;

    public boolean ignoreGlobalLeftMouseDown = false;

    protected String messagePrefix = null;

    protected String messageSuffix = null;

    protected Consumer<ExtendedSliderButton> applyValueCallback;

    @Nullable
    protected DrawableColor backgroundColor = null;

    @Nullable
    protected DrawableColor borderColor = null;

    @Nullable
    protected DrawableColor handleColorNormal = null;

    @Nullable
    protected DrawableColor handleColorHover = null;

    @NotNull
    protected DrawableColor labelColorNormal = DrawableColor.of(new Color(16777215));

    @NotNull
    protected DrawableColor labelColorInactive = DrawableColor.of(new Color(10526880));

    protected boolean labelShadow = true;

    @Nullable
    protected String identifier;

    protected boolean focusable = true;

    protected boolean navigatable = true;

    protected boolean leftDownNotHovered = false;

    protected boolean leftDownThis = false;

    @Deprecated
    public ExtendedSliderButton(int x, int y, int width, int height, boolean handleClick, double value, Consumer<ExtendedSliderButton> applyValueCallback) {
        super(x, y, width, height, CommonComponents.EMPTY, value);
        this.handleClick = handleClick;
        this.applyValueCallback = applyValueCallback;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(graphics);
        this.renderHandle(graphics);
        int labelColorInt = this.f_93623_ ? this.labelColorNormal.getColorInt() : this.labelColorInactive.getColorInt();
        this.renderScrollingLabel(graphics, Minecraft.getInstance().font, 2, labelColorInt | Mth.ceil(this.f_93625_ * 255.0F) << 24);
        RenderingUtils.resetShaderColor(graphics);
    }

    protected void renderHandle(@NotNull GuiGraphics graphics) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        int handleX = this.m_252754_() + (int) (this.f_93577_ * (double) (this.f_93618_ - 8));
        DrawableColor c = this.getHandleRenderColor();
        if (c == null) {
            graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
            graphics.blitNineSliced(SLIDER_LOCATION, handleX, this.m_252907_(), 8, 20, 20, 4, 200, 20, 0, this.getHandleTextureY());
        } else {
            graphics.fill(handleX, this.m_252907_(), handleX + 8, this.m_252907_() + this.m_93694_(), RenderingUtils.replaceAlphaInColor(c.getColorInt(), this.f_93625_));
        }
        RenderingUtils.resetShaderColor(graphics);
    }

    @Nullable
    protected DrawableColor getHandleRenderColor() {
        return this.f_93622_ && this.handleColorHover != null ? this.handleColorHover : this.handleColorNormal;
    }

    protected void renderBackground(@NotNull GuiGraphics graphics) {
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();
        if (this.backgroundColor == null) {
            graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
            graphics.blitNineSliced(SLIDER_LOCATION, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 20, 4, 200, 20, 0, this.getTextureY());
        } else {
            int borderOffset = this.borderColor != null ? 1 : 0;
            graphics.fill(this.m_252754_() + borderOffset, this.m_252907_() + borderOffset, this.m_252754_() + this.m_5711_() - borderOffset, this.m_252907_() + this.m_93694_() - borderOffset, RenderingUtils.replaceAlphaInColor(this.backgroundColor.getColorInt(), this.f_93625_));
            if (this.borderColor != null) {
                UIBase.renderBorder(graphics, this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), 1, this.borderColor, true, true, true, true);
            }
        }
        RenderingUtils.resetShaderColor(graphics);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        if (this.f_93624_) {
            this.f_93622_ = mouseX >= this.m_252754_() && mouseY >= this.m_252907_() && mouseX < this.m_252754_() + this.f_93618_ && mouseY < this.m_252907_() + this.f_93619_;
            if (!this.m_198029_() && MouseInput.isLeftMouseDown()) {
                this.leftDownNotHovered = true;
            }
            if (!MouseInput.isLeftMouseDown()) {
                this.leftDownNotHovered = false;
            }
            if (this.handleClick) {
                if (this.m_198029_() && (MouseInput.isLeftMouseDown() || this.enableRightClick && MouseInput.isRightMouseDown()) && (!leftDownGlobal || this.ignoreGlobalLeftMouseDown) && !this.leftDownNotHovered && !this.isInputBlocked() && this.f_93623_ && this.f_93624_ && !this.leftDownThis) {
                    this.onClick((double) mouseX, (double) mouseY);
                    leftDownGlobal = true;
                    this.leftDownThis = true;
                }
                if (!MouseInput.isLeftMouseDown() && (!MouseInput.isRightMouseDown() || !this.enableRightClick)) {
                    leftDownGlobal = false;
                    if (this.leftDownThis) {
                        this.onRelease((double) mouseX, (double) mouseY);
                    }
                    this.leftDownThis = false;
                }
                if (this.leftDownThis) {
                    this.onDrag((double) mouseX, (double) mouseY, 0.0, 0.0);
                }
            }
        }
        super.m_88315_(graphics, mouseX, mouseY, partial);
    }

    protected void renderScrollingLabel(@NotNull GuiGraphics graphics, @NotNull Font font, int spaceLeftRight, int textColor) {
        int xMin = this.m_252754_() + spaceLeftRight;
        int xMax = this.m_252754_() + this.m_5711_() - spaceLeftRight;
        this.renderScrollingLabelInternal(graphics, font, this.m_6035_(), xMin, this.m_252907_(), xMax, this.m_252907_() + this.m_93694_(), textColor);
    }

    protected void renderScrollingLabelInternal(@NotNull GuiGraphics graphics, Font font, @NotNull Component text, int xMin, int yMin, int xMax, int yMax, int textColor) {
        int textWidth = font.width(text);
        int textPosY = (yMin + yMax - 9) / 2 + 1;
        int maxTextWidth = xMax - xMin;
        if (textWidth > maxTextWidth) {
            int diffTextWidth = textWidth - maxTextWidth;
            double scrollTime = (double) Util.getMillis() / 1000.0;
            double $$13 = Math.max((double) diffTextWidth * 0.5, 3.0);
            double $$14 = Math.sin((Math.PI / 2) * Math.cos((Math.PI * 2) * scrollTime / $$13)) / 2.0 + 0.5;
            double textPosX = Mth.lerp($$14, 0.0, (double) diffTextWidth);
            graphics.enableScissor(xMin, yMin, xMax, yMax);
            graphics.drawString(font, text, xMin - (int) textPosX, textPosY, textColor, this.labelShadow);
            graphics.disableScissor();
        } else {
            graphics.drawString(font, text, (int) ((float) (xMin + xMax) / 2.0F - (float) font.width(text) / 2.0F), textPosY, textColor, this.labelShadow);
        }
    }

    @Nullable
    public DrawableColor getBackgroundColor() {
        return this.backgroundColor;
    }

    public void setBackgroundColor(@Nullable DrawableColor backgroundColor) {
        this.backgroundColor = backgroundColor;
    }

    @Nullable
    public DrawableColor getBorderColor() {
        return this.borderColor;
    }

    public void setBorderColor(@Nullable DrawableColor borderColor) {
        this.borderColor = borderColor;
    }

    @Nullable
    public DrawableColor getHandleColorNormal() {
        return this.handleColorNormal;
    }

    public void setHandleColorNormal(@Nullable DrawableColor handleColorNormal) {
        this.handleColorNormal = handleColorNormal;
    }

    @Nullable
    public DrawableColor getHandleColorHover() {
        return this.handleColorHover;
    }

    public void setHandleColorHover(@Nullable DrawableColor handleColorHover) {
        this.handleColorHover = handleColorHover;
    }

    @NotNull
    public DrawableColor getLabelColorNormal() {
        return this.labelColorNormal;
    }

    public void setLabelColorNormal(@NotNull DrawableColor labelColorNormal) {
        this.labelColorNormal = labelColorNormal;
    }

    @NotNull
    public DrawableColor getLabelColorInactive() {
        return this.labelColorInactive;
    }

    public void setLabelColorInactive(@NotNull DrawableColor labelColorInactive) {
        this.labelColorInactive = labelColorInactive;
    }

    public boolean isLabelShadow() {
        return this.labelShadow;
    }

    public void setLabelShadow(boolean labelShadow) {
        this.labelShadow = labelShadow;
    }

    public boolean canChangeValue() {
        return this.getAccessor().getCanChangeValueFancyMenu();
    }

    public IMixinAbstractSliderButton getAccessor() {
        return (IMixinAbstractSliderButton) this;
    }

    @Override
    protected int getTextureY() {
        int $$0 = this.isFocused() && !this.canChangeValue() ? 1 : 0;
        return $$0 * 20;
    }

    @Override
    protected int getHandleTextureY() {
        int $$0 = !this.f_93622_ && !this.canChangeValue() ? 2 : 3;
        return $$0 * 20;
    }

    @Override
    protected void onDrag(double mouseX, double mouseY, double d1, double d2) {
        super.onDrag(mouseX, mouseY, d1, d2);
    }

    @Override
    public void onClick(double mouseX, double mouseY) {
        super.onClick(mouseX, mouseY);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
    }

    @Override
    protected void applyValue() {
        if (this.applyValueCallback != null) {
            this.applyValueCallback.accept(this);
        }
    }

    @Override
    public void updateMessage() {
        String s = "";
        if (this.messagePrefix != null) {
            s = s + this.messagePrefix;
        }
        s = s + this.getSliderMessageWithoutPrefixSuffix();
        if (this.messageSuffix != null) {
            s = s + this.messageSuffix;
        }
        this.m_93666_(Component.literal(s));
    }

    public abstract String getSliderMessageWithoutPrefixSuffix();

    public void setLabelPrefix(String prefix) {
        this.messagePrefix = prefix;
        this.updateMessage();
    }

    public void setLabelSuffix(String suffix) {
        this.messageSuffix = suffix;
        this.updateMessage();
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

    protected boolean isInputBlocked() {
        return this.ignoreBlockedInput ? false : MouseInput.isVanillaInputBlocked();
    }

    @Nullable
    @Override
    public String getWidgetIdentifierFancyMenu() {
        return this.identifier;
    }

    public ExtendedSliderButton setWidgetIdentifierFancyMenu(@Nullable String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Override
    public void setFocused(boolean focused) {
        if (!this.focusable) {
            super.setFocused(false);
        } else {
            super.setFocused(focused);
        }
    }

    @Override
    public boolean isFocused() {
        return !this.focusable ? false : super.m_93696_();
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
}