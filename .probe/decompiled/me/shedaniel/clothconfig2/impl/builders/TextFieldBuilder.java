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
public class TextFieldBuilder extends AbstractFieldBuilder<String, StringListEntry, TextFieldBuilder> {

    public TextFieldBuilder(Component resetButtonKey, Component fieldNameKey, String value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.value = value;
    }

    public TextFieldBuilder setErrorSupplier(Function<String, Optional<Component>> errorSupplier) {
        return (TextFieldBuilder) super.setErrorSupplier(errorSupplier);
    }

    public TextFieldBuilder requireRestart() {
        return (TextFieldBuilder) super.requireRestart();
    }

    public TextFieldBuilder setSaveConsumer(Consumer<String> saveConsumer) {
        return (TextFieldBuilder) super.setSaveConsumer(saveConsumer);
    }

    public TextFieldBuilder setDefaultValue(Supplier<String> defaultValue) {
        return (TextFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public TextFieldBuilder setDefaultValue(String defaultValue) {
        return (TextFieldBuilder) super.setDefaultValue(defaultValue);
    }

    public TextFieldBuilder setTooltipSupplier(Function<String, Optional<Component[]>> tooltipSupplier) {
        return (TextFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public TextFieldBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (TextFieldBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public TextFieldBuilder setTooltip(Optional<Component[]> tooltip) {
        return (TextFieldBuilder) super.setTooltip(tooltip);
    }

    public TextFieldBuilder setTooltip(Component... tooltip) {
        return (TextFieldBuilder) super.setTooltip(tooltip);
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