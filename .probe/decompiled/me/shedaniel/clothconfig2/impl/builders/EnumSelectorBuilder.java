package me.shedaniel.clothconfig2.impl.builders;

import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.EnumListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class EnumSelectorBuilder<T extends Enum<?>> extends AbstractFieldBuilder<T, EnumListEntry<T>, EnumSelectorBuilder<T>> {

    private final Class<T> clazz;

    private Function<Enum, Component> enumNameProvider = EnumListEntry.DEFAULT_NAME_PROVIDER;

    public EnumSelectorBuilder(Component resetButtonKey, Component fieldNameKey, Class<T> clazz, T value) {
        super(resetButtonKey, fieldNameKey);
        Objects.requireNonNull(clazz);
        Objects.requireNonNull(value);
        this.value = value;
        this.clazz = clazz;
    }

    public EnumSelectorBuilder<T> setErrorSupplier(Function<T, Optional<Component>> errorSupplier) {
        return (EnumSelectorBuilder<T>) super.setErrorSupplier(errorSupplier);
    }

    public EnumSelectorBuilder<T> requireRestart() {
        return (EnumSelectorBuilder<T>) super.requireRestart();
    }

    public EnumSelectorBuilder<T> setSaveConsumer(Consumer<T> saveConsumer) {
        return (EnumSelectorBuilder<T>) super.setSaveConsumer(saveConsumer);
    }

    public EnumSelectorBuilder<T> setDefaultValue(Supplier<T> defaultValue) {
        return (EnumSelectorBuilder<T>) super.setDefaultValue(defaultValue);
    }

    public EnumSelectorBuilder<T> setDefaultValue(T defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public EnumSelectorBuilder<T> setTooltipSupplier(Function<T, Optional<Component[]>> tooltipSupplier) {
        return (EnumSelectorBuilder<T>) super.setTooltipSupplier(tooltipSupplier);
    }

    public EnumSelectorBuilder<T> setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (EnumSelectorBuilder<T>) super.setTooltipSupplier(tooltipSupplier);
    }

    public EnumSelectorBuilder<T> setTooltip(Optional<Component[]> tooltip) {
        return (EnumSelectorBuilder<T>) super.setTooltip(tooltip);
    }

    public EnumSelectorBuilder<T> setTooltip(Component... tooltip) {
        return (EnumSelectorBuilder<T>) super.setTooltip(tooltip);
    }

    public EnumSelectorBuilder<T> setEnumNameProvider(Function<Enum, Component> enumNameProvider) {
        Objects.requireNonNull(enumNameProvider);
        this.enumNameProvider = enumNameProvider;
        return this;
    }

    @NotNull
    public EnumListEntry<T> build() {
        EnumListEntry<T> entry = new EnumListEntry<>(this.getFieldNameKey(), this.clazz, this.value, this.getResetButtonKey(), this.defaultValue, this.getSaveConsumer(), this.enumNameProvider, null, this.isRequireRestart());
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}