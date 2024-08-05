package me.shedaniel.clothconfig2.gui.entries;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public abstract class AbstractListListEntry<T, C extends AbstractListListEntry.AbstractListCell<T, C, SELF>, SELF extends AbstractListListEntry<T, C, SELF>> extends BaseListEntry<T, C, SELF> {

    protected final BiFunction<T, SELF, C> createNewCell;

    protected Function<T, Optional<Component>> cellErrorSupplier;

    protected List<T> original;

    @Internal
    public AbstractListListEntry(Component fieldName, List<T> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<T>> saveConsumer, Supplier<List<T>> defaultValue, Component resetButtonKey, boolean requiresRestart, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<T, SELF, C> createNewCell) {
        super(fieldName, tooltipSupplier, defaultValue, abstractListListEntry -> (AbstractListListEntry.AbstractListCell) createNewCell.apply(null, abstractListListEntry), saveConsumer, resetButtonKey, requiresRestart, deleteButtonEnabled, insertInFront);
        this.createNewCell = createNewCell;
        this.original = new ArrayList(value);
        for (T f : value) {
            this.cells.add((AbstractListListEntry.AbstractListCell) createNewCell.apply(f, this.self()));
        }
        this.widgets.addAll(this.cells);
        this.setExpanded(defaultExpanded);
    }

    public Function<T, Optional<Component>> getCellErrorSupplier() {
        return this.cellErrorSupplier;
    }

    public void setCellErrorSupplier(Function<T, Optional<Component>> cellErrorSupplier) {
        this.cellErrorSupplier = cellErrorSupplier;
    }

    public List<T> getValue() {
        return (List<T>) this.cells.stream().map(AbstractListListEntry.AbstractListCell::getValue).collect(Collectors.toList());
    }

    protected C getFromValue(T value) {
        return (C) this.createNewCell.apply(value, this.self());
    }

    @Override
    public boolean isEdited() {
        if (super.isEdited()) {
            return true;
        } else {
            List<T> value = this.getValue();
            if (value.size() != this.original.size()) {
                return true;
            } else {
                for (int i = 0; i < value.size(); i++) {
                    if (!Objects.equals(value.get(i), this.original.get(i))) {
                        return true;
                    }
                }
                return false;
            }
        }
    }

    @Internal
    public abstract static class AbstractListCell<T, SELF extends AbstractListListEntry.AbstractListCell<T, SELF, OUTER_SELF>, OUTER_SELF extends AbstractListListEntry<T, SELF, OUTER_SELF>> extends BaseListCell {

        protected final OUTER_SELF listListEntry;

        public AbstractListCell(@Nullable T value, OUTER_SELF listListEntry) {
            this.listListEntry = listListEntry;
            this.setErrorSupplier(() -> Optional.ofNullable(listListEntry.cellErrorSupplier).flatMap(cellErrorFn -> (Optional) cellErrorFn.apply(this.getValue())));
        }

        public abstract T getValue();
    }
}