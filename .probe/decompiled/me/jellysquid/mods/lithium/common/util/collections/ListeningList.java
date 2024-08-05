package me.jellysquid.mods.lithium.common.util.collections;

import java.util.Collection;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.function.Consumer;
import java.util.function.Predicate;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import org.jetbrains.annotations.NotNull;

public class ListeningList<T> implements List<T> {

    private final List<T> delegate;

    private final Runnable changeCallback;

    public ListeningList(List<T> delegate, Runnable changeCallback) {
        this.delegate = delegate;
        this.changeCallback = changeCallback;
    }

    protected void onChange() {
        this.changeCallback.run();
    }

    public int size() {
        return this.delegate.size();
    }

    public boolean isEmpty() {
        return this.delegate.isEmpty();
    }

    public boolean contains(Object o) {
        return this.delegate.contains(o);
    }

    @NotNull
    public Iterator<T> iterator() {
        return this.listIterator();
    }

    @NotNull
    public Object[] toArray() {
        return this.delegate.toArray();
    }

    public void forEach(Consumer<? super T> consumer) {
        this.delegate.forEach(consumer);
    }

    @NotNull
    public <T1> T1[] toArray(@NotNull T1[] t1s) {
        return (T1[]) this.delegate.toArray(t1s);
    }

    public boolean add(T t) {
        boolean add = this.delegate.add(t);
        this.onChange();
        return add;
    }

    public boolean remove(Object o) {
        boolean remove = this.delegate.remove(o);
        this.onChange();
        return remove;
    }

    public boolean containsAll(@NotNull Collection<?> collection) {
        return this.delegate.containsAll(collection);
    }

    public boolean addAll(@NotNull Collection<? extends T> collection) {
        boolean addAll = this.delegate.addAll(collection);
        this.onChange();
        return addAll;
    }

    public boolean addAll(int i, @NotNull Collection<? extends T> collection) {
        boolean addAll = this.delegate.addAll(i, collection);
        this.onChange();
        return addAll;
    }

    public boolean removeAll(@NotNull Collection<?> collection) {
        boolean b = this.delegate.removeAll(collection);
        this.onChange();
        return b;
    }

    public boolean removeIf(Predicate<? super T> predicate) {
        boolean b = this.delegate.removeIf(predicate);
        this.onChange();
        return b;
    }

    public boolean retainAll(@NotNull Collection<?> collection) {
        boolean b = this.delegate.retainAll(collection);
        this.onChange();
        return b;
    }

    public void replaceAll(UnaryOperator<T> unaryOperator) {
        this.delegate.replaceAll(unaryOperator);
        this.onChange();
    }

    public void sort(Comparator<? super T> comparator) {
        this.delegate.sort(comparator);
        this.onChange();
    }

    public void clear() {
        this.delegate.clear();
        this.onChange();
    }

    public T get(int i) {
        return (T) this.delegate.get(i);
    }

    public T set(int i, T t) {
        T set = (T) this.delegate.set(i, t);
        this.onChange();
        return set;
    }

    public void add(int i, T t) {
        this.delegate.add(i, t);
        this.onChange();
    }

    public T remove(int i) {
        T remove = (T) this.delegate.remove(i);
        this.onChange();
        return remove;
    }

    public int indexOf(Object o) {
        return this.delegate.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.delegate.lastIndexOf(o);
    }

    @NotNull
    public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }

    @NotNull
    public ListIterator<T> listIterator(int i) {
        return new ListIterator<T>() {

            final ListIterator<T> itDelegate = ListeningList.this.delegate.listIterator(i);

            public boolean hasNext() {
                return this.itDelegate.hasNext();
            }

            public T next() {
                return (T) this.itDelegate.next();
            }

            public boolean hasPrevious() {
                return this.itDelegate.hasPrevious();
            }

            public T previous() {
                return (T) this.itDelegate.previous();
            }

            public int nextIndex() {
                return this.itDelegate.nextIndex();
            }

            public int previousIndex() {
                return this.itDelegate.previousIndex();
            }

            public void remove() {
                this.itDelegate.remove();
                ListeningList.this.onChange();
            }

            public void set(T t) {
                this.itDelegate.set(t);
                ListeningList.this.onChange();
            }

            public void add(T t) {
                this.itDelegate.add(t);
                ListeningList.this.onChange();
            }
        };
    }

    @NotNull
    public List<T> subList(int i, int i1) {
        throw new UnsupportedOperationException();
    }

    public Spliterator<T> spliterator() {
        return this.delegate.spliterator();
    }

    public Stream<T> stream() {
        return this.delegate.stream();
    }

    public Stream<T> parallelStream() {
        return this.delegate.parallelStream();
    }
}