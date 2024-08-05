package de.keksuccino.fancymenu.customization.element.elements.button.vanillawidget;

import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementStacker;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.element.anchor.ElementAnchorPoints;
import de.keksuccino.fancymenu.customization.element.elements.button.custombutton.ButtonElement;
import de.keksuccino.fancymenu.customization.element.elements.button.custombutton.ButtonElementBuilder;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.konkrete.math.MathUtils;
import net.minecraft.network.chat.Component;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class VanillaWidgetElementBuilder extends ButtonElementBuilder implements ElementStacker<VanillaWidgetElement> {

    private static final Logger LOGGER = LogManager.getLogger();

    public static final VanillaWidgetElementBuilder INSTANCE = new VanillaWidgetElementBuilder();

    @NotNull
    @Override
    public String getIdentifier() {
        return "vanilla_button";
    }

    @NotNull
    public VanillaWidgetElement buildDefaultInstance() {
        VanillaWidgetElement element = new VanillaWidgetElement(this);
        element.anchorPoint = ElementAnchorPoints.VANILLA;
        return element;
    }

    @Nullable
    @Override
    public SerializedElement serializeElementInternal(@NotNull AbstractElement elementAbstract) {
        try {
            VanillaWidgetElement element = (VanillaWidgetElement) elementAbstract;
            SerializedElement serializeTo = super.serializeElementInternal(element);
            if (serializeTo != null) {
                serializeTo.setType("vanilla_button");
                serializeTo.putProperty("is_hidden", element.vanillaButtonHidden + "");
                serializeTo.putProperty("automated_button_clicks", element.automatedButtonClicks + "");
                if (element.sliderBackgroundTextureNormal != null) {
                    serializeTo.putProperty("slider_background_texture_normal", element.sliderBackgroundTextureNormal.getSourceWithPrefix());
                }
                if (element.sliderBackgroundTextureHighlighted != null) {
                    serializeTo.putProperty("slider_background_texture_highlighted", element.sliderBackgroundTextureHighlighted.getSourceWithPrefix());
                }
                serializeTo.putProperty("slider_background_animation_normal", element.sliderBackgroundAnimationNormal);
                serializeTo.putProperty("slider_background_animation_highlighted", element.sliderBackgroundAnimationHighlighted);
                serializeTo.putProperty("nine_slice_slider_handle", element.nineSliceSliderHandle + "");
                serializeTo.putProperty("nine_slice_slider_handle_border_x", element.nineSliceSliderHandleBorderX + "");
                serializeTo.putProperty("nine_slice_slider_handle_border_y", element.nineSliceSliderHandleBorderY + "");
                return serializeTo;
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
        return null;
    }

    @NotNull
    public VanillaWidgetElement deserializeElement(@NotNull SerializedElement serialized) {
        VanillaWidgetElement element = (VanillaWidgetElement) super.deserializeElement(serialized);
        String hidden = serialized.getValue("is_hidden");
        if (hidden != null && hidden.equalsIgnoreCase("true")) {
            element.vanillaButtonHidden = true;
        }
        String automatedClicks = serialized.getValue("automated_button_clicks");
        if (automatedClicks != null && MathUtils.isInteger(automatedClicks)) {
            element.automatedButtonClicks = Integer.parseInt(automatedClicks);
        }
        element.sliderBackgroundTextureNormal = deserializeImageResourceSupplier(serialized.getValue("slider_background_texture_normal"));
        element.sliderBackgroundTextureHighlighted = deserializeImageResourceSupplier(serialized.getValue("slider_background_texture_highlighted"));
        element.sliderBackgroundAnimationNormal = serialized.getValue("slider_background_animation_normal");
        element.sliderBackgroundAnimationHighlighted = serialized.getValue("slider_background_animation_highlighted");
        element.nineSliceSliderHandle = this.deserializeBoolean(element.nineSliceSliderHandle, serialized.getValue("nine_slice_slider_handle"));
        element.nineSliceSliderHandleBorderX = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceSliderHandleBorderX), serialized.getValue("nine_slice_slider_handle_border_x"));
        element.nineSliceSliderHandleBorderY = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceSliderHandleBorderY), serialized.getValue("nine_slice_slider_handle_border_y"));
        return element;
    }

    @Nullable
    public VanillaWidgetElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        return (VanillaWidgetElement) super.deserializeElementInternal(serialized);
    }

    @NotNull
    public VanillaWidgetEditorElement wrapIntoEditorElement(@NotNull ButtonElement element, @NotNull LayoutEditorScreen editor) {
        return new VanillaWidgetEditorElement(element, editor);
    }

    public void stackElements(@NotNull VanillaWidgetElement e, @NotNull VanillaWidgetElement stack) {
        if (e.widgetMeta != null) {
            stack.setVanillaWidget(e.widgetMeta, false);
        }
        if (e.vanillaButtonHidden) {
            stack.vanillaButtonHidden = true;
        }
        if (e.automatedButtonClicks != 0) {
            stack.automatedButtonClicks = e.automatedButtonClicks;
        }
        if (e.clickSound != null) {
            stack.clickSound = e.clickSound;
        }
        if (e.hoverSound != null) {
            stack.hoverSound = e.hoverSound;
        }
        if (e.label != null) {
            stack.label = e.label;
        }
        if (e.hoverLabel != null) {
            stack.hoverLabel = e.hoverLabel;
        }
        if (e.tooltip != null) {
            stack.tooltip = e.tooltip;
        }
        if (e.backgroundTextureNormal != null) {
            stack.backgroundTextureNormal = e.backgroundTextureNormal;
        }
        if (e.backgroundTextureHover != null) {
            stack.backgroundTextureHover = e.backgroundTextureHover;
        }
        if (e.backgroundTextureInactive != null) {
            stack.backgroundTextureInactive = e.backgroundTextureInactive;
        }
        if (e.backgroundAnimationNormal != null) {
            stack.backgroundAnimationNormal = e.backgroundAnimationNormal;
        }
        if (e.backgroundAnimationHover != null) {
            stack.backgroundAnimationHover = e.backgroundAnimationHover;
        }
        if (e.backgroundAnimationInactive != null) {
            stack.backgroundAnimationInactive = e.backgroundAnimationInactive;
        }
        if (!e.loopBackgroundAnimations) {
            stack.loopBackgroundAnimations = false;
        }
        if (!e.restartBackgroundAnimationsOnHover) {
            stack.restartBackgroundAnimationsOnHover = false;
        }
        if (e.nineSliceCustomBackground) {
            stack.nineSliceCustomBackground = true;
        }
        if (e.nineSliceBorderX != 5) {
            stack.nineSliceBorderX = e.nineSliceBorderX;
        }
        if (e.nineSliceBorderY != 5) {
            stack.nineSliceBorderY = e.nineSliceBorderY;
        }
        if (e.nineSliceSliderHandle) {
            stack.nineSliceSliderHandle = true;
        }
        if (e.nineSliceSliderHandleBorderX != 5) {
            stack.nineSliceSliderHandleBorderX = e.nineSliceSliderHandleBorderX;
        }
        if (e.nineSliceSliderHandleBorderY != 5) {
            stack.nineSliceSliderHandleBorderY = e.nineSliceSliderHandleBorderY;
        }
        if (e.sliderBackgroundTextureNormal != null) {
            stack.sliderBackgroundTextureNormal = e.sliderBackgroundTextureNormal;
        }
        if (e.sliderBackgroundTextureHighlighted != null) {
            stack.sliderBackgroundTextureHighlighted = e.sliderBackgroundTextureHighlighted;
        }
        if (e.sliderBackgroundAnimationNormal != null) {
            stack.sliderBackgroundAnimationNormal = e.sliderBackgroundAnimationNormal;
        }
        if (e.sliderBackgroundAnimationHighlighted != null) {
            stack.sliderBackgroundAnimationHighlighted = e.sliderBackgroundAnimationHighlighted;
        }
    }

    @Override
    public void stackElementsSingleInternal(AbstractElement e, AbstractElement stack) {
        ElementStacker.super.stackElementsSingleInternal(e, stack);
        if (e.anchorPoint != ElementAnchorPoints.VANILLA) {
            stack.anchorPoint = e.anchorPoint;
        }
    }

    @Nullable
    public VanillaWidgetElement stackElementsInternal(AbstractElement stack, AbstractElement... elements) {
        if (stack != null) {
            stack.anchorPoint = ElementAnchorPoints.VANILLA;
        }
        return (VanillaWidgetElement) ElementStacker.super.stackElementsInternal(stack, elements);
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return null;
    }
}