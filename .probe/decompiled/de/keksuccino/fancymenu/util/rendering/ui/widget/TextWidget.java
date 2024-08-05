package de.keksuccino.fancymenu.util.rendering.ui.widget;

import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class TextWidget extends AbstractWidget implements UniqueWidget, NavigatableWidget {

    @Nullable
    protected String widgetIdentifier;

    @NotNull
    protected TextWidget.TextAlignment alignment = TextWidget.TextAlignment.LEFT;

    @NotNull
    protected DrawableColor baseColor = DrawableColor.WHITE;

    protected boolean shadow = true;

    @NotNull
    protected Font font;

    @NotNull
    public static TextWidget empty(int x, int y, int width) {
        return new TextWidget(x, y, width, 9, Minecraft.getInstance().font, Component.empty());
    }

    @NotNull
    public static TextWidget of(@NotNull Component text, int x, int y, int width) {
        return new TextWidget(x, y, width, 9, Minecraft.getInstance().font, text);
    }

    @NotNull
    public static TextWidget of(@NotNull String text, int x, int y, int width) {
        return of(Component.literal(text), x, y, width);
    }

    public TextWidget(int x, int y, int width, int height, @NotNull Font font, @NotNull Component text) {
        super(x, y, width, height, text);
        this.font = font;
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        int textWidth = this.getTextWidth();
        int textX = this.m_252754_();
        int textY = this.m_252907_();
        if (this.alignment == TextWidget.TextAlignment.CENTER) {
            textX = this.m_252754_() + this.m_5711_() / 2 - textWidth / 2;
        }
        if (this.alignment == TextWidget.TextAlignment.RIGHT) {
            textX = this.m_252754_() + this.m_5711_() - textWidth;
        }
        RenderingUtils.resetShaderColor(graphics);
        graphics.drawString(this.font, this.m_6035_(), textX, textY, this.baseColor.getColorInt(), this.shadow);
        RenderingUtils.resetShaderColor(graphics);
    }

    public int getTextWidth() {
        return this.font.width(this.m_6035_().getVisualOrderText());
    }

    @NotNull
    public TextWidget.TextAlignment getTextAlignment() {
        return this.alignment;
    }

    public TextWidget setTextAlignment(@NotNull TextWidget.TextAlignment alignment) {
        this.alignment = alignment;
        return this;
    }

    @NotNull
    public DrawableColor getBaseColor() {
        return this.baseColor;
    }

    public TextWidget setBaseColor(@NotNull DrawableColor baseColor) {
        this.baseColor = baseColor;
        return this;
    }

    public boolean isShadowEnabled() {
        return this.shadow;
    }

    public TextWidget setShadowEnabled(boolean enabled) {
        this.shadow = enabled;
        return this;
    }

    @NotNull
    public Font getFont() {
        return this.font;
    }

    public TextWidget setFont(@NotNull Font font) {
        this.font = font;
        return this;
    }

    public TextWidget centerWidget(@NotNull Screen parent) {
        this.m_252865_(parent.width / 2 - this.m_5711_() / 2);
        return this;
    }

    public TextWidget setWidgetIdentifierFancyMenu(@Nullable String identifier) {
        this.widgetIdentifier = identifier;
        return this;
    }

    @Nullable
    @Override
    public String getWidgetIdentifierFancyMenu() {
        return this.widgetIdentifier;
    }

    @Override
    protected void updateWidgetNarration(@NotNull NarrationElementOutput var1) {
    }

    @Override
    public boolean isFocusable() {
        return false;
    }

    @Override
    public void setFocusable(boolean focusable) {
        throw new RuntimeException("TextWidgets are not focusable!");
    }

    @Override
    public boolean isNavigatable() {
        return false;
    }

    @Override
    public void setNavigatable(boolean navigatable) {
        throw new RuntimeException("TextWidgets are not navigatable!");
    }

    @Override
    public void playDownSound(@NotNull SoundManager $$0) {
    }

    public static enum TextAlignment {

        LEFT, RIGHT, CENTER
    }
}