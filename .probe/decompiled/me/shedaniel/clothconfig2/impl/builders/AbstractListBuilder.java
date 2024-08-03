package me.shedaniel.clothconfig2.impl.builders;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import net.minecraft.network.chat.Component;

public abstract class AbstractListBuilder<T, A extends AbstractConfigListEntry, SELF extends AbstractListBuilder<T, A, SELF>> extends AbstractFieldBuilder<List<T>, A, SELF> {

    protected Function<T, Optional<Component>> cellErrorSupplier;

    private boolean expanded = false;

    private Component addTooltip = Component.translatable("text.cloth-config.list.add");

    private Component removeTooltip = Component.translatable("text.cloth-config.list.remove");

    private boolean insertButtonEnabled = true;

    private boolean deleteButtonEnabled = true;

    private boolean insertInFront = false;

    protected AbstractListBuilder(Component resetButtonKey, Component fieldNameKey) {
        super(resetButtonKey, fieldNameKey);
    }

    public Function<T, Optional<Component>> getCellErrorSupplier() {
        return this.cellErrorSupplier;
    }

    public SELF setCellErrorSupplier(Function<T, Optional<Component>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
        return (SELF) this;
    }

    public SELF setDeleteButtonEnabled(boolean deleteButtonEnabled) {
        this.deleteButtonEnabled = deleteButtonEnabled;
        return (SELF) this;
    }

    public SELF setInsertButtonEnabled(boolean insertButtonEnabled) {
        this.insertButtonEnabled = insertButtonEnabled;
        return (SELF) this;
    }

    public SELF setInsertInFront(boolean insertInFront) {
        this.insertInFront = insertInFront;
        return (SELF) this;
    }

    public SELF setAddButtonTooltip(Component addTooltip) {
        this.addTooltip = addTooltip;
        return (SELF) this;
    }

    public SELF setRemoveButtonTooltip(Component removeTooltip) {
        this.removeTooltip = removeTooltip;
        return (SELF) this;
    }

    public SELF setExpanded(boolean expanded) {
        this.expanded = expanded;
        return (SELF) this;
    }

    public boolean isExpanded() {
        return this.expanded;
    }

    public Component getAddTooltip() {
        return this.addTooltip;
    }

    public Component getRemoveTooltip() {
        return this.removeTooltip;
    }

    public boolean isInsertButtonEnabled() {
        return this.insertButtonEnabled;
    }

    public boolean isDeleteButtonEnabled() {
        return this.deleteButtonEnabled;
    }

    public boolean isInsertInFront() {
        return this.insertInFront;
    }
}