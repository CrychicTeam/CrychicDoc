package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.gui.entries.StringListListEntry;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;

@OnlyIn(Dist.CLIENT)
public class StringListBuilder extends AbstractListBuilder<String, StringListListEntry, StringListBuilder> {

    private Function<StringListListEntry, StringListListEntry.StringListCell> createNewInstance;

    public StringListBuilder(Component resetButtonKey, Component fieldNameKey, List<String> value) {
        super(resetButtonKey, fieldNameKey);
        this.value = (List<T>) value;
    }

    @Override
    public Function<String, Optional<Component>> getCellErrorSupplier() {
        return super.getCellErrorSupplier();
    }

    public StringListBuilder setCellErrorSupplier(Function<String, Optional<Component>> cellErrorSupplier) {
        return (StringListBuilder) super.setCellErrorSupplier(cellErrorSupplier);
    }

    public StringListBuilder setErrorSupplier(Function<List<String>, Optional<Component>> errorSupplier) {
        return (StringListBuilder) super.setErrorSupplier(errorSupplier);
    }

    public StringListBuilder setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        return (StringListBuilder) super.setDeleteButtonEnabled(deleteButtonEnabled);
    }

    public StringListBuilder setInsertInFront(boolean insertInFront) {
        return (StringListBuilder) super.setInsertInFront(insertInFront);
    }

    public StringListBuilder setAddButtonTooltip(Component addTooltip) {
        return (StringListBuilder) super.setAddButtonTooltip(addTooltip);
    }

    public StringListBuilder setRemoveButtonTooltip(Component removeTooltip) {
        return (StringListBuilder) super.setRemoveButtonTooltip(removeTooltip);
    }

    public StringListBuilder requireRestart() {
        return (StringListBuilder) super.requireRestart();
    }

    public StringListBuilder setCreateNewInstance(Function<StringListListEntry, StringListListEntry.StringListCell> createNewInstance) {
        this.createNewInstance = createNewInstance;
        return this;
    }

    public StringListBuilder setExpanded(boolean expanded) {
        return (StringListBuilder) super.setExpanded(expanded);
    }

    public StringListBuilder setSaveConsumer(Consumer<List<String>> saveConsumer) {
        return (StringListBuilder) super.setSaveConsumer(saveConsumer);
    }

    public StringListBuilder setDefaultValue(Supplier<List<String>> defaultValue) {
        return (StringListBuilder) super.setDefaultValue(defaultValue);
    }

    public StringListBuilder setDefaultValue(List<String> defaultValue) {
        return (StringListBuilder) super.setDefaultValue(defaultValue);
    }

    public StringListBuilder setTooltipSupplier(Function<List<String>, Optional<Component[]>> tooltipSupplier) {
        return (StringListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public StringListBuilder setTooltipSupplier(Supplier<Optional<Component[]>> tooltipSupplier) {
        return (StringListBuilder) super.setTooltipSupplier(tooltipSupplier);
    }

    public StringListBuilder setTooltip(Optional<Component[]> tooltip) {
        return (StringListBuilder) super.setTooltip(tooltip);
    }

    public StringListBuilder setTooltip(Component... tooltip) {
        return (StringListBuilder) super.setTooltip(tooltip);
    }

    @NotNull
    public StringListListEntry build() {
        StringListListEntry entry = new StringListListEntry(this.getFieldNameKey(), this.value, this.isExpanded(), null, this.getSaveConsumer(), this.defaultValue, this.getResetButtonKey(), this.isRequireRestart(), this.isDeleteButtonEnabled(), this.isInsertInFront());
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