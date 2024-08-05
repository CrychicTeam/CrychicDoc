package de.keksuccino.fancymenu.util.rendering.ui.widget.button;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinButton;
import de.keksuccino.fancymenu.util.ConsumingSupplier;
import de.keksuccino.fancymenu.util.rendering.DrawableColor;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.rendering.ui.UIBase;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.Tooltip;
import de.keksuccino.fancymenu.util.rendering.ui.tooltip.TooltipHandler;
import de.keksuccino.fancymenu.util.rendering.ui.widget.CustomizableWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.IExtendedWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.NavigatableWidget;
import de.keksuccino.fancymenu.util.rendering.ui.widget.UniqueWidget;
import de.keksuccino.fancymenu.util.resource.RenderableResource;
import java.awt.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ExtendedButton extends Button implements IExtendedWidget, UniqueWidget, NavigatableWidget {

    private static final Logger LOGGER = LogManager.getLogger();

    protected final Minecraft mc = Minecraft.getInstance();

    protected boolean enableLabel = true;

    protected DrawableColor labelBaseColorNormal = DrawableColor.of(new Color(16777215));

    protected DrawableColor labelBaseColorInactive = DrawableColor.of(new Color(10526880));

    protected boolean labelShadow = true;

    @NotNull
    protected ConsumingSupplier<ExtendedButton, Component> labelSupplier = consumes -> Component.empty();

    protected ConsumingSupplier<ExtendedButton, Tooltip> tooltipSupplier = null;

    protected boolean forceDefaultTooltipStyle = false;

    @Nullable
    protected DrawableColor backgroundColorNormal;

    @Nullable
    protected DrawableColor backgroundColorHover;

    @Nullable
    protected DrawableColor backgroundColorInactive;

    @Nullable
    protected DrawableColor borderColorNormal;

    @Nullable
    protected DrawableColor borderColorHover;

    @Nullable
    protected DrawableColor borderColorInactive;

    @Nullable
    protected ConsumingSupplier<ExtendedButton, Boolean> activeSupplier;

    protected boolean focusable = true;

    protected boolean navigatable = true;

    @Nullable
    protected String identifier;

    public ExtendedButton(int x, int y, int width, int height, @NotNull String label, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, Component.literal(""), onPress, f_252438_);
        this.setLabel(Component.literal(label));
    }

    public ExtendedButton(int x, int y, int width, int height, @NotNull String label, @NotNull Button.OnPress onPress, Button.CreateNarration narration) {
        super(x, y, width, height, Component.literal(""), onPress, narration);
        this.setLabel(Component.literal(label));
    }

    public ExtendedButton(int x, int y, int width, int height, @NotNull Component label, @NotNull Button.OnPress onPress) {
        super(x, y, width, height, Component.literal(""), onPress, f_252438_);
        this.setLabel(label);
    }

    public ExtendedButton(int x, int y, int width, int height, @NotNull Component label, @NotNull Button.OnPress onPress, Button.CreateNarration narration) {
        super(x, y, width, height, Component.literal(""), onPress, narration);
        this.setLabel(label);
    }

    @Override
    public void render(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.updateIsActive();
        this.updateLabel();
        Tooltip tooltip = this.getTooltipFancyMenu();
        if (tooltip != null && this.m_274382_() && this.f_93624_) {
            if (this.forceDefaultTooltipStyle) {
                tooltip.setDefaultStyle();
            }
            TooltipHandler.INSTANCE.addTooltip(tooltip, () -> true, false, true);
        }
        super.m_88315_(graphics, mouseX, mouseY, partial);
    }

    @Override
    public void renderWidget(@NotNull GuiGraphics graphics, int mouseX, int mouseY, float partial) {
        this.renderBackground(graphics);
        this.renderLabelText(graphics);
    }

    protected void renderBackground(@NotNull GuiGraphics graphics) {
        if (this.getExtendedAsCustomizableWidget().renderCustomBackgroundFancyMenu(this, graphics, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_())) {
            if (this.renderColorBackground(graphics)) {
                graphics.setColor(1.0F, 1.0F, 1.0F, this.f_93625_);
                RenderSystem.enableBlend();
                RenderSystem.enableDepthTest();
                graphics.blitNineSliced(f_93617_, this.m_252754_(), this.m_252907_(), this.m_5711_(), this.m_93694_(), 20, 4, 200, 20, 0, this.getTextureY());
                RenderingUtils.resetShaderColor(graphics);
            }
            RenderingUtils.resetShaderColor(graphics);
        }
    }

    protected boolean renderColorBackground(@NotNull GuiGraphics graphics) {
        RenderSystem.enableBlend();
        if (this.f_93623_) {
            if (this.m_198029_()) {
                if (this.backgroundColorHover != null) {
                    graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.backgroundColorHover.getColorInt());
                    if (this.borderColorHover != null) {
                        UIBase.renderBorder(graphics, (float) this.m_252754_(), (float) this.m_252907_(), (float) (this.m_252754_() + this.m_5711_()), (float) (this.m_252907_() + this.m_93694_()), 1.0F, this.borderColorHover.getColorInt(), true, true, true, true);
                    }
                    return false;
                }
            } else if (this.backgroundColorNormal != null) {
                graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.backgroundColorNormal.getColorInt());
                if (this.borderColorNormal != null) {
                    UIBase.renderBorder(graphics, (float) this.m_252754_(), (float) this.m_252907_(), (float) (this.m_252754_() + this.m_5711_()), (float) (this.m_252907_() + this.m_93694_()), 1.0F, this.borderColorNormal.getColorInt(), true, true, true, true);
                }
                return false;
            }
        } else if (this.backgroundColorInactive != null) {
            graphics.fill(this.m_252754_(), this.m_252907_(), this.m_252754_() + this.m_5711_(), this.m_252907_() + this.m_93694_(), this.backgroundColorInactive.getColorInt());
            if (this.borderColorInactive != null) {
                UIBase.renderBorder(graphics, (float) this.m_252754_(), (float) this.m_252907_(), (float) (this.m_252754_() + this.m_5711_()), (float) (this.m_252907_() + this.m_93694_()), 1.0F, this.borderColorInactive.getColorInt(), true, true, true, true);
            }
            return false;
        }
        return true;
    }

    protected void renderLabelText(@NotNull GuiGraphics graphics) {
        if (this.enableLabel) {
            int k = this.f_93623_ ? this.labelBaseColorNormal.getColorIntWithAlpha(this.f_93625_) : this.labelBaseColorInactive.getColorIntWithAlpha(this.f_93625_);
            this.renderScrollingLabel(this, graphics, this.mc.font, 2, this.labelShadow, k);
        }
    }

    protected void updateLabel() {
        Component c = this.labelSupplier.get(this);
        if (c == null) {
            c = Component.literal("");
        }
        ((IMixinAbstractWidget) this).setMessageFieldFancyMenu(c);
    }

    protected void updateIsActive() {
        if (this.activeSupplier != null) {
            Boolean b = this.activeSupplier.get(this);
            if (b != null) {
                this.f_93623_ = b;
            }
        }
    }

    public void setHeight(int height) {
        this.f_93619_ = height;
    }

    protected int getHoverState() {
        return this.f_93622_ ? 1 : 0;
    }

    @Override
    protected int getTextureY() {
        int i = 1;
        if (!this.f_93623_) {
            i = 0;
        } else if (this.m_198029_()) {
            i = 2;
        }
        return 46 + i * 20;
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

    @Nullable
    public ConsumingSupplier<ExtendedButton, Tooltip> getTooltipSupplier() {
        return this.tooltipSupplier;
    }

    public ExtendedButton setTooltipSupplier(@Nullable ConsumingSupplier<ExtendedButton, Tooltip> tooltipSupplier) {
        this.tooltipSupplier = tooltipSupplier;
        return this;
    }

    @Nullable
    public Tooltip getTooltipFancyMenu() {
        return this.tooltipSupplier != null ? this.tooltipSupplier.get(this) : null;
    }

    public ExtendedButton setTooltip(@Nullable Tooltip tooltip) {
        if (tooltip == null) {
            this.tooltipSupplier = null;
        } else {
            this.tooltipSupplier = button -> tooltip;
        }
        return this;
    }

    public boolean isForceDefaultTooltipStyle() {
        return this.forceDefaultTooltipStyle;
    }

    public ExtendedButton setForceDefaultTooltipStyle(boolean forceDefaultTooltipStyle) {
        this.forceDefaultTooltipStyle = forceDefaultTooltipStyle;
        return this;
    }

    @Deprecated
    @Override
    public void setMessage(@NotNull Component msg) {
        this.setLabel(msg);
    }

    @Deprecated
    @NotNull
    @Override
    public Component getMessage() {
        return super.m_6035_();
    }

    public ExtendedButton setLabel(@NotNull Component label) {
        this.labelSupplier = btn -> label;
        ((IMixinAbstractWidget) this).setMessageFieldFancyMenu(label);
        return this;
    }

    public ExtendedButton setLabel(@NotNull String label) {
        this.labelSupplier = btn -> Component.literal(label);
        return this;
    }

    public ExtendedButton setLabelSupplier(@NotNull ConsumingSupplier<ExtendedButton, Component> labelSupplier) {
        this.labelSupplier = labelSupplier;
        return this;
    }

    @NotNull
    public ConsumingSupplier<ExtendedButton, Component> getLabelSupplier() {
        return this.labelSupplier;
    }

    @NotNull
    public Component getLabel() {
        Component c = this.getLabelSupplier().get(this);
        if (c == null) {
            c = Component.empty();
        }
        return c;
    }

    public boolean isLabelEnabled() {
        return this.enableLabel;
    }

    public ExtendedButton setLabelEnabled(boolean enabled) {
        this.enableLabel = enabled;
        return this;
    }

    public DrawableColor getLabelBaseColorNormal() {
        return this.labelBaseColorNormal;
    }

    public void setLabelBaseColorNormal(DrawableColor labelBaseColorNormal) {
        this.labelBaseColorNormal = labelBaseColorNormal;
    }

    public DrawableColor getLabelBaseColorInactive() {
        return this.labelBaseColorInactive;
    }

    public void setLabelBaseColorInactive(DrawableColor labelBaseColorInactive) {
        this.labelBaseColorInactive = labelBaseColorInactive;
    }

    public boolean isLabelShadowEnabled() {
        return this.labelShadow;
    }

    public ExtendedButton setLabelShadowEnabled(boolean enabled) {
        this.labelShadow = enabled;
        return this;
    }

    public ExtendedButton setIsActiveSupplier(@Nullable ConsumingSupplier<ExtendedButton, Boolean> isActiveSupplier) {
        this.activeSupplier = isActiveSupplier;
        return this;
    }

    @Nullable
    public ConsumingSupplier<ExtendedButton, Boolean> getIsActiveSupplier() {
        return this.activeSupplier;
    }

    @Nullable
    public DrawableColor getBackgroundColorNormal() {
        return this.backgroundColorNormal;
    }

    public void setBackgroundColor(@Nullable DrawableColor backgroundColorNormal, @Nullable DrawableColor backgroundColorHover, @Nullable DrawableColor backgroundColorInactive, @Nullable DrawableColor borderColorNormal, @Nullable DrawableColor borderColorHover, @Nullable DrawableColor borderColorInactive) {
        this.backgroundColorNormal = backgroundColorNormal;
        this.backgroundColorHover = backgroundColorHover;
        this.backgroundColorInactive = backgroundColorInactive;
        this.borderColorNormal = borderColorNormal;
        this.borderColorHover = borderColorHover;
        this.borderColorInactive = borderColorInactive;
    }

    public void setBackgroundColorNormal(@Nullable DrawableColor backgroundColorNormal) {
        this.backgroundColorNormal = backgroundColorNormal;
    }

    @Nullable
    public DrawableColor getBackgroundColorHover() {
        return this.backgroundColorHover;
    }

    public void setBackgroundColorHover(@Nullable DrawableColor backgroundColorHover) {
        this.backgroundColorHover = backgroundColorHover;
    }

    @Nullable
    public DrawableColor getBackgroundColorInactive() {
        return this.backgroundColorInactive;
    }

    public void setBackgroundColorInactive(@Nullable DrawableColor backgroundColorInactive) {
        this.backgroundColorInactive = backgroundColorInactive;
    }

    @Nullable
    public DrawableColor getBorderColorNormal() {
        return this.borderColorNormal;
    }

    public void setBorderColorNormal(@Nullable DrawableColor borderColorNormal) {
        this.borderColorNormal = borderColorNormal;
    }

    @Nullable
    public DrawableColor getBorderColorHover() {
        return this.borderColorHover;
    }

    public void setBorderColorHover(@Nullable DrawableColor borderColorHover) {
        this.borderColorHover = borderColorHover;
    }

    @Nullable
    public DrawableColor getBorderColorInactive() {
        return this.borderColorInactive;
    }

    public void setBorderColorInactive(@Nullable DrawableColor borderColorInactive) {
        this.borderColorInactive = borderColorInactive;
    }

    @Nullable
    public RenderableResource getBackgroundNormal() {
        return this.getExtendedAsCustomizableWidget().getCustomBackgroundNormalFancyMenu();
    }

    public ExtendedButton setBackgroundNormal(@Nullable RenderableResource background) {
        this.getExtendedAsCustomizableWidget().setCustomBackgroundNormalFancyMenu(background);
        return this;
    }

    @Nullable
    public RenderableResource getBackgroundHover() {
        return this.getExtendedAsCustomizableWidget().getCustomBackgroundHoverFancyMenu();
    }

    public ExtendedButton setBackgroundHover(@Nullable RenderableResource background) {
        this.getExtendedAsCustomizableWidget().setCustomBackgroundHoverFancyMenu(background);
        return this;
    }

    @Nullable
    public RenderableResource getBackgroundInactive() {
        return this.getExtendedAsCustomizableWidget().getCustomBackgroundInactiveFancyMenu();
    }

    public ExtendedButton setBackgroundInactive(@Nullable RenderableResource background) {
        this.getExtendedAsCustomizableWidget().setCustomBackgroundInactiveFancyMenu(background);
        return this;
    }

    public Button.OnPress getPressAction() {
        return this.f_93717_;
    }

    public ExtendedButton setPressAction(@NotNull Button.OnPress pressAction) {
        ((IMixinButton) this).setPressActionFancyMenu(pressAction);
        return this;
    }

    public CustomizableWidget getExtendedAsCustomizableWidget() {
        return (CustomizableWidget) this;
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public boolean isFocused() {
        return !this.focusable ? false : super.m_93696_();
    }

    @Override
    public void setFocused(boolean $$0) {
        if (!this.focusable) {
            super.m_93692_(false);
        } else {
            super.m_93692_($$0);
        }
    }

    public ExtendedButton setWidgetIdentifierFancyMenu(@Nullable String identifier) {
        this.identifier = identifier;
        return this;
    }

    @Nullable
    @Override
    public String getWidgetIdentifierFancyMenu() {
        return this.identifier;
    }
}