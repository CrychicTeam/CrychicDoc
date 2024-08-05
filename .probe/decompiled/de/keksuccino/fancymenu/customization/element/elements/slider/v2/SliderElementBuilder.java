package de.keksuccino.fancymenu.customization.element.elements.slider.v2;

import de.keksuccino.fancymenu.customization.action.blocks.ExecutableBlockDeserializer;
import de.keksuccino.fancymenu.customization.action.blocks.GenericExecutableBlock;
import de.keksuccino.fancymenu.customization.element.AbstractElement;
import de.keksuccino.fancymenu.customization.element.ElementBuilder;
import de.keksuccino.fancymenu.customization.element.SerializedElement;
import de.keksuccino.fancymenu.customization.layout.editor.LayoutEditorScreen;
import de.keksuccino.fancymenu.util.LocalizationUtils;
import de.keksuccino.konkrete.math.MathUtils;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import net.minecraft.network.chat.Component;
import org.apache.commons.lang3.tuple.Pair;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SliderElementBuilder extends ElementBuilder<SliderElement, SliderEditorElement> {

    public SliderElementBuilder() {
        super("slider_v2");
    }

    @NotNull
    public SliderElement buildDefaultInstance() {
        SliderElement i = new SliderElement(this);
        i.baseWidth = 100;
        i.baseHeight = 20;
        i.label = "New Slider: $$value";
        i.listValues.addAll(List.of("some_value", "another_value", "third_value"));
        i.minRangeValue = 0.0;
        i.maxRangeValue = 20.0;
        return i;
    }

    public SliderElement deserializeElement(@NotNull SerializedElement serialized) {
        SliderElement element = this.buildDefaultInstance();
        String sliderTypeString = serialized.getValue("slider_type");
        if (sliderTypeString != null) {
            SliderElement.SliderType t = SliderElement.SliderType.getByName(sliderTypeString);
            if (t != null) {
                element.type = t;
            }
        }
        element.preSelectedValue = serialized.getValue("pre_selected_value");
        element.label = serialized.getValue("slider_label");
        element.minRangeValue = (Double) this.deserializeNumber(Double.class, Double.valueOf(element.minRangeValue), serialized.getValue("min_range_value"));
        element.maxRangeValue = (Double) this.deserializeNumber(Double.class, Double.valueOf(element.maxRangeValue), serialized.getValue("max_range_value"));
        element.roundingDecimalPlace = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.roundingDecimalPlace), serialized.getValue("rounding_decimal_place"));
        List<Pair<String, String>> listValueEntries = new ArrayList();
        serialized.getProperties().forEach((key, value) -> {
            if (key.startsWith("slider_list_value_")) {
                listValueEntries.add(Pair.of(key, value));
            }
        });
        listValueEntries.sort(Comparator.comparingInt(value -> {
            String key = (String) value.getKey();
            key = new StringBuilder(key).reverse().toString();
            key = new StringBuilder(key.split("_", 2)[0]).reverse().toString();
            return MathUtils.isInteger(key) ? Integer.parseInt(key) : 0;
        }));
        if (!listValueEntries.isEmpty()) {
            element.listValues.clear();
        }
        listValueEntries.forEach(pair -> element.listValues.add((String) pair.getValue()));
        if (element.listValues.size() < 2) {
            element.listValues.add("placeholder_value");
        }
        String executableBlockId = serialized.getValue("slider_element_executable_block_identifier");
        if (executableBlockId != null && ExecutableBlockDeserializer.deserializeWithIdentifier(serialized, executableBlockId) instanceof GenericExecutableBlock g) {
            element.executableBlock = g;
        }
        element.tooltip = serialized.getValue("tooltip");
        element.handleTextureNormal = deserializeImageResourceSupplier(serialized.getValue("handle_texture_normal"));
        element.handleTextureHover = deserializeImageResourceSupplier(serialized.getValue("handle_texture_hovered"));
        element.handleTextureInactive = deserializeImageResourceSupplier(serialized.getValue("handle_texture_inactive"));
        element.handleAnimationNormal = serialized.getValue("handle_animation_normal");
        element.handleAnimationHover = serialized.getValue("handle_animation_hovered");
        element.handleAnimationInactive = serialized.getValue("handle_animation_inactive");
        element.sliderBackgroundTextureNormal = deserializeImageResourceSupplier(serialized.getValue("slider_background_texture_normal"));
        element.sliderBackgroundTextureHighlighted = deserializeImageResourceSupplier(serialized.getValue("slider_background_texture_highlighted"));
        element.sliderBackgroundAnimationNormal = serialized.getValue("slider_background_animation_normal");
        element.sliderBackgroundAnimationHighlighted = serialized.getValue("slider_background_animation_highlighted");
        element.loopBackgroundAnimations = this.deserializeBoolean(element.loopBackgroundAnimations, serialized.getValue("loop_background_animations"));
        element.restartBackgroundAnimationsOnHover = this.deserializeBoolean(element.restartBackgroundAnimationsOnHover, serialized.getValue("restart_background_animations"));
        element.nineSliceCustomBackground = this.deserializeBoolean(element.nineSliceCustomBackground, serialized.getValue("nine_slice_custom_background"));
        element.nineSliceBorderX = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceBorderX), serialized.getValue("nine_slice_border_x"));
        element.nineSliceBorderY = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceBorderY), serialized.getValue("nine_slice_border_y"));
        element.nineSliceSliderHandle = this.deserializeBoolean(element.nineSliceSliderHandle, serialized.getValue("nine_slice_slider_handle"));
        element.nineSliceSliderHandleBorderX = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceSliderHandleBorderX), serialized.getValue("nine_slice_slider_handle_border_x"));
        element.nineSliceSliderHandleBorderY = (Integer) this.deserializeNumber(Integer.class, Integer.valueOf(element.nineSliceSliderHandleBorderY), serialized.getValue("nine_slice_slider_handle_border_y"));
        element.navigatable = this.deserializeBoolean(element.navigatable, serialized.getValue("navigatable"));
        element.buildSlider();
        element.prepareExecutableBlock();
        return element;
    }

    @Nullable
    public SliderElement deserializeElementInternal(@NotNull SerializedElement serialized) {
        SliderElement element = (SliderElement) super.deserializeElementInternal(serialized);
        if (element != null) {
            element.prepareLoadingRequirementContainer();
        }
        return element;
    }

    protected SerializedElement serializeElement(@NotNull SliderElement element, @NotNull SerializedElement serializeTo) {
        serializeTo.putProperty("slider_type", element.type.getName());
        serializeTo.putProperty("pre_selected_value", element.preSelectedValue);
        serializeTo.putProperty("slider_label", element.label);
        serializeTo.putProperty("min_range_value", element.minRangeValue + "");
        serializeTo.putProperty("max_range_value", element.maxRangeValue + "");
        serializeTo.putProperty("rounding_decimal_place", element.roundingDecimalPlace + "");
        int i = 0;
        for (String s : element.listValues) {
            serializeTo.putProperty("slider_list_value_" + i, s);
            i++;
        }
        serializeTo.putProperty("slider_element_executable_block_identifier", element.executableBlock.getIdentifier());
        element.executableBlock.serializeToExistingPropertyContainer(serializeTo);
        serializeTo.putProperty("tooltip", element.tooltip);
        if (element.handleTextureNormal != null) {
            serializeTo.putProperty("handle_texture_normal", element.handleTextureNormal.getSourceWithPrefix());
        }
        if (element.handleTextureHover != null) {
            serializeTo.putProperty("handle_texture_hovered", element.handleTextureHover.getSourceWithPrefix());
        }
        if (element.handleTextureInactive != null) {
            serializeTo.putProperty("handle_texture_inactive", element.handleTextureInactive.getSourceWithPrefix());
        }
        serializeTo.putProperty("handle_animation_normal", element.handleAnimationNormal);
        serializeTo.putProperty("handle_animation_hovered", element.handleAnimationHover);
        serializeTo.putProperty("handle_animation_inactive", element.handleAnimationInactive);
        serializeTo.putProperty("restart_background_animations", element.restartBackgroundAnimationsOnHover + "");
        serializeTo.putProperty("loop_background_animations", element.loopBackgroundAnimations + "");
        if (element.sliderBackgroundTextureNormal != null) {
            serializeTo.putProperty("slider_background_texture_normal", element.sliderBackgroundTextureNormal.getSourceWithPrefix());
        }
        if (element.sliderBackgroundTextureHighlighted != null) {
            serializeTo.putProperty("slider_background_texture_highlighted", element.sliderBackgroundTextureHighlighted.getSourceWithPrefix());
        }
        serializeTo.putProperty("slider_background_animation_normal", element.sliderBackgroundAnimationNormal);
        serializeTo.putProperty("slider_background_animation_highlighted", element.sliderBackgroundAnimationHighlighted);
        serializeTo.putProperty("nine_slice_custom_background", element.nineSliceCustomBackground + "");
        serializeTo.putProperty("nine_slice_border_x", element.nineSliceBorderX + "");
        serializeTo.putProperty("nine_slice_border_y", element.nineSliceBorderY + "");
        serializeTo.putProperty("nine_slice_slider_handle", element.nineSliceSliderHandle + "");
        serializeTo.putProperty("nine_slice_slider_handle_border_x", element.nineSliceSliderHandleBorderX + "");
        serializeTo.putProperty("nine_slice_slider_handle_border_y", element.nineSliceSliderHandleBorderY + "");
        serializeTo.putProperty("navigatable", element.navigatable + "");
        return serializeTo;
    }

    @NotNull
    public SliderEditorElement wrapIntoEditorElement(@NotNull SliderElement element, @NotNull LayoutEditorScreen editor) {
        return new SliderEditorElement(element, editor);
    }

    @NotNull
    @Override
    public Component getDisplayName(@Nullable AbstractElement element) {
        return Component.translatable("fancymenu.elements.slider.v2");
    }

    @Nullable
    @Override
    public Component[] getDescription(@Nullable AbstractElement element) {
        return LocalizationUtils.splitLocalizedLines("fancymenu.elements.slider.v2.desc");
    }
}