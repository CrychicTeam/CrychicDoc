package me.shedaniel.clothconfig2.impl.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.StringListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StringFieldBuilder extends AbstractFieldBuilder<String, StringListEntry, StringFieldBuilder> {

    public StringFieldBuilder(Component resetButtonKey, Component fieldNameKey, String value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.value = value;
    }

    public StringFieldBuilder setErrorSupplier(Function<String, Optional<Component>> errorSupplier) {
        return (StringFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public StringFieldBuilder requireRestart() {
        return (StringFieldBuilder) super.requireRestart();
    }

    public StringFieldBuilder setSaveConsumer(Consumer<String> saveConsumer) {
        return (StringFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public StringFieldBuilder setDefaultValue(Supplier<String> defaultValue) {
        return (StringFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public StringFieldBuilder setDefaultValue(String defaultValue) {
        return (StringFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public StringFieldBuilder setTooltipSupplier(Function<String, Optional<Component[]>> tooltipSupplier) {
        return (StringFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public StringFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (StringFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public StringFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (StringFieldBuilder) super.setTooltip(tooltip);
    }

    public StringFieldBuilder setTooltip(Component... tooltip) {
        return (StringFieldBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public StringListEntry build() {
        StringListEntry entry = new StringListEntry(this.getFieldNameKey(), this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), null, this.isRequireRestart());
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}