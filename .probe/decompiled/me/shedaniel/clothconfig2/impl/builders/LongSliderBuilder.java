package me.shedaniel.clothconfig2.impl.builders;

import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.LongSliderEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LongSliderBuilder extends AbstractSliderFieldBuilder<Long, LongSliderEntry, LongSliderBuilder> {

    public LongSliderBuilder(Component resetButtonKey, Component fieldNameKey, long value, long min, long max) {
        super(resetButtonKey, fieldNameKey);
        this.value = value;
        this.max = max;
        this.min = min;
    }

    public LongSliderBuilder setErrorSupplier(Function<Long, Optional<Component>> errorSupplier) {
        return (LongSliderBuilder) super.setErrorSupplier(errorSupplier);
    }

    public LongSliderBuilder requireRestart() {
        return (LongSliderBuilder) super.requireRestart();
    }

    public LongSliderBuilder setTextGetter(Function<Long, Component> textGetter) {
        return (LongSliderBuilder) super.setTextGetter(textGetter);
    }

    public LongSliderBuilder setSaveConsumer(Consumer<Long> saveConsumer) {
        return (LongSliderBuilder) super.setSaveConsumer(saveConsumer);
    }

    public LongSliderBuilder setDefaultValue(Supplier<Long> defaultValue) {
        return (LongSliderBuilder) super.setDefaultValue(defaultValue);
    }

    public LongSliderBuilder setDefaultValue(long defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public LongSliderBuilder setTooltipSupplier(Function<Long, Optional<Component[]>> tooltipSupplier) {
        return (LongSliderBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongSliderBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (LongSliderBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongSliderBuilder setTooltip(Optional<Component[]> tooltip) {
        return (LongSliderBuilder) super.setTooltip(tooltip);
    }

    public LongSliderBuilder setTooltip(Component... tooltip) {
        return (LongSliderBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public LongSliderEntry build() {
        LongSliderEntry entry = new LongSliderEntry(this.getFieldNameKey(), this.min, this.max, this.value, this.getSaveConsumer(), this.getResetButtonKey(), this.defaultValue, null, this.isRequireRestart());
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