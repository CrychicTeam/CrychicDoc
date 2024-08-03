package me.shedaniel.clothconfig2.gui.entries;

import com.google.common.collect.Iterators;
import com.google.common.collect.Lists;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;
import me.shedaniel.clothconfig2.api.AbstractConfigEntry;
import me.shedaniel.clothconfig2.api.AbstractConfigListEntry;
import me.shedaniel.clothconfig2.api.ReferenceProvider;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.narration.NarratableEntry;
import net.minecraft.client.gui.narration.NarrationElementOutput;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

@OnlyIn(Dist.CLIENT)
public final class NestedListListEntry<T, INNER extends AbstractConfigListEntry<T>> extends AbstractListListEntry<T, NestedListListEntry.NestedListCell<T, INNER>, NestedListListEntry<T, INNER>> {

    private final List<ReferenceProvider<?>> referencableEntries = Lists.newArrayList();

    @Internal
    public NestedListListEntry(Component fieldName, List<T> value, boolean defaultExpanded, Supplier<Optional<Component[]>> tooltipSupplier, Consumer<List<T>> saveConsumer, Supplier<List<T>> defaultValue, Component resetButtonKey, boolean deleteButtonEnabled, boolean insertInFront, BiFunction<T, NestedListListEntry<T, INNER>, INNER> createNewCell) {
        super(fieldName, value, defaultExpanded, tooltipSupplier, saveConsumer, defaultValue, resetButtonKey, false, deleteButtonEnabled, insertInFront, (t, nestedListListEntry) -> new NestedListListEntry.NestedListCell<>(t, nestedListListEntry, (INNER) createNewCell.apply(t, nestedListListEntry)));
        for (NestedListListEntry.NestedListCell<T, INNER> cell : this.cells) {
            this.referencableEntries.add(cell.nestedEntry);
        }
        this.setReferenceProviderEntries(this.referencableEntries);
    }

    @Override
    public Iterator<String> getSearchTags() {
        return Iterators.concat(super.getSearchTags(), Iterators.concat(this.cells.stream().map(cell -> cell.nestedEntry.getSearchTags()).iterator()));
    }

    public NestedListListEntry<T, INNER> self() {
        return this;
    }

    public static class NestedListCell<T, INNER extends AbstractConfigListEntry<T>> extends AbstractListListEntry.AbstractListCell<T, NestedListListEntry.NestedListCell<T, INNER>, NestedListListEntry<T, INNER>> implements ReferenceProvider<T> {

        private final INNER nestedEntry;

        @Internal
        public NestedListCell(@Nullable T value, NestedListListEntry<T, INNER> listListEntry, INNER nestedEntry) {
            super(value, listListEntry);
            this.nestedEntry = nestedEntry;
        }

        @NotNull
        @Override
        public AbstractConfigEntry<T> provideReferenceEntry() {
            return this.nestedEntry;
        }

        @Override
        public T getValue() {
            return this.nestedEntry.getValue();
        }

        @Override
        public Optional<Component> getError() {
            return this.nestedEntry.getError();
        }

        @Override
        public int getCellHeight() {
            return this.nestedEntry.getItemHeight();
        }

        @Override
        public void render(GuiGraphics graphics, int index, int y, int x, int entryWidth, int entryHeight, int mouseX, int mouseY, boolean isSelected, float delta) {
            this.nestedEntry.setParent(this.listListEntry.getParent());
            this.nestedEntry.setScreen(this.listListEntry.getConfigScreen());
            this.nestedEntry.render(graphics, index, y, x, entryWidth, entryHeight, mouseX, mouseY, isSelected, delta);
        }

        @Override
        public List<? extends GuiEventListener> children() {
            return Collections.singletonList(this.nestedEntry);
        }

        @Override
        public boolean isRequiresRestart() {
            return this.nestedEntry.isRequiresRestart();
        }

        @Override
        public void updateSelected(boolean isSelected) {
            this.nestedEntry.updateSelected(isSelected);
        }

        @Override
        public boolean isEdited() {
            return super.isEdited() || this.nestedEntry.isEdited();
        }

        @Override
        public void onAdd() {
            super.onAdd();
            this.listListEntry.referencableEntries.add(this.nestedEntry);
            this.listListEntry.requestReferenceRebuilding();
        }

        @Override
        public void onDelete() {
            super.onDelete();
            this.listListEntry.referencableEntries.remove(this.nestedEntry);
            this.listListEntry.requestReferenceRebuilding();
        }

        @Override
        public NarratableEntry.NarrationPriority narrationPriority() {
            return NarratableEntry.NarrationPriority.NONE;
        }

        @Override
        public void updateNarration(NarrationElementOutput narrationElementOutput) {
        }
    }
}