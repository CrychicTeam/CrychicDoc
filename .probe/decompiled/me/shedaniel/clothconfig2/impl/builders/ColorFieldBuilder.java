package me.shedaniel.clothconfig2.impl.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.ColorEntry;
import me.shedaniel.math.Color;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextColor;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class ColorFieldBuilder extends AbstractFieldBuilder<Integer, ColorEntry, ColorFieldBuilder> {

    private boolean alpha = false;

    public ColorFieldBuilder(Component resetButtonKey, Component fieldNameKey, int value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public ColorFieldBuilder setErrorSupplier(Function<Integer, Optional<Component>> errorSupplier) {
        return (ColorFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public ColorFieldBuilder requireRestart() {
        return (ColorFieldBuilder) super.requireRestart();
    }

    public ColorFieldBuilder setSaveConsumer(Consumer<Integer> saveConsumer) {
        return (ColorFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public ColorFieldBuilder setSaveConsumer2(Consumer<Color> saveConsumer) {
        return (ColorFieldBuilder) super.setSaveConsumer(integer -> saveConsumer.accept(this.alpha ? Color.ofTransparent(integer) : Color.ofOpaque(integer)));
    }

    public ColorFieldBuilder setSaveConsumer3(Consumer<TextColor> saveConsumer) {
        return (ColorFieldBuilder) super.setSaveConsumer(integer -> saveConsumer.accept(TextColor.fromRgb(integer)));
    }

    public ColorFieldBuilder setDefaultValue(Supplier<Integer> defaultValue) {
        return (ColorFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public ColorFieldBuilder setDefaultValue2(Supplier<Color> defaultValue) {
        this.defaultValue = () -> ((Color) defaultValue.get()).getColor();
        return this;
    }

    public ColorFieldBuilder setDefaultValue3(Supplier<TextColor> defaultValue) {
        this.defaultValue = () -> ((TextColor) defaultValue.get()).getValue();
        return this;
    }

    public ColorFieldBuilder setAlphaMode(boolean withAlpha) {
        this.alpha = withAlpha;
        return this;
    }

    public ColorFieldBuilder setDefaultValue(int defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public ColorFieldBuilder setDefaultValue(TextColor defaultValue) {
        this.defaultValue = () -> ((TextColor) Objects.requireNonNull(defaultValue)).getValue();
        return this;
    }

    public ColorFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (ColorFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public ColorFieldBuilder setTooltipSupplier(Function<Integer, Optional<Component[]>> tooltipSupplier) {
        return (ColorFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public ColorFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (ColorFieldBuilder) super.setTooltip(tooltip);
    }

    public ColorFieldBuilder setTooltip(Component... tooltip) {
        return (ColorFieldBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public ColorEntry build() {
        ColorEntry entry = new ColorEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
        if (this.alpha) {
            entry.withAlpha();
        } else {
            entry.withoutAlpha();
        }
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}