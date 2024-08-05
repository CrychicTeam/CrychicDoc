package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.FloatListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class FloatListBuilder extends AbstractRangeListBuilder<Float, FloatListListEntry, FloatListBuilder> {

    private Function<FloatListListEntry, FloatListListEntry.FloatListCell> createNewInstance;

    public FloatListBuilder(Component resetButtonKey, Component fieldNameKey, List<Float> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = (List<T>) value;
    }

    @Override
    public Function<Float, Optional<Component>> getCellErrorSupplier() {
        return super.getCellErrorSupplier();
    }

    public FloatListBuilder setCellErrorSupplier(Function<Float, Optional<Component>> cellErrorSupplier) {
        return (FloatListBuilder) super.setCellErrorSupplier(cellErrorSupplier);
    }

    public FloatListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        return (FloatListBuilder) super.setDeleteButtonEnabled(deleteButtonEnabled);
    }

    public FloatListBuilder setErrorSupplier(Function<List<Float>, Optional<Component>> errorSupplier) {
        return (FloatListBuilder) super.setErrorSupplier(errorSupplier);
    }

    public FloatListBuilder setInsertInFront(boolean insertInFront) {
        return (FloatListBuilder) super.setInsertInFront(insertInFront);
    }

    public FloatListBuilder setAddButtonTooltip(Component addTooltip) {
        return (FloatListBuilder) super.setAddButtonTooltip(addTooltip);
    }

    public FloatListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        return (FloatListBuilder) super.setRemoveButtonTooltip(removeTooltip);
    }

    public FloatListBuilder requireRestart() {
        return (FloatListBuilder) super.requireRestart();
    }

    public FloatListBuilder setCreateNewInstance(Function<FloatListListEntry, FloatListListEntry.FloatListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }

    public FloatListBuilder setExpanded(boolean expanded) {
        return (FloatListBuilder) super.setExpanded(expanded);
    }

    public FloatListBuilder setSaveConsumer(Consumer<List<Float>> saveConsumer) {
        return (FloatListBuilder) super.setSaveConsumer(saveConsumer);
    }

    public FloatListBuilder setDefaultValue(Supplier<List<Float>> defaultValue) {
        return (FloatListBuilder) super.setDefaultValue(defaultValue);
    }

    public FloatListBuilder setMin(float min) {
        this.min = min;
        return this;
    }

    public FloatListBuilder setMax(float max) {
        this.max = max;
        return this;
    }

    public FloatListBuilder removeMin() {
        return (FloatListBuilder) super.removeMin();
    }

    public FloatListBuilder removeMax() {
        return (FloatListBuilder) super.removeMax();
    }

    public FloatListBuilder setDefaultValue(List<Float> defaultValue) {
        return (FloatListBuilder) super.setDefaultValue(defaultValue);
    }

    public FloatListBuilder setTooltipSupplier(Function<List<Float>, Optional<Component[]>> tooltipSupplier) {
        return (FloatListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (FloatListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public FloatListBuilder setTooltip(Optional<Component[]> tooltip) {
        return (FloatListBuilder) super.setTooltip(tooltip);
    }

    public FloatListBuilder setTooltip(Component... tooltip) {
        return (FloatListBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public FloatListListEntry build() {
        FloatListListEntry entry = new FloatListListEntry(this.getFieldNameKey(), this.value, this.isExpanded(), null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.isRequireRestart(), this.isDeleteButtonEnabled(), this.isInsertInFront());
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