package me.shedaniel.clothconfig2.impl;

import java.util.List;
import java.util.UUID;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ConfigEntryBuilder;
import me.shedaniel.clothconfig2.api.ModifierKeyCode;
import me.shedaniel.clothconfig2.gui.entries.DropdownBoxEntry;
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
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class ConfigEntryBuilderImpl implements ConfigEntryBuilder {

    private Component resetButtonKey = Component.translatable("text.cloth-config.reset_value");

    private ConfigEntryBuilderImpl() {
    }

    public static ConfigEntryBuilderImpl create() {
        return new ConfigEntryBuilderImpl();
    }

    public static ConfigEntryBuilderImpl createImmutable() {
        return new ConfigEntryBuilderImpl() {

            @Override
            public ConfigEntryBuilder setResetButtonKey(Component resetButtonKey) {
                throw new UnsupportedOperationException("This is an immutable entry builder!");
            }
        };
    }

    @Override
    public Component getResetButtonKey() {
        return this.resetButtonKey;
    }

    @Override
    public ConfigEntryBuilder setResetButtonKey(Component resetButtonKey) {
        this.resetButtonKey = resetButtonKey;
        return this;
    }

    @Override
    public IntListBuilder startIntList(Component fieldNameKey, List<Integer> value) {
        return new IntListBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public LongListBuilder startLongList(Component fieldNameKey, List<Long> value) {
        return new LongListBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public FloatListBuilder startFloatList(Component fieldNameKey, List<Float> value) {
        return new FloatListBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public DoubleListBuilder startDoubleList(Component fieldNameKey, List<Double> value) {
        return new DoubleListBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public StringListBuilder startStrList(Component fieldNameKey, List<String> value) {
        return new StringListBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Component fieldNameKey) {
        return new SubCategoryBuilder(this.resetButtonKey, fieldNameKey);
    }

    @Override
    public SubCategoryBuilder startSubCategory(Component fieldNameKey, List<AbstractConfigListEntry> entries) {
        SubCategoryBuilder builder = new SubCategoryBuilder(this.resetButtonKey, fieldNameKey);
        builder.addAll(entries);
        return builder;
    }

    @Override
    public BooleanToggleBuilder startBooleanToggle(Component fieldNameKey, boolean value) {
        return new BooleanToggleBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public StringFieldBuilder startStrField(Component fieldNameKey, String value) {
        return new StringFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public ColorFieldBuilder startColorField(Component fieldNameKey, int value) {
        return new ColorFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public TextFieldBuilder startTextField(Component fieldNameKey, String value) {
        return new TextFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public TextDescriptionBuilder startTextDescription(Component value) {
        return new TextDescriptionBuilder(this.resetButtonKey, Component.literal(UUID.randomUUID().toString()), value);
    }

    @Override
    public <T extends Enum<?>> EnumSelectorBuilder<T> startEnumSelector(Component fieldNameKey, Class<T> clazz, T value) {
        return new EnumSelectorBuilder<>(this.resetButtonKey, fieldNameKey, clazz, value);
    }

    @Override
    public <T> SelectorBuilder<T> startSelector(Component fieldNameKey, T[] valuesArray, T value) {
        return new SelectorBuilder<>(this.resetButtonKey, fieldNameKey, valuesArray, value);
    }

    @Override
    public IntFieldBuilder startIntField(Component fieldNameKey, int value) {
        return new IntFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public LongFieldBuilder startLongField(Component fieldNameKey, long value) {
        return new LongFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public FloatFieldBuilder startFloatField(Component fieldNameKey, float value) {
        return new FloatFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public DoubleFieldBuilder startDoubleField(Component fieldNameKey, double value) {
        return new DoubleFieldBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public IntSliderBuilder startIntSlider(Component fieldNameKey, int value, int min, int max) {
        return new IntSliderBuilder(this.resetButtonKey, fieldNameKey, value, min, max);
    }

    @Override
    public LongSliderBuilder startLongSlider(Component fieldNameKey, long value, long min, long max) {
        return new LongSliderBuilder(this.resetButtonKey, fieldNameKey, value, min, max);
    }

    @Override
    public KeyCodeBuilder startModifierKeyCodeField(Component fieldNameKey, ModifierKeyCode value) {
        return new KeyCodeBuilder(this.resetButtonKey, fieldNameKey, value);
    }

    @Override
    public <T> DropdownMenuBuilder<T> startDropdownMenu(Component fieldNameKey, DropdownBoxEntry.SelectionTopCellElement<T> topCellElement, DropdownBoxEntry.SelectionCellCreator<T> cellCreator) {
        return new DropdownMenuBuilder<>(this.resetButtonKey, fieldNameKey, topCellElement, cellCreator);
    }
}