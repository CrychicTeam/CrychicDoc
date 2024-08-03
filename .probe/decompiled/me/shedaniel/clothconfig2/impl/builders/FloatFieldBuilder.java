package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.FloatListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FloatFieldBuilder extends AbstractRangeFieldBuilder<Float, FloatListEntry, FloatFieldBuilder> {

    public FloatFieldBuilder(Component resetButtonKey, Component fieldNameKey, float value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public FloatFieldBuilder setErrorSupplier(Function<Float, Optional<Component>> errorSupplier) {
        return (FloatFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public FloatFieldBuilder requireRestart() {
        return (FloatFieldBuilder) super.requireRestart();
    }

    public FloatFieldBuilder setSaveConsumer(Consumer<Float> saveConsumer) {
        return (FloatFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public FloatFieldBuilder setDefaultValue(Supplier<Float> defaultValue) {
        return (FloatFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public FloatFieldBuilder setDefaultValue(float defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public FloatFieldBuilder setTooltipSupplier(Function<Float, Optional<Component[]>> tooltipSupplier) {
        return (FloatFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (FloatFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (FloatFieldBuilder) super.setTooltip(tooltip);
    }

    public FloatFieldBuilder setTooltip(Component... tooltip) {
        return (FloatFieldBuilder) super.setTooltip(tooltip);
    }

    public FloatFieldBuilder setMin(float min) {
        this.min = min;
        return this;
    }

    public FloatFieldBuilder setMax(float max) {
        this.max = max;
        return this;
    }

    public FloatFieldBuilder removeMin() {
        return (FloatFieldBuilder) super.removeMin();
    }

    public FloatFieldBuilder removeMax() {
        return (FloatFieldBuilder) super.removeMax();
    }

    @NotNull
    public FloatListEntry build() {
        FloatListEntry entry = new FloatListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
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