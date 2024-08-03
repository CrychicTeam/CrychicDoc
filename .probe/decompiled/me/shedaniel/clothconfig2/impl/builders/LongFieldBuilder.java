package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.LongListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LongFieldBuilder extends AbstractRangeFieldBuilder<Long, LongListEntry, LongFieldBuilder> {

    public LongFieldBuilder(Component resetButtonKey, Component fieldNameKey, long value) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
    }

    public LongFieldBuilder setErrorSupplier(Function<Long, Optional<Component>> errorSupplier) {
        return (LongFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public LongFieldBuilder requireRestart() {
        return (LongFieldBuilder) super.requireRestart();
    }

    public LongFieldBuilder setSaveConsumer(Consumer<Long> saveConsumer) {
        return (LongFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public LongFieldBuilder setDefaultValue(Supplier<Long> defaultValue) {
        return (LongFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public LongFieldBuilder setDefaultValue(long defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public LongFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (LongFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongFieldBuilder setTooltipSupplier(Function<Long, Optional<Component[]>> tooltipSupplier) {
        return (LongFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (LongFieldBuilder) super.setTooltip(tooltip);
    }

    public LongFieldBuilder setTooltip(Component... tooltip) {
        return (LongFieldBuilder) super.setTooltip(tooltip);
    }

    public LongFieldBuilder setMin(long min) {
        this.min = min;
        return this;
    }

    public LongFieldBuilder setMax(long max) {
        this.max = max;
        return this;
    }

    public LongFieldBuilder removeMin() {
        return (LongFieldBuilder) super.removeMin();
    }

    public LongFieldBuilder removeMax() {
        return (LongFieldBuilder) super.removeMax();
    }

    @NotNull
    public LongListEntry build() {
        LongListEntry entry = new LongListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
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