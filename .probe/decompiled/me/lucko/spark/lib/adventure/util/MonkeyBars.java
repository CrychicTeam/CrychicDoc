package me.lucko.spark.lib.adventure.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import org.jetbrains.annotations.NotNull;

public final class MonkeyBars {

    private MonkeyBars() {
    }

    @SafeVarargs
    @NotNull
    public static <E extends Enum<E>> Set<E> enumSet(final Class<E> type, @NotNull final E... constants) {
        Set<E> set = EnumSet.noneOf(type);
        Collections.addAll(set, constants);
        return Collections.unmodifiableSet(set);
    }

    @NotNull
    public static <T> List<T> addOne(@NotNull final List<T> oldList, final T newElement) {
        if (oldList.isEmpty()) {
            return Collections.singletonList(newElement);
        } else {
            List<T> newList = new ArrayList(oldList.size() + 1);
            newList.addAll(oldList);
            newList.add(newElement);
            return Collections.unmodifiableList(newList);
        }
    }
}