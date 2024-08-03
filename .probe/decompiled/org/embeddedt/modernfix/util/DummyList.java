package org.embeddedt.modernfix.util;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import org.jetbrains.annotations.NotNull;

public class DummyList<T> implements List<T> {

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return true;
    }

    public boolean contains(Object o) {
        return false;
    }

    @NotNull
    public Iterator<T> iterator() {
        return Collections.emptyIterator();
    }

    @NotNull
    public Object[] toArray() {
        return new Object[0];
    }

    @NotNull
    public <T1> T1[] toArray(@NotNull T1[] t1s) {
        return (T1[]) Arrays.copyOf(t1s, 0);
    }

    public boolean add(T t) {
        return false;
    }

    public boolean remove(Object o) {
        return false;
    }

    public boolean containsAll(@NotNull Collection<?> collection) {
        return false;
    }

    public boolean addAll(@NotNull Collection<? extends T> collection) {
        return false;
    }

    public boolean addAll(int i, @NotNull Collection<? extends T> collection) {
        return false;
    }

    public boolean removeAll(@NotNull Collection<?> collection) {
        return false;
    }

    public boolean retainAll(@NotNull Collection<?> collection) {
        return false;
    }

    public void clear() {
    }

    public T get(int i) {
        return null;
    }

    public T set(int i, T t) {
        return null;
    }

    public void add(int i, T t) {
    }

    public T remove(int i) {
        return null;
    }

    public int indexOf(Object o) {
        return -1;
    }

    public int lastIndexOf(Object o) {
        return -1;
    }

    @NotNull
    public ListIterator<T> listIterator() {
        return Collections.emptyListIterator();
    }

    @NotNull
    public ListIterator<T> listIterator(int i) {
        return Collections.emptyListIterator();
    }

    @NotNull
    public List<T> subList(int i, int i1) {
        return new DummyList<>();
    }
}