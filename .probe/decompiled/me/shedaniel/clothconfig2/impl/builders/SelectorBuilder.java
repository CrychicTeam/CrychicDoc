package me.shedaniel.clothconfig2.impl.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.SelectionListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class SelectorBuilder<T> extends AbstractFieldBuilder<T, SelectionListEntry<T>, SelectorBuilder<T>> {

    private final T[] valuesArray;

    private Function<T, Component> nameProvider = null;

    public SelectorBuilder(Component resetButtonKey, Component fieldNameKey, T[] valuesArray, T value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(value);
        this.valuesArray = valuesArray;
        this.value = value;
    }

    public SelectorBuilder<T> setErrorSupplier(Function<T, Optional<Component>> errorSupplier) {
        return (SelectorBuilder<T>) super.setErrorSupplier(errorSupplier);
    }

    public SelectorBuilder<T> requireRestart() {
        return (SelectorBuilder<T>) super.requireRestart();
    }

    public SelectorBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        return (SelectorBuilder<T>) super.setSaveConsumer(saveConsumer);
    }

    public SelectorBuilder<T> setDefaultValue(Supplier<T> defaultValue) {
        return (SelectorBuilder<T>) super.setDefaultValue(defaultValue);
    }

    public SelectorBuilder<T> setDefaultValue(T defaultValue) {
        return (SelectorBuilder<T>) super.setDefaultValue(defaultValue);
    }

    public SelectorBuilder<T> setTooltipSupplier(Function<T, Optional<Component[]>> tooltipSupplier) {
        return (SelectorBuilder<T>) super.setTooltipSupplier(tooltipSupplier);
    }

    public SelectorBuilder<T> setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (SelectorBuilder<T>) super.setTooltipSupplier(tooltipSupplier);
    }

    public SelectorBuilder<T> setTooltip(Optional<Component[]> tooltip) {
        return (SelectorBuilder<T>) super.setTooltip(tooltip);
    }

    public SelectorBuilder<T> setTooltip(Component... tooltip) {
        return (SelectorBuilder<T>) super.setTooltip(tooltip);
    }

    public SelectorBuilder<T> setNameProvider(Function<T, Component> enumNameProvider) {
        this.nameProvider = enumNameProvider;
        return this;
    }

    @NotNull
    public SelectionListEntry<T> build() {
        SelectionListEntry<T> entry = new SelectionListEntry<>(this.getFieldNameKey(), this.valuesArray, this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), this.nameProvider, null, this.isRequireRestart());
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}