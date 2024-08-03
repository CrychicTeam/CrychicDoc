package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.event.EventJS;
import dev.latvian.mods.kubejs.event.EventResult;
import dev.latvian.mods.kubejs.platform.IngredientPlatformHelper;
import dev.latvian.mods.kubejs.util.UtilsJS;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import me.shedaniel.rei.api.client.entry.filtering.base.BasicFilteringRule;
import me.shedaniel.rei.api.client.registry.entry.EntryRegistry;
import me.shedaniel.rei.api.common.entry.EntryStack;
import org.jetbrains.annotations.Nullable;

public class HideREIEventJS<T, C> extends EventJS {

    private final EntryRegistry registry;

    private final EntryWrapper<T, C> entryWrapper;

    private final BasicFilteringRule<?> rule;

    private final List<EntryStack<T>> allEntries;

    private List<T> allValues;

    private final List<Predicate<T>> hide;

    private Predicate<T>[] hideArray;

    private final Collection<EntryStack<T>> hiddenNoFilter;

    public HideREIEventJS(EntryRegistry registry, EntryWrapper<T, C> entryWrapper, BasicFilteringRule<?> rule, EntryStack<?>[] allEntries0) {
        this.registry = registry;
        this.entryWrapper = entryWrapper;
        this.rule = rule;
        this.allEntries = new ArrayList();
        for (EntryStack<?> entry : allEntries0) {
            if (entry.getType() == entryWrapper.type()) {
                this.allEntries.add(entry.cast());
            }
        }
        this.allValues = null;
        this.hide = new ArrayList();
        this.hiddenNoFilter = new ArrayList();
    }

    public List<T> getAllEntryValues() {
        if (this.allValues == null) {
            this.allValues = this.allEntries.stream().map(EntryStack::getValue).toList();
        }
        return this.allValues;
    }

    public void hide(Object entries, @Nullable Object except) {
        if (this.hide != UtilsJS.ALWAYS_TRUE) {
            Predicate<T> filter = this.entryWrapper.filter(entries);
            if (except != null) {
                filter = filter.and(this.entryWrapper.filter(except).negate());
            }
            this.hide.add(filter);
        }
    }

    public void hide(Object entries) {
        this.hide(entries, null);
    }

    public void hideNoFilter(Object o) {
        this.hiddenNoFilter.addAll(this.entryWrapper.entryList(o));
    }

    public void hideAll(@Nullable Object except) {
        this.hide(IngredientPlatformHelper.get().wildcard(), except);
    }

    public void hideAll() {
        this.hideAll(null);
    }

    @Override
    protected void afterPosted(EventResult result) {
        if (this.hide != null) {
            this.hideArray = (Predicate<T>[]) this.hide.toArray(new Predicate[0]);
            this.rule.hide(this.allEntries.stream().filter(this::testEntry).toList());
        }
        if (!this.hiddenNoFilter.isEmpty()) {
            this.hiddenNoFilter.forEach(this.registry::removeEntry);
        }
    }

    private boolean testEntry(EntryStack<T> e) {
        for (Predicate<T> h : this.hideArray) {
            if (h.test(e.getValue())) {
                return true;
            }
        }
        return false;
    }
}