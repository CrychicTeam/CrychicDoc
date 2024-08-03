package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.IntegerListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IntFieldBuilder extends AbstractRangeFieldBuilder<Integer, IntegerListEntry, IntFieldBuilder> {

    public IntFieldBuilder(Component resetButtonKey, Component fieldNameKey, int value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public IntFieldBuilder requireRestart() {
        return (IntFieldBuilder) super.requireRestart();
    }

    public IntFieldBuilder setErrorSupplier(Function<Integer, Optional<Component>> errorSupplier) {
        return (IntFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public IntFieldBuilder setSaveConsumer(Consumer<Integer> saveConsumer) {
        return (IntFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public IntFieldBuilder setDefaultValue(Supplier<Integer> defaultValue) {
        return (IntFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public IntFieldBuilder setDefaultValue(int defaultValue) {
        return (IntFieldBuilder) super.setDefaultValue(Integer.valueOf(defaultValue));
    }

    public IntFieldBuilder setTooltipSupplier(Function<Integer, Optional<Component[]>> tooltipSupplier) {
        return (IntFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (IntFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (IntFieldBuilder) super.setTooltip(tooltip);
    }

    public IntFieldBuilder setTooltip(Component... tooltip) {
        return (IntFieldBuilder) super.setTooltip(tooltip);
    }

    public IntFieldBuilder setMin(int min) {
        return (IntFieldBuilder) super.setMin(min);
    }

    public IntFieldBuilder setMax(int max) {
        return (IntFieldBuilder) super.setMax(max);
    }

    public IntFieldBuilder removeMin() {
        return (IntFieldBuilder) super.removeMin();
    }

    public IntFieldBuilder removeMax() {
        return (IntFieldBuilder) super.removeMax();
    }

    @NotNull
    public IntegerListEntry build() {
        IntegerListEntry entry = new IntegerListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
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