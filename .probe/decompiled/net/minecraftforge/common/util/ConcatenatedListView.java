package net.minecraftforge.common.util;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class ConcatenatedListView<T> implements List<T> {

    private final List<? extends List<? extends T>> lists;

    @SafeVarargs
    public static <T> ConcatenatedListView<T> of(List<T>... lists) {
        return new ConcatenatedListView<>(List.of(lists));
    }

    public static <T> List<T> of(List<? extends List<? extends T>> members) {
        return (List<T>) (switch(members.size()) {
            case 0 ->
                List.of();
            case 1 ->
                Collections.unmodifiableList((List) members.get(0));
            default ->
                new ConcatenatedListView(members);
        });
    }

    private ConcatenatedListView(List<? extends List<? extends T>> lists) {
        this.lists = lists;
    }

    public int size() {
        int size = 0;
        for (List<? extends T> list : this.lists) {
            size += list.size();
        }
        return size;
    }

    public boolean isEmpty() {
        for (List<? extends T> list : this.lists) {
            if (!list.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean contains(Object o) {
        for (List<? extends T> list : this.lists) {
            if (list.contains(o)) {
                return true;
            }
        }
        return false;
    }

    public T get(int index) {
        for (List<? extends T> list : this.lists) {
            int size = list.size();
            if (index < size) {
                return (T) list.get(index);
            }
            index -= size;
        }
        throw new IndexOutOfBoundsException(index);
    }

    public int indexOf(Object o) {
        int offset = 0;
        for (List<? extends T> list : this.lists) {
            int foundIndex = list.indexOf(o);
            if (foundIndex >= 0) {
                return offset + foundIndex;
            }
            offset += list.size();
        }
        return -1;
    }

    public int lastIndexOf(Object o) {
        int offset = 0;
        for (List<? extends T> list : Lists.reverse(this.lists)) {
            int foundIndex = list.lastIndexOf(o);
            if (foundIndex >= 0) {
                return offset + foundIndex;
            }
            offset += list.size();
        }
        return -1;
    }

    @NotNull
    public Iterator<T> iterator() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.lists)).iterator();
    }

    public Spliterator<T> spliterator() {
        return Iterables.unmodifiableIterable(Iterables.concat(this.lists)).spliterator();
    }

    private <C extends Collection<T>> C concatenate(Supplier<C> collectionFactory) {
        C concat = (C) collectionFactory.get();
        for (List<? extends T> list : this.lists) {
            concat.addAll(list);
        }
        return concat;
    }

    @NotNull
    public Object[] toArray() {
        return ((ArrayList) this.concatenate(ArrayList::new)).toArray();
    }

    @NotNull
    public <T1> T1[] toArray(@NotNull T1[] a) {
        return (T1[]) ((ArrayList) this.concatenate(ArrayList::new)).toArray(a);
    }

    public boolean containsAll(@NotNull Collection<?> c) {
        return ((HashSet) this.concatenate(HashSet::new)).containsAll(c);
    }

    public boolean add(T t) {
        throw new UnsupportedOperationException();
    }

    public void add(int index, T element) {
        throw new UnsupportedOperationException();
    }

    public T set(int index, T element) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(@NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    public boolean addAll(int index, @NotNull Collection<? extends T> c) {
        throw new UnsupportedOperationException();
    }

    public boolean remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public T remove(int index) {
        throw new UnsupportedOperationException();
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public ListIterator<T> listIterator() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public ListIterator<T> listIterator(int index) {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public List<T> subList(int fromIndex, int toIndex) {
        throw new UnsupportedOperationException();
    }
}