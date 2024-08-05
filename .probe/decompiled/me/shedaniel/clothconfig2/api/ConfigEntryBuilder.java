package me.shedaniel.clothconfig2.api;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import java.util.function.Function;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
import me.shedaniel.clothconfig2.impl.ConfigEntryBuilderImpl;
import me.shedaniel.clothconfig2.impl.builders.BooleanToggleBuilder;
import me.shedaniel.clothconfig2.impl.builders.ColorFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DoubleFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.DoubleListBuilder;
import me.shedaniel.clothconfig2.impl.builders.DropdownMenuBuilder;
import me.shedaniel.clothconfig2.impl.builders.EnumSelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.FloatFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.FloatListBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntListBuilder;
import me.shedaniel.clothconfig2.impl.builders.IntSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.KeyCodeBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongListBuilder;
import me.shedaniel.clothconfig2.impl.builders.LongSliderBuilder;
import me.shedaniel.clothconfig2.impl.builders.SelectorBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringFieldBuilder;
import me.shedaniel.clothconfig2.impl.builders.StringListBuilder;
import me.shedaniel.clothconfig2.impl.builders.SubCategoryBuilder;
import me.shedaniel.clothconfig2.impl.builders.TextDescriptionBuilder;
import me.shedaniel.clothconfig2.impl.builders.TextFieldBuilder;
import me.shedaniel.math.Color;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public interface ConfigEntryBuilder {

    static ConfigEntryBuilder create() {
        return ConfigEntryBuilderImpl.create();
    }

    Component getResetButtonKey();

    ConfigEntryBuilder setResetButtonKey(Component var1);

    IntListBuilder startIntList(Component var1, List<Integer> var2);

    LongListBuilder startLongList(Component var1, List<Long> var2);

    FloatListBuilder startFloatList(Component var1, List<Float> var2);

    DoubleListBuilder startDoubleList(Component var1, List<Double> var2);

    StringListBuilder startStrList(Component var1, List<String> var2);

    SubCategoryBuilder startSubCategory(Component var1);

    SubCategoryBuilder startSubCategory(Component var1, List<AbstractConfigListEntry> var2);

    BooleanToggleBuilder startBooleanToggle(Component var1, boolean var2);

    StringFieldBuilder startStrField(Component var1, String var2);

    ColorFieldBuilder startColorField(Component var1, int var2);

    default ColorFieldBuilder startColorField(Component fieldNameKey, TextColor color) {
        return this.startColorField(fieldNameKey, color.getValue());
    }

    default ColorFieldBuilder startColorField(Component fieldNameKey, Color color) {
        return this.startColorField(fieldNameKey, color.getColor() & 16777215);
    }

    default ColorFieldBuilder startAlphaColorField(Component fieldNameKey, int value) {
        return this.startColorField(fieldNameKey, value).setAlphaMode(true);
    }

    default ColorFieldBuilder startAlphaColorField(Component fieldNameKey, Color color) {
        return this.startColorField(fieldNameKey, color.getColor());
    }

    TextFieldBuilder startTextField(Component var1, String var2);

    TextDescriptionBuilder startTextDescription(Component var1);

    <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(Component var1, Class<T> var2, T var3);

    <T> SelectorBuilder<T> startSelector(Component var1, T[] var2, T var3);

    IntFieldBuilder startIntField(Component var1, int var2);

    LongFieldBuilder startLongField(Component var1, long var2);

    FloatFieldBuilder startFloatField(Component var1, float var2);

    DoubleFieldBuilder startDoubleField(Component var1, double var2);

    IntSliderBuilder startIntSlider(Component var1, int var2, int var3, int var4);

    LongSliderBuilder startLongSlider(Component var1, long var2, long var4, long var6);

    KeyCodeBuilder startModifierKeyCodeField(Component var1, ModifierKeyCode var2);

    default KeyCodeBuilder startKeyCodeField(Component fieldNameKey, InputConstants.Key value) {
        return this.startModifierKeyCodeField(fieldNameKey, ModifierKeyCode.of(value, Modifier.none())).setAllowModifiers(false);
    }

    default KeyCodeBuilder fillKeybindingField(Component fieldNameKey, KeyMapping value) {
        return this.startKeyCodeField(fieldNameKey, value.key).setDefaultValue(value.getDefaultKey()).setKeySaveConsumer(code -> {
            value.setKey(code);
            KeyMapping.resetMapping();
            Minecraft.getInstance().options.save();
        });
    }

    <T> DropdownMenuBuilder<T> startDropdownMenu(Component var1, DropdownBoxEntry.SelectionTopCellElement<T> var2, DropdownBoxEntry.SelectionCellCreator<T> var3);

    default <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement) {
        return this.startDropdownMenu(fieldNameKey, topCellElement, new DropdownBoxEntry.DefaultSelectionCellCreator<>());
    }

    default <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, T value, Function<String, T> toObjectFunction, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, toObjectFunction), cellCreator);
    }

    default <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, T value, Function<String, T> toObjectFunction, Function<T, Component> toTextFunction, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, toObjectFunction, toTextFunction), cellCreator);
    }

    default <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, T value, Function<String, T> toObjectFunction) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, toObjectFunction), new DropdownBoxEntry.DefaultSelectionCellCreator<>());
    }

    default <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, T value, Function<String, T> toObjectFunction, Function<T, Component> toTextFunction) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, toObjectFunction, toTextFunction), new DropdownBoxEntry.DefaultSelectionCellCreator<>());
    }

    default DropdownMenuBuilder<String> startStringDropdownMenu(Component fieldNameKey, String value, DropdownBoxEntry.SelectionCellCreator<String> cellCreator) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, s -> s, Component::m_237113_), cellCreator);
    }

    default DropdownMenuBuilder<String> startStringDropdownMenu(Component fieldNameKey, String value, Function<String, Component> toTextFunction, DropdownBoxEntry.SelectionCellCreator<String> cellCreator) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, s -> s, toTextFunction), cellCreator);
    }

    default DropdownMenuBuilder<String> startStringDropdownMenu(Component fieldNameKey, String value) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, s -> s, Component::m_237113_), new DropdownBoxEntry.DefaultSelectionCellCreator<>());
    }

    default DropdownMenuBuilder<String> startStringDropdownMenu(Component fieldNameKey, String value, Function<String, Component> toTextFunction) {
        return this.startDropdownMenu(fieldNameKey, DropdownMenuBuilder.TopCellElementBuilder.of(value, s -> s, toTextFunction), new DropdownBoxEntry.DefaultSelectionCellCreator<>());
    }
}