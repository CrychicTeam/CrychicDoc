package net.minecraft.util;

import it.unimi.dsi.fastutil.objects.ObjectArrays;
import java.util.AbstractSet;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import javax.annotation.Nullable;

public class SortedArraySet<T> extends AbstractSet<T> {

    private static final int DEFAULT_INITIAL_CAPACITY = 10;

    private final Comparator<T> comparator;

    T[] contents;

    int size;

    private SortedArraySet(int int0, Comparator<T> comparatorT1) {
        this.comparator = comparatorT1;
        if (int0 < 0) {
            throw new IllegalArgumentException("Initial capacity (" + int0 + ") is negative");
        } else {
            this.contents = (T[]) castRawArray(new Object[int0]);
        }
    }

    public static <T extends Comparable<T>> SortedArraySet<T> create() {
        return create(10);
    }

    public static <T extends Comparable<T>> SortedArraySet<T> create(int int0) {
        return new SortedArraySet<>(int0, Comparator.naturalOrder());
    }

    public static <T> SortedArraySet<T> create(Comparator<T> comparatorT0) {
        return create(comparatorT0, 10);
    }

    public static <T> SortedArraySet<T> create(Comparator<T> comparatorT0, int int1) {
        return new SortedArraySet<>(int1, comparatorT0);
    }

    private static <T> T[] castRawArray(Object[] object0) {
        return (T[]) object0;
    }

    private int findIndex(T t0) {
        return Arrays.binarySearch(this.contents, 0, this.size, t0, this.comparator);
    }

    private static int getInsertionPosition(int int0) {
        return -int0 - 1;
    }

    public boolean add(T t0) {
        int $$1 = this.findIndex(t0);
        if ($$1 >= 0) {
            return false;
        } else {
            int $$2 = getInsertionPosition($$1);
            this.addInternal(t0, $$2);
            return true;
        }
    }

    private void grow(int int0) {
        if (int0 > this.contents.length) {
            if (this.contents != ObjectArrays.DEFAULT_EMPTY_ARRAY) {
                int0 = (int) Math.max(Math.min((long) this.contents.length + (long) (this.contents.length >> 1), 2147483639L), (long) int0);
            } else if (int0 < 10) {
                int0 = 10;
            }
            Object[] $$1 = new Object[int0];
            System.arraycopy(this.contents, 0, $$1, 0, this.size);
            this.contents = (T[]) castRawArray($$1);
        }
    }

    private void addInternal(T t0, int int1) {
        this.grow(this.size + 1);
        if (int1 != this.size) {
            System.arraycopy(this.contents, int1, this.contents, int1 + 1, this.size - int1);
        }
        this.contents[int1] = t0;
        this.size++;
    }

    void removeInternal(int int0) {
        this.size--;
        if (int0 != this.size) {
            System.arraycopy(this.contents, int0 + 1, this.contents, int0, this.size - int0);
        }
        this.contents[this.size] = null;
    }

    private T getInternal(int int0) {
        return this.contents[int0];
    }

    public T addOrGet(T t0) {
        int $$1 = this.findIndex(t0);
        if ($$1 >= 0) {
            return this.getInternal($$1);
        } else {
            this.addInternal(t0, getInsertionPosition($$1));
            return t0;
        }
    }

    public boolean remove(Object object0) {
        int $$1 = this.findIndex((T) object0);
        if ($$1 >= 0) {
            this.removeInternal($$1);
            return true;
        } else {
            return false;
        }
    }

    @Nullable
    public T get(T t0) {
        int $$1 = this.findIndex(t0);
        return $$1 >= 0 ? this.getInternal($$1) : null;
    }

    public T first() {
        return this.getInternal(0);
    }

    public T last() {
        return this.getInternal(this.size - 1);
    }

    public boolean contains(Object object0) {
        int $$1 = this.findIndex((T) object0);
        return $$1 >= 0;
    }

    public Iterator<T> iterator() {
        return new SortedArraySet.ArrayIterator();
    }

    public int size() {
        return this.size;
    }

    public Object[] toArray() {
        return Arrays.copyOf(this.contents, this.size, Object[].class);
    }

    public <U> U[] toArray(U[] u0) {
        if (u0.length < this.size) {
            return (U[]) Arrays.copyOf(this.contents, this.size, u0.getClass());
        } else {
            System.arraycopy(this.contents, 0, u0, 0, this.size);
            if (u0.length > this.size) {
                u0[this.size] = null;
            }
            return u0;
        }
    }

    public void clear() {
        Arrays.fill(this.contents, 0, this.size, null);
        this.size = 0;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof SortedArraySet<?> $$1 && this.comparator.equals($$1.comparator)) {
                return this.size == $$1.size && Arrays.equals(this.contents, $$1.contents);
            }
            return super.equals(object0);
        }
    }

    class ArrayIterator implements Iterator<T> {

        private int index;

        private int last = -1;

        public boolean hasNext() {
            return this.index < SortedArraySet.this.size;
        }

        public T next() {
            if (this.index >= SortedArraySet.this.size) {
                throw new NoSuchElementException();
            } else {
                this.last = this.index++;
                return SortedArraySet.this.contents[this.last];
            }
        }

        public void remove() {
            if (this.last == -1) {
                throw new IllegalStateException();
            } else {
                SortedArraySet.this.removeInternal(this.last);
                this.index--;
                this.last = -1;
            }
        }
    }
}