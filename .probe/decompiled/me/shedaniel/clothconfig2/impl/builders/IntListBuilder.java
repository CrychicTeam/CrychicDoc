package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.IntegerListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class IntListBuilder extends AbstractRangeListBuilder<Integer, IntegerListListEntry, IntListBuilder> {

    private Function<IntegerListListEntry, IntegerListListEntry.IntegerListCell> createNewInstance;

    public IntListBuilder(Component resetButtonKey, Component fieldNameKey, List<Integer> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = (List<T>) value;
    }

    @Override
    public Function<Integer, Optional<Component>> getCellErrorSupplier() {
        return super.getCellErrorSupplier();
    }

    public IntListBuilder setCellErrorSupplier(Function<Integer, Optional<Component>> cellErrorSupplier) {
        return (IntListBuilder) super.setCellErrorSupplier(cellErrorSupplier);
    }

    public IntListBuilder setErrorSupplier(Function<List<Integer>, Optional<Component>> errorSupplier) {
        return (IntListBuilder) super.setErrorSupplier(errorSupplier);
    }

    public IntListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        return (IntListBuilder) super.setDeleteButtonEnabled(deleteButtonEnabled);
    }

    public IntListBuilder setInsertInFront(boolean insertInFront) {
        return (IntListBuilder) super.setInsertInFront(insertInFront);
    }

    public IntListBuilder setAddButtonTooltip(Component addTooltip) {
        return (IntListBuilder) super.setAddButtonTooltip(addTooltip);
    }

    public IntListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        return (IntListBuilder) super.setRemoveButtonTooltip(removeTooltip);
    }

    public IntListBuilder requireRestart() {
        return (IntListBuilder) super.requireRestart();
    }

    public IntListBuilder setCreateNewInstance(Function<IntegerListListEntry, IntegerListListEntry.IntegerListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }

    public IntListBuilder setExpanded(boolean expanded) {
        return (IntListBuilder) super.setExpanded(expanded);
    }

    public IntListBuilder setSaveConsumer(Consumer<List<Integer>> saveConsumer) {
        return (IntListBuilder) super.setSaveConsumer(saveConsumer);
    }

    public IntListBuilder setDefaultValue(Supplier<List<Integer>> defaultValue) {
        return (IntListBuilder) super.setDefaultValue(defaultValue);
    }

    public IntListBuilder setMin(int min) {
        this.min = min;
        return this;
    }

    public IntListBuilder setMax(int max) {
        this.max = max;
        return this;
    }

    public IntListBuilder removeMin() {
        return (IntListBuilder) super.removeMin();
    }

    public IntListBuilder removeMax() {
        return (IntListBuilder) super.removeMax();
    }

    public IntListBuilder setDefaultValue(List<Integer> defaultValue) {
        return (IntListBuilder) super.setDefaultValue(defaultValue);
    }

    public IntListBuilder setTooltipSupplier(Function<List<Integer>, Optional<Component[]>> tooltipSupplier) {
        return (IntListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (IntListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public IntListBuilder setTooltip(Optional<Component[]> tooltip) {
        return (IntListBuilder) super.setTooltip(tooltip);
    }

    public IntListBuilder setTooltip(Component... tooltip) {
        return (IntListBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public IntegerListListEntry build() {
        IntegerListListEntry entry = new IntegerListListEntry(this.getFieldNameKey(), this.value, this.isExpanded(), null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.isRequireRestart(), this.isDeleteButtonEnabled(), this.isInsertInFront());
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