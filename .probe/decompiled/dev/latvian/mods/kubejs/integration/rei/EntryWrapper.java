package dev.latvian.mods.kubejs.integration.rei;

import dev.latvian.mods.kubejs.util.ListJS;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import me.shedaniel.rei.api.common.entry.EntryStack;
import me.shedaniel.rei.api.common.entry.type.EntryType;

public record EntryWrapper<T, C>(EntryType<T> type, Function<Object, C> converter, Function<C, ? extends Predicate<T>> filter, Function<C, ? extends Iterable<T>> entries) {

    public List<EntryStack<T>> entryList(Object input) {
        ArrayList<EntryStack<T>> list = new ArrayList();
        for (Object in : ListJS.orSelf(input)) {
            for (T entry : (Iterable) this.entries.apply(this.converter.apply(in))) {
                list.add(EntryStack.of(this.type, entry));
            }
        }
        return list;
    }

    public Predicate<T> filter(Object input) {
        return (Predicate<T>) this.filter.apply(this.converter.apply(input));
    }
}