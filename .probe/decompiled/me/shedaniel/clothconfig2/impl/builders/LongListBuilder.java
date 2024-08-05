package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.LongListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class LongListBuilder extends AbstractRangeListBuilder<Long, LongListListEntry, LongListBuilder> {

    private Function<LongListListEntry, LongListListEntry.LongListCell> createNewInstance;

    public LongListBuilder(Component resetButtonKey, Component fieldNameKey, List<Long> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = (List<T>) value;
    }

    @Override
    public Function<Long, Optional<Component>> getCellErrorSupplier() {
        return super.getCellErrorSupplier();
    }

    public LongListBuilder setCellErrorSupplier(Function<Long, Optional<Component>> cellErrorSupplier) {
        return (LongListBuilder) super.setCellErrorSupplier(cellErrorSupplier);
    }

    public LongListBuilder setErrorSupplier(Function<List<Long>, Optional<Component>> errorSupplier) {
        return (LongListBuilder) super.setErrorSupplier(errorSupplier);
    }

    public LongListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        return (LongListBuilder) super.setDeleteButtonEnabled(deleteButtonEnabled);
    }

    public LongListBuilder setInsertInFront(boolean insertInFront) {
        return (LongListBuilder) super.setInsertInFront(insertInFront);
    }

    public LongListBuilder setAddButtonTooltip(Component addTooltip) {
        return (LongListBuilder) super.setAddButtonTooltip(addTooltip);
    }

    public LongListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        return (LongListBuilder) super.setRemoveButtonTooltip(removeTooltip);
    }

    public LongListBuilder requireRestart() {
        return (LongListBuilder) super.requireRestart();
    }

    public LongListBuilder setCreateNewInstance(Function<LongListListEntry, LongListListEntry.LongListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }

    public LongListBuilder setExpanded(boolean expanded) {
        return (LongListBuilder) super.setExpanded(expanded);
    }

    public LongListBuilder setSaveConsumer(Consumer<List<Long>> saveConsumer) {
        return (LongListBuilder) super.setSaveConsumer(saveConsumer);
    }

    public LongListBuilder setDefaultValue(Supplier<List<Long>> defaultValue) {
        return (LongListBuilder) super.setDefaultValue(defaultValue);
    }

    public LongListBuilder setMin(long min) {
        this.min = min;
        return this;
    }

    public LongListBuilder setMax(long max) {
        this.max = max;
        return this;
    }

    public LongListBuilder removeMin() {
        return (LongListBuilder) super.removeMin();
    }

    public LongListBuilder removeMax() {
        return (LongListBuilder) super.removeMax();
    }

    public LongListBuilder setDefaultValue(List<Long> defaultValue) {
        return (LongListBuilder) super.setDefaultValue(defaultValue);
    }

    public LongListBuilder setTooltipSupplier(Function<List<Long>, Optional<Component[]>> tooltipSupplier) {
        return (LongListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (LongListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public LongListBuilder setTooltip(Optional<Component[]> tooltip) {
        return (LongListBuilder) super.setTooltip(tooltip);
    }

    public LongListBuilder setTooltip(Component... tooltip) {
        return (LongListBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public LongListListEntry build() {
        LongListListEntry entry = new LongListListEntry(this.getFieldNameKey(), this.value, this.isExpanded(), null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.isRequireRestart(), this.isDeleteButtonEnabled(), this.isInsertInFront());
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