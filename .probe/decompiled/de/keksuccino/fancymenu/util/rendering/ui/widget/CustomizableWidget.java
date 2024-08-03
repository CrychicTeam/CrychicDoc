package de.keksuccino.fancymenu.util.rendering.ui.widget;

import com.mojang.blaze3d.systems.RenderSystem;
import de.keksuccino.fancymenu.mixin.mixins.common.client.IMixinAbstractWidget;
import de.keksuccino.fancymenu.util.rendering.RenderingUtils;
import de.keksuccino.fancymenu.util.resource.PlayableResource;
import de.keksuccino.fancymenu.util.resource.RenderableResource;
import de.keksuccino.fancymenu.util.resource.resources.audio.IAudio;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface CustomizableWidget {

    @Nullable
    default Component getOriginalMessageFancyMenu() {
        Component custom = this.getCustomLabelFancyMenu();
        Component hover = this.getHoverLabelFancyMenu();
        this.setCustomLabelFancyMenu(null);
        this.setHoverLabelFancyMenu(null);
        Component original = null;
        if (this instanceof AbstractWidget w) {
            original = w.getMessage();
        }
        this.setCustomLabelFancyMenu(custom);
        this.setHoverLabelFancyMenu(hover);
        return original;
    }

    default boolean renderCustomBackgroundFancyMenu(@NotNull AbstractWidget widget, @NotNull GuiGraphics graphics, int x, int y, int width, int height) {
        RenderableResource customBackgroundNormal = this.getCustomBackgroundNormalFancyMenu();
        RenderableResource customBackgroundHover = this.getCustomBackgroundHoverFancyMenu();
        RenderableResource customBackgroundInactive = this.getCustomBackgroundInactiveFancyMenu();
        RenderableResource customBackground;
        if (widget.active) {
            if (widget.isHoveredOrFocused()) {
                customBackground = customBackgroundHover;
                if (customBackgroundNormal instanceof PlayableResource p) {
                    p.pause();
                }
            } else {
                customBackground = customBackgroundNormal;
                if (customBackgroundHover instanceof PlayableResource p) {
                    p.pause();
                }
            }
            if (customBackgroundInactive instanceof PlayableResource p) {
                p.pause();
            }
        } else {
            customBackground = customBackgroundInactive;
            if (customBackgroundNormal instanceof PlayableResource p) {
                p.pause();
            }
            if (customBackgroundHover instanceof PlayableResource p) {
                p.pause();
            }
        }
        boolean renderVanilla = true;
        if (customBackground != null) {
            if (customBackground instanceof PlayableResource p) {
                p.play();
            }
            ResourceLocation location = customBackground.getResourceLocation();
            if (location != null) {
                label55: {
                    renderVanilla = false;
                    graphics.setColor(1.0F, 1.0F, 1.0F, ((IMixinAbstractWidget) widget).getAlphaFancyMenu());
                    RenderSystem.enableBlend();
                    if (widget instanceof CustomizableSlider s && s.isNineSliceCustomSliderHandle_FancyMenu()) {
                        RenderingUtils.blitNineSliced(graphics, location, x, y, width, height, s.getNineSliceSliderHandleBorderX_FancyMenu(), s.getNineSliceSliderHandleBorderY_FancyMenu(), s.getNineSliceSliderHandleBorderX_FancyMenu(), s.getNineSliceSliderHandleBorderY_FancyMenu(), customBackground.getWidth(), customBackground.getHeight(), 0, 0, customBackground.getWidth(), customBackground.getHeight());
                        break label55;
                    }
                    if (!(widget instanceof CustomizableSlider) && this.isNineSliceCustomBackgroundTexture_FancyMenu()) {
                        RenderingUtils.blitNineSliced(graphics, location, x, y, width, height, this.getNineSliceCustomBackgroundBorderX_FancyMenu(), this.getNineSliceCustomBackgroundBorderY_FancyMenu(), this.getNineSliceCustomBackgroundBorderX_FancyMenu(), this.getNineSliceCustomBackgroundBorderY_FancyMenu(), customBackground.getWidth(), customBackground.getHeight(), 0, 0, customBackground.getWidth(), customBackground.getHeight());
                    } else {
                        graphics.blit(location, x, y, 0.0F, 0.0F, width, height, width, height);
                    }
                }
                RenderingUtils.resetShaderColor(graphics);
            }
        }
        return renderVanilla;
    }

    void resetWidgetCustomizationsFancyMenu();

    void resetWidgetSizeAndPositionFancyMenu();

    void addResetCustomizationsListenerFancyMenu(@NotNull Runnable var1);

    @NotNull
    List<Runnable> getResetCustomizationsListenersFancyMenu();

    void addHoverStateListenerFancyMenu(@NotNull Consumer<Boolean> var1);

    void addFocusStateListenerFancyMenu(@NotNull Consumer<Boolean> var1);

    void addHoverOrFocusStateListenerFancyMenu(@NotNull Consumer<Boolean> var1);

    @NotNull
    List<Consumer<Boolean>> getHoverStateListenersFancyMenu();

    @NotNull
    List<Consumer<Boolean>> getFocusStateListenersFancyMenu();

    @NotNull
    List<Consumer<Boolean>> getHoverOrFocusStateListenersFancyMenu();

    boolean getLastHoverStateFancyMenu();

    void setLastHoverStateFancyMenu(boolean var1);

    boolean getLastFocusStateFancyMenu();

    void setLastFocusStateFancyMenu(boolean var1);

    boolean getLastHoverOrFocusStateFancyMenu();

    void setLastHoverOrFocusStateFancyMenu(boolean var1);

    default void tickHoverStateListenersFancyMenu(boolean hovered) {
        if (this.getLastHoverStateFancyMenu() != hovered) {
            for (Consumer<Boolean> listener : this.getHoverStateListenersFancyMenu()) {
                listener.accept(hovered);
            }
        }
        this.setLastHoverStateFancyMenu(hovered);
    }

    default void tickFocusStateListenersFancyMenu(boolean focused) {
        if (this.getLastFocusStateFancyMenu() != focused) {
            for (Consumer<Boolean> listener : this.getFocusStateListenersFancyMenu()) {
                listener.accept(focused);
            }
        }
        this.setLastFocusStateFancyMenu(focused);
    }

    default void tickHoverOrFocusStateListenersFancyMenu(boolean hoveredOrFocused) {
        if (this.getLastHoverOrFocusStateFancyMenu() != hoveredOrFocused) {
            for (Consumer<Boolean> listener : this.getHoverOrFocusStateListenersFancyMenu()) {
                listener.accept(hoveredOrFocused);
            }
        }
        this.setLastHoverOrFocusStateFancyMenu(hoveredOrFocused);
    }

    void setCustomLabelFancyMenu(@Nullable Component var1);

    @Nullable
    Component getCustomLabelFancyMenu();

    void setHoverLabelFancyMenu(@Nullable Component var1);

    @Nullable
    Component getHoverLabelFancyMenu();

    void setCustomClickSoundFancyMenu(@Nullable IAudio var1);

    @Nullable
    IAudio getCustomClickSoundFancyMenu();

    void setHoverSoundFancyMenu(@Nullable IAudio var1);

    @Nullable
    IAudio getHoverSoundFancyMenu();

    void setHiddenFancyMenu(boolean var1);

    boolean isHiddenFancyMenu();

    void setCustomBackgroundNormalFancyMenu(@Nullable RenderableResource var1);

    @Nullable
    RenderableResource getCustomBackgroundNormalFancyMenu();

    void setCustomBackgroundHoverFancyMenu(@Nullable RenderableResource var1);

    @Nullable
    RenderableResource getCustomBackgroundHoverFancyMenu();

    void setCustomBackgroundInactiveFancyMenu(@Nullable RenderableResource var1);

    @Nullable
    RenderableResource getCustomBackgroundInactiveFancyMenu();

    void setNineSliceCustomBackground_FancyMenu(boolean var1);

    boolean isNineSliceCustomBackgroundTexture_FancyMenu();

    void setNineSliceBorderX_FancyMenu(int var1);

    int getNineSliceCustomBackgroundBorderX_FancyMenu();

    void setNineSliceBorderY_FancyMenu(int var1);

    int getNineSliceCustomBackgroundBorderY_FancyMenu();

    void setCustomBackgroundResetBehaviorFancyMenu(@NotNull CustomizableWidget.CustomBackgroundResetBehavior var1);

    @NotNull
    CustomizableWidget.CustomBackgroundResetBehavior getCustomBackgroundResetBehaviorFancyMenu();

    @Nullable
    Integer getCustomWidthFancyMenu();

    void setCustomWidthFancyMenu(@Nullable Integer var1);

    @Nullable
    Integer getCustomHeightFancyMenu();

    void setCustomHeightFancyMenu(@Nullable Integer var1);

    @Nullable
    Integer getCustomXFancyMenu();

    void setCustomXFancyMenu(@Nullable Integer var1);

    @Nullable
    Integer getCustomYFancyMenu();

    void setCustomYFancyMenu(@Nullable Integer var1);

    public static enum CustomBackgroundResetBehavior {

        RESET_NEVER, RESET_ON_HOVER, RESET_ON_UNHOVER, RESET_ON_HOVER_AND_UNHOVER
    }
}