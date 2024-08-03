package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.IntegerSliderEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IntSliderBuilder extends AbstractSliderFieldBuilder<Integer, IntegerSliderEntry, IntSliderBuilder> {

    public IntSliderBuilder(Component resetButtonKey, Component fieldNameKey, int value, int min, int max) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public IntSliderBuilder setErrorSupplier(Function<Integer, Optional<Component>> errorSupplier) {
        return (IntSliderBuilder) super.setErrorSupplier(errorSupplier);
    }

    public IntSliderBuilder requireRestart() {
        return (IntSliderBuilder) super.requireRestart();
    }

    public IntSliderBuilder setTextGetter(Function<Integer, Component> textGetter) {
        return (IntSliderBuilder) super.setTextGetter(textGetter);
    }

    public IntSliderBuilder setSaveConsumer(Consumer<Integer> saveConsumer) {
        return (IntSliderBuilder) super.setSaveConsumer(saveConsumer);
    }

    public IntSliderBuilder setDefaultValue(Supplier<Integer> defaultValue) {
        return (IntSliderBuilder) super.setDefaultValue(defaultValue);
    }

    public IntSliderBuilder setDefaultValue(int defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public IntSliderBuilder setTooltipSupplier(Function<Integer, Optional<Component[]>> tooltipSupplier) {
        return (IntSliderBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntSliderBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (IntSliderBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntSliderBuilder setTooltip(Optional<Component[]> tooltip) {
        return (IntSliderBuilder) super.setTooltip(tooltip);
    }

    public IntSliderBuilder setTooltip(Component... tooltip) {
        return (IntSliderBuilder) super.setTooltip(tooltip);
    }

    public IntSliderBuilder setMax(int max) {
        this.max = max;
        return this;
    }

    public IntSliderBuilder setMin(int min) {
        this.min = min;
        return this;
    }

    public IntSliderBuilder removeMin() {
        return this;
    }

    public IntSliderBuilder removeMax() {
        return this;
    }

    @NotNull
    public IntegerSliderEntry build() {
        IntegerSliderEntry entry = new IntegerSliderEntry(this.getFieldNameKey(), this.min, this.max, this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
        if (this.textGetter != null) {
            entry.setTextGetter(this.textGetter);
        }
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}