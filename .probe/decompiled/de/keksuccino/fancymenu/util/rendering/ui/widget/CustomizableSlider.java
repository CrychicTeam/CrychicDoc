package de.keksuccino.fancymenu.util.rendering.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.resource.RenderableResource;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractSliderButton;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public interface CustomizableSlider {

    void setNineSliceCustomSliderBackground_FancyMenu(boolean var1);

    boolean isNineSliceCustomSliderBackground_FancyMenu();

    void setNineSliceSliderBackgroundBorderX_FancyMenu(int var1);

    int getNineSliceSliderBackgroundBorderX_FancyMenu();

    void setNineSliceSliderBackgroundBorderY_FancyMenu(int var1);

    int getNineSliceSliderBackgroundBorderY_FancyMenu();

    void setNineSliceCustomSliderHandle_FancyMenu(boolean var1);

    boolean isNineSliceCustomSliderHandle_FancyMenu();

    void setNineSliceSliderHandleBorderX_FancyMenu(int var1);

    int getNineSliceSliderHandleBorderX_FancyMenu();

    void setNineSliceSliderHandleBorderY_FancyMenu(int var1);

    int getNineSliceSliderHandleBorderY_FancyMenu();

    void setCustomSliderBackgroundNormalFancyMenu(@Nullable RenderableResource var1);

    @Nullable
    RenderableResource getCustomSliderBackgroundNormalFancyMenu();

    void setCustomSliderBackgroundHighlightedFancyMenu(@Nullable RenderableResource var1);

    @Nullable
    RenderableResource getCustomSliderBackgroundHighlightedFancyMenu();

    default boolean renderSliderBackgroundFancyMenu(GuiGraphics graphics, AbstractSliderButton widget, boolean canChangeValue) {
        ResourceLocation location = null;
        RenderableResource texture = null;
        if (widget.m_93696_() && !canChangeValue) {
            if (this.getCustomSliderBackgroundNormalFancyMenu() instanceof PlayableResource p) {
                p.pause();
            }
            if (this.getCustomSliderBackgroundHighlightedFancyMenu() != null) {
                if (this.getCustomSliderBackgroundHighlightedFancyMenu() instanceof PlayableResource p) {
                    p.play();
                }
                texture = this.getCustomSliderBackgroundHighlightedFancyMenu();
                location = this.getCustomSliderBackgroundHighlightedFancyMenu().getResourceLocation();
            }
        } else {
            if (this.getCustomSliderBackgroundHighlightedFancyMenu() instanceof PlayableResource p) {
                p.pause();
            }
            if (this.getCustomSliderBackgroundNormalFancyMenu() != null) {
                if (this.getCustomSliderBackgroundNormalFancyMenu() instanceof PlayableResource p) {
                    p.play();
                }
                texture = this.getCustomSliderBackgroundNormalFancyMenu();
                location = this.getCustomSliderBackgroundNormalFancyMenu().getResourceLocation();
            }
        }
        if (location != null) {
            graphics.setColor(1.0F, 1.0F, 1.0F, ((IMixinAbstractWidget) this).getAlphaFancyMenu());
            RenderSystem.enableBlend();
            if (this.isNineSliceCustomSliderBackground_FancyMenu()) {
                RenderingUtils.blitNineSliced(graphics, location, widget.m_252754_(), widget.m_252907_(), widget.m_5711_(), widget.m_93694_(), this.getNineSliceSliderBackgroundBorderX_FancyMenu(), this.getNineSliceSliderBackgroundBorderY_FancyMenu(), this.getNineSliceSliderBackgroundBorderX_FancyMenu(), this.getNineSliceSliderBackgroundBorderY_FancyMenu(), texture.getWidth(), texture.getHeight(), 0, 0, texture.getWidth(), texture.getHeight());
            } else {
                graphics.blit(location, widget.m_252754_(), widget.m_252907_(), 0.0F, 0.0F, widget.m_5711_(), widget.m_93694_(), widget.m_5711_(), widget.m_93694_());
            }
            RenderingUtils.resetShaderColor(graphics);
            return false;
        } else {
            return true;
        }
    }
}