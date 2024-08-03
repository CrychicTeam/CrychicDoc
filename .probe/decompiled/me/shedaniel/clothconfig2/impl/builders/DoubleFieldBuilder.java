package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.DoubleListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DoubleFieldBuilder extends AbstractRangeFieldBuilder<Double, DoubleListEntry, DoubleFieldBuilder> {

    public DoubleFieldBuilder(Component resetButtonKey, Component fieldNameKey, double value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public DoubleFieldBuilder setErrorSupplier(Function<Double, Optional<Component>> errorSupplier) {
        return (DoubleFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public DoubleFieldBuilder requireRestart() {
        return (DoubleFieldBuilder) super.requireRestart();
    }

    public DoubleFieldBuilder setSaveConsumer(Consumer<Double> saveConsumer) {
        return (DoubleFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public DoubleFieldBuilder setDefaultValue(Supplier<Double> defaultValue) {
        return (DoubleFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public DoubleFieldBuilder setDefaultValue(double defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public DoubleFieldBuilder setMin(double min) {
        this.min = min;
        return this;
    }

    public DoubleFieldBuilder setMax(double max) {
        this.max = max;
        return this;
    }

    public DoubleFieldBuilder removeMin() {
        return (DoubleFieldBuilder) super.removeMin();
    }

    public DoubleFieldBuilder removeMax() {
        return (DoubleFieldBuilder) super.removeMax();
    }

    public DoubleFieldBuilder setTooltipSupplier(Function<Double, Optional<Component[]>> tooltipSupplier) {
        return (DoubleFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public DoubleFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (DoubleFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public DoubleFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (DoubleFieldBuilder) super.setTooltip(tooltip);
    }

    public DoubleFieldBuilder setTooltip(Component... tooltip) {
        return (DoubleFieldBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public DoubleListEntry build() {
        DoubleListEntry entry = new DoubleListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
        if (this.min != null) {
            entry.setMinimum(this.min);
        }
        if (this.max != null) {
            entry.setMaximum(this.max);
        }
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}