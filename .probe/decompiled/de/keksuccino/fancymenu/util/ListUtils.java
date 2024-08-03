package de.keksuccino.fancymenu.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ListUtils {

    public static <T> void changeIndexOf(@NotNull List<T> list, @NotNull T object, int newIndex) {
        if (!list.isEmpty()) {
            if (newIndex < 0) {
                newIndex = 0;
            }
            if (newIndex > list.size()) {
                newIndex = list.size();
            }
            int currentIndex = list.indexOf(Objects.requireNonNull(object));
            if (currentIndex != -1) {
                if (currentIndex != newIndex) {
                    list.remove(object);
                    list.add(newIndex, object);
                }
            }
        }
    }

    public static <T> void offsetIndexOf(@NotNull List<T> list, @NotNull T object, int indexOffset) {
        int currentIndex = list.indexOf(Objects.requireNonNull(object));
        if (currentIndex != -1) {
            int newIndex = currentIndex + indexOffset;
            changeIndexOf(list, object, newIndex);
        }
    }

    @Nullable
    public static <T> T getLast(@NotNull List<T> list) {
        return (T) (list.isEmpty() ? null : list.get(list.size() - 1));
    }

    @SafeVarargs
    public static <T> List<T> mergeLists(@NotNull List<T>... lists) {
        List<T> l = new ArrayList();
        for (List<T> list : lists) {
            l.addAll(list);
        }
        return l;
    }

    public static boolean allInListEqual(@NotNull List<?> list) {
        if (list.size() < 2) {
            return true;
        } else {
            Object first = list.get(0);
            for (Object obj : list.subList(1, list.size())) {
                if (!Objects.equals(obj, first)) {
                    return false;
                }
            }
            return true;
        }
    }

    public static <T> boolean contentEqualIgnoreOrder(@NotNull List<T> list1, @NotNull List<T> list2) {
        Objects.requireNonNull(list1);
        Objects.requireNonNull(list2);
        if (list1.size() != list2.size()) {
            return false;
        } else if (list1 == list2) {
            return true;
        } else {
            for (T obj1 : list1) {
                boolean foundMatch = false;
                for (T obj2 : list2) {
                    if (obj1.equals(obj2)) {
                        foundMatch = true;
                        break;
                    }
                }
                if (!foundMatch) {
                    return false;
                }
            }
            return true;
        }
    }

    @Deprecated
    @NotNull
    public static <T> List<T> filterList(@NotNull List<T> listToFilter, @NotNull ConsumingSupplier<T, Boolean> filter) {
        List<T> l = new ArrayList();
        for (T object : listToFilter) {
            if (filter.get(object)) {
                l.add(object);
            }
        }
        listToFilter.clear();
        listToFilter.addAll(l);
        return listToFilter;
    }

    @SafeVarargs
    @NotNull
    public static <T> List<T> of(T... entries) {
        return new ArrayList(Arrays.asList(entries));
    }
}