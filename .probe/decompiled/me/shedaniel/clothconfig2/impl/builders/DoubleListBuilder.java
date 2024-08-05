package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.DoubleListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class DoubleListBuilder extends AbstractRangeListBuilder<Double, DoubleListListEntry, DoubleListBuilder> {

    private Function<DoubleListListEntry, DoubleListListEntry.DoubleListCell> createNewInstance;

    public DoubleListBuilder(Component resetButtonKey, Component fieldNameKey, List<Double> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = (List<T>) value;
    }

    @Override
    public Function<Double, Optional<Component>> getCellErrorSupplier() {
        return super.getCellErrorSupplier();
    }

    public DoubleListBuilder setCellErrorSupplier(Function<Double, Optional<Component>> cellErrorSupplier) {
        return (DoubleListBuilder) super.setCellErrorSupplier(cellErrorSupplier);
    }

    public DoubleListBuilder setErrorSupplier(Function<List<Double>, Optional<Component>> errorSupplier) {
        this.errorSupplier = (Function<List<T>, Optional<Component>>) errorSupplier;
        return this;
    }

    public DoubleListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        return (DoubleListBuilder) super.setDeleteButtonEnabled(deleteButtonEnabled);
    }

    public DoubleListBuilder setInsertInFront(boolean insertInFront) {
        return (DoubleListBuilder) super.setInsertInFront(insertInFront);
    }

    public DoubleListBuilder setAddButtonTooltip(Component addTooltip) {
        return (DoubleListBuilder) super.setAddButtonTooltip(addTooltip);
    }

    public DoubleListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        return (DoubleListBuilder) super.setRemoveButtonTooltip(removeTooltip);
    }

    public DoubleListBuilder requireRestart() {
        return (DoubleListBuilder) super.requireRestart();
    }

    public DoubleListBuilder setCreateNewInstance(Function<DoubleListListEntry, DoubleListListEntry.DoubleListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }

    public DoubleListBuilder setExpanded(boolean expanded) {
        return (DoubleListBuilder) super.setExpanded(expanded);
    }

    public DoubleListBuilder setSaveConsumer(Consumer<List<Double>> saveConsumer) {
        return (DoubleListBuilder) super.setSaveConsumer(saveConsumer);
    }

    public DoubleListBuilder setDefaultValue(Supplier<List<Double>> defaultValue) {
        return (DoubleListBuilder) super.setDefaultValue(defaultValue);
    }

    public DoubleListBuilder setMin(double min) {
        this.min = min;
        return this;
    }

    public DoubleListBuilder setMax(double max) {
        this.max = max;
        return this;
    }

    public DoubleListBuilder removeMin() {
        return (DoubleListBuilder) super.removeMin();
    }

    public DoubleListBuilder removeMax() {
        return (DoubleListBuilder) super.removeMax();
    }

    public DoubleListBuilder setDefaultValue(List<Double> defaultValue) {
        this.defaultValue = () -> defaultValue;
        return this;
    }

    public DoubleListBuilder setTooltipSupplier(Function<List<Double>, Optional<Component[]>> tooltipSupplier) {
        return (DoubleListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public DoubleListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (DoubleListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public DoubleListBuilder setTooltip(Optional<Component[]> tooltip) {
        return (DoubleListBuilder) super.setTooltip(tooltip);
    }

    public DoubleListBuilder setTooltip(Component... tooltip) {
        return (DoubleListBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public DoubleListListEntry build() {
        DoubleListListEntry entry = new DoubleListListEntry(this.getFieldNameKey(), this.value, this.isExpanded(), null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.requireRestart, this.isDeleteButtonEnabled(), this.isInsertInFront());
        if (this.min != null) {
            entry.setMinimum(this.min);
        }
        if (this.max != null) {
            entry.setMaximum(this.max);
        }
        if (this.createNewInstance != null) {
            entry.setCreateNewInstance(this.createNewInstance);
        }
        entry.setInsertButtonEnabled(this.isInsertButtonEnabled());
        entry.setCellErrorSupplier(this.cellErrorSupplier);
        entry.setTooltipSupplier(() -> (Optional) this.getTooltipSupplier().apply(entry.getValue()));
        entry.setAddTooltip(this.getAddTooltip());
        entry.setRemoveTooltip(this.getRemoveTooltip());
        if (this.errorSupplier != null) {
            entry.setErrorSupplier(() -> (Optional) this.errorSupplier.apply(entry.getValue()));
        }
        return this.finishBuilding(entry);
    }
}