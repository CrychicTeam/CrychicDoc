package me.jellysquid.mods.lithium.common.util.collections;

import it.unimi.dsi.fastutil.objects.ObjectListIterator;
import it.unimi.dsi.fastutil.objects.Reference2IntOpenHashMap;
import it.unimi.dsi.fastutil.objects.ReferenceArrayList;
import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.NoSuchElementException;
import org.jetbrains.annotations.NotNull;

public class HashedReferenceList<T> implements List<T> {

    private final ReferenceArrayList<T> list = new ReferenceArrayList();

    private final Reference2IntOpenHashMap<T> counter;

    public HashedReferenceList(List<T> list) {
        this.list.addAll(list);
        this.counter = new Reference2IntOpenHashMap();
        this.counter.defaultReturnValue(0);
        ObjectListIterator var2 = this.list.iterator();
        while (var2.hasNext()) {
            T obj = (T) var2.next();
            this.counter.addTo(obj, 1);
        }
    }

    public int size() {
        return this.list.size();
    }

    public boolean isEmpty() {
        return this.list.isEmpty();
    }

    public boolean contains(Object o) {
        return this.counter.containsKey(o);
    }

    public Iterator<T> iterator() {
        return this.listIterator();
    }

    public Object[] toArray() {
        return this.list.toArray();
    }

    public <T1> T1[] toArray(@NotNull T1[] a) {
        return (T1[]) this.list.toArray(a);
    }

    public boolean add(T t) {
        this.trackReferenceAdded(t);
        return this.list.add(t);
    }

    public boolean remove(Object o) {
        this.trackReferenceRemoved(o);
        return this.list.remove(o);
    }

    public boolean containsAll(Collection<?> c) {
        for (Object obj : c) {
            if (!this.counter.containsKey(obj)) {
                return false;
            }
        }
        return true;
    }

    public boolean addAll(Collection<? extends T> c) {
        for (T obj : c) {
            this.trackReferenceAdded(obj);
        }
        return this.list.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        for (T obj : c) {
            this.trackReferenceAdded(obj);
        }
        return this.list.addAll(index, c);
    }

    public boolean removeAll(@NotNull Collection<?> c) {
        if (this.size() >= 2 && c.size() > 4 && c instanceof List) {
            c = new ReferenceOpenHashSet(c);
        }
        this.counter.keySet().removeAll(c);
        return this.list.removeAll(c);
    }

    public boolean retainAll(@NotNull Collection<?> c) {
        this.counter.keySet().retainAll(c);
        return this.list.retainAll(c);
    }

    public void clear() {
        this.counter.clear();
        this.list.clear();
    }

    public T get(int index) {
        return (T) this.list.get(index);
    }

    public T set(int index, T element) {
        T prev = (T) this.list.set(index, element);
        if (prev != element) {
            if (prev != null) {
                this.trackReferenceRemoved(prev);
            }
            this.trackReferenceAdded(element);
        }
        return prev;
    }

    public void add(int index, T element) {
        this.trackReferenceAdded(element);
        this.list.add(index, element);
    }

    public T remove(int index) {
        T prev = (T) this.list.remove(index);
        if (prev != null) {
            this.trackReferenceRemoved(prev);
        }
        return prev;
    }

    public int indexOf(Object o) {
        return this.list.indexOf(o);
    }

    public int lastIndexOf(Object o) {
        return this.list.lastIndexOf(o);
    }

    public ListIterator<T> listIterator() {
        return this.listIterator(0);
    }

    public ListIterator<T> listIterator(int index) {
        return new ListIterator<T>() {

            private final ListIterator<T> inner = HashedReferenceList.this.list.listIterator(index);

            public boolean hasNext() {
                return this.inner.hasNext();
            }

            public T next() {
                return (T) this.inner.next();
            }

            public boolean hasPrevious() {
                return this.inner.hasPrevious();
            }

            public T previous() {
                return (T) this.inner.previous();
            }

            public int nextIndex() {
                return this.inner.nextIndex();
            }

            public int previousIndex() {
                return this.inner.previousIndex();
            }

            public void remove() {
                int last = this.previousIndex();
                if (last == -1) {
                    throw new NoSuchElementException();
                } else {
                    T prev = (T) HashedReferenceList.this.get(last);
                    if (prev != null) {
                        HashedReferenceList.this.trackReferenceRemoved(prev);
                    }
                    this.inner.remove();
                }
            }

            public void set(T t) {
                int last = this.previousIndex();
                if (last == -1) {
                    throw new NoSuchElementException();
                } else {
                    T prev = (T) HashedReferenceList.this.get(last);
                    if (prev != t) {
                        if (prev != null) {
                            HashedReferenceList.this.trackReferenceRemoved(prev);
                        }
                        HashedReferenceList.this.trackReferenceAdded(t);
                    }
                    this.inner.remove();
                }
            }

            public void add(T t) {
                HashedReferenceList.this.trackReferenceAdded(t);
                this.inner.add(t);
            }
        };
    }

    public List<T> subList(int fromIndex, int toIndex) {
        return this.list.subList(fromIndex, toIndex);
    }

    private void trackReferenceAdded(T t) {
        this.counter.addTo(t, 1);
    }

    private void trackReferenceRemoved(Object o) {
        if (this.counter.addTo(o, -1) <= 1) {
            this.counter.removeInt(o);
        }
    }
}