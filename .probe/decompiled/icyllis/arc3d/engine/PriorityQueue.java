package icyllis.arc3d.engine;

import java.util.AbstractQueue;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

public class PriorityQueue<E> extends AbstractQueue<E> {

    private static final int DEFAULT_INITIAL_CAPACITY = 11;

    protected transient E[] mHeap;

    protected int mSize;

    protected Comparator<? super E> mComparator;

    protected PriorityQueue.Access<? super E> mAccess;

    public PriorityQueue() {
        this(11, null, null);
    }

    public PriorityQueue(int priority) {
        this(priority, null, null);
    }

    public PriorityQueue(PriorityQueue.Access<? super E> access) {
        this(11, null, access);
    }

    public PriorityQueue(int capacity, PriorityQueue.Access<? super E> access) {
        this(capacity, null, access);
    }

    public PriorityQueue(Comparator<? super E> comparator, PriorityQueue.Access<? super E> access) {
        this(11, comparator, access);
    }

    public PriorityQueue(int capacity, Comparator<? super E> comparator, PriorityQueue.Access<? super E> access) {
        this.mHeap = (E[]) (new Object[Math.max(1, capacity)]);
        this.mComparator = comparator;
        this.mAccess = access;
    }

    private void grow(int minCapacity) {
        int oldCapacity = this.mHeap.length;
        int newCapacity = oldCapacity + Math.max(minCapacity - oldCapacity, oldCapacity < 64 ? oldCapacity + 2 : oldCapacity >> 1);
        this.mHeap = (E[]) Arrays.copyOf(this.mHeap, newCapacity);
    }

    public boolean add(E e) {
        return this.offer(e);
    }

    public boolean offer(E e) {
        int i = this.mSize;
        if (i >= this.mHeap.length) {
            this.grow(i + 1);
        }
        this.siftUp(i, (E) Objects.requireNonNull(e));
        this.mSize = i + 1;
        return true;
    }

    public E peek() {
        return this.mHeap[0];
    }

    private int indexOf(Object o) {
        if (o != null) {
            if (this.mAccess != null) {
                return this.mAccess.getIndex((E) o);
            }
            E[] es = this.mHeap;
            int i = 0;
            for (int n = this.mSize; i < n; i++) {
                if (o.equals(es[i])) {
                    return i;
                }
            }
        }
        return -1;
    }

    public boolean remove(Object o) {
        int i = this.indexOf(o);
        if (i == -1) {
            return false;
        } else {
            this.removeAt(i);
            return true;
        }
    }

    public boolean contains(Object o) {
        return this.indexOf(o) >= 0;
    }

    public Object[] toArray() {
        return Arrays.copyOf(this.mHeap, this.mSize);
    }

    public <T> T[] toArray(T[] a) {
        int size = this.mSize;
        if (a.length < size) {
            return (T[]) Arrays.copyOf(this.mHeap, size, a.getClass());
        } else {
            System.arraycopy(this.mHeap, 0, a, 0, size);
            if (a.length > size) {
                a[size] = null;
            }
            return a;
        }
    }

    public Iterator<E> iterator() {
        return new PriorityQueue.Itr();
    }

    public int size() {
        return this.mSize;
    }

    public void clear() {
        E[] es = this.mHeap;
        if (this.mAccess != null) {
            int i = 0;
            for (int n = this.mSize; i < n; i++) {
                this.mAccess.setIndex(es[i], -1);
                es[i] = null;
            }
        } else {
            int i = 0;
            for (int n = this.mSize; i < n; i++) {
                es[i] = null;
            }
        }
        this.mSize = 0;
    }

    public E poll() {
        E[] es = this.mHeap;
        E result = es[0];
        if (result != null) {
            int n = --this.mSize;
            E x = es[n];
            es[n] = null;
            if (n > 0) {
                this.siftDown(0, x);
            }
        }
        return result;
    }

    public void removeAt(int i) {
        Objects.checkIndex(i, this.mSize);
        E[] es = this.mHeap;
        int s = --this.mSize;
        if (s == i) {
            if (this.mAccess != null) {
                this.mAccess.setIndex(es[i], -1);
            }
            es[i] = null;
        } else {
            E moved = es[s];
            if (this.mAccess != null) {
                this.mAccess.setIndex(moved, -1);
            }
            es[s] = null;
            this.siftDown(i, moved);
            if (es[i] == moved) {
                this.siftUp(i, moved);
            }
        }
    }

    public E elementAt(int i) {
        return this.mHeap[Objects.checkIndex(i, this.mSize)];
    }

    public void sort() {
        int n = this.mSize;
        if (n > 1) {
            E[] es = this.mHeap;
            Arrays.sort(es, 0, n, this.mComparator);
            PriorityQueue.Access<? super E> access = this.mAccess;
            if (access != null) {
                for (int i = 0; i < n; i++) {
                    access.setIndex(es[i], i);
                }
            }
        }
    }

    public void heap() {
        E[] es = this.mHeap;
        int n = this.mSize;
        int i = (n >>> 1) - 1;
        if (this.mComparator == null) {
            while (i >= 0) {
                siftDownComparable(i, es[i], es, n);
                i--;
            }
        } else {
            while (i >= 0) {
                siftDownUsingComparator(i, es[i], es, n, this.mComparator);
                i--;
            }
        }
        PriorityQueue.Access<? super E> access = this.mAccess;
        if (access != null) {
            for (int var5 = 0; var5 < n; var5++) {
                access.setIndex(es[var5], var5);
            }
        }
    }

    public void trim() {
        E[] es = this.mHeap;
        int n = this.mSize;
        if (n < es.length) {
            this.mHeap = (E[]) Arrays.copyOf(es, n);
        }
    }

    private void siftUp(int k, E x) {
        if (this.mComparator != null) {
            if (this.mAccess != null) {
                siftUpUsingComparator(k, x, this.mHeap, this.mComparator, this.mAccess);
            } else {
                siftUpUsingComparator(k, x, this.mHeap, this.mComparator);
            }
        } else if (this.mAccess != null) {
            siftUpComparable(k, x, this.mHeap, this.mAccess);
        } else {
            siftUpComparable(k, x, this.mHeap);
        }
    }

    private static <T> void siftUpComparable(int k, T x, T[] es) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0) {
            int parent = k - 1 >>> 1;
            T e = es[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            es[k] = e;
            k = parent;
        }
        es[k] = x;
    }

    private static <T> void siftUpComparable(int k, T x, T[] es, PriorityQueue.Access<? super T> access) {
        Comparable<? super T> key = (Comparable<? super T>) x;
        while (k > 0) {
            int parent = k - 1 >>> 1;
            T e = es[parent];
            if (key.compareTo(e) >= 0) {
                break;
            }
            es[k] = e;
            access.setIndex(e, k);
            k = parent;
        }
        es[k] = x;
        access.setIndex(x, k);
    }

    private static <T> void siftUpUsingComparator(int k, T x, T[] es, Comparator<? super T> c) {
        while (k > 0) {
            int parent = k - 1 >>> 1;
            T e = es[parent];
            if (c.compare(x, e) < 0) {
                es[k] = e;
                k = parent;
                continue;
            }
            break;
        }
        es[k] = x;
    }

    private static <T> void siftUpUsingComparator(int k, T x, T[] es, Comparator<? super T> c, PriorityQueue.Access<? super T> access) {
        while (k > 0) {
            int parent = k - 1 >>> 1;
            T e = es[parent];
            if (c.compare(x, e) < 0) {
                es[k] = e;
                access.setIndex(e, k);
                k = parent;
                continue;
            }
            break;
        }
        es[k] = x;
        access.setIndex(x, k);
    }

    private void siftDown(int k, E x) {
        if (this.mComparator != null) {
            if (this.mAccess != null) {
                siftDownUsingComparator(k, x, this.mHeap, this.mSize, this.mComparator, this.mAccess);
            } else {
                siftDownUsingComparator(k, x, this.mHeap, this.mSize, this.mComparator);
            }
        } else if (this.mAccess != null) {
            siftDownComparable(k, x, this.mHeap, this.mSize, this.mAccess);
        } else {
            siftDownComparable(k, x, this.mHeap, this.mSize);
        }
    }

    private static <T> void siftDownComparable(int k, T x, T[] es, int n) {
        assert n > 0;
        Comparable<? super T> key = (Comparable<? super T>) x;
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = es[child];
            int right = child + 1;
            if (right < n && ((Comparable) c).compareTo(es[right]) > 0) {
                child = right;
                c = es[right];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            es[k] = c;
            k = child;
        }
        es[k] = x;
    }

    private static <T> void siftDownComparable(int k, T x, T[] es, int n, PriorityQueue.Access<? super T> access) {
        assert n > 0;
        Comparable<? super T> key = (Comparable<? super T>) x;
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = es[child];
            int right = child + 1;
            if (right < n && ((Comparable) c).compareTo(es[right]) > 0) {
                child = right;
                c = es[right];
            }
            if (key.compareTo(c) <= 0) {
                break;
            }
            es[k] = c;
            access.setIndex(c, k);
            k = child;
        }
        es[k] = x;
        access.setIndex(x, k);
    }

    private static <T> void siftDownUsingComparator(int k, T x, T[] es, int n, Comparator<? super T> cmp) {
        assert n > 0;
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = es[child];
            int right = child + 1;
            if (right < n && cmp.compare(c, es[right]) > 0) {
                child = right;
                c = es[right];
            }
            if (cmp.compare(x, c) <= 0) {
                break;
            }
            es[k] = c;
            k = child;
        }
        es[k] = x;
    }

    private static <T> void siftDownUsingComparator(int k, T x, T[] es, int n, Comparator<? super T> cmp, PriorityQueue.Access<? super T> access) {
        assert n > 0;
        int half = n >>> 1;
        while (k < half) {
            int child = (k << 1) + 1;
            T c = es[child];
            int right = child + 1;
            if (right < n && cmp.compare(c, es[right]) > 0) {
                child = right;
                c = es[right];
            }
            if (cmp.compare(x, c) <= 0) {
                break;
            }
            es[k] = c;
            access.setIndex(c, k);
            k = child;
        }
        es[k] = x;
        access.setIndex(x, k);
    }

    public Comparator<? super E> comparator() {
        return this.mComparator;
    }

    public PriorityQueue.Access<? super E> access() {
        return this.mAccess;
    }

    public interface Access<E> {

        void setIndex(E var1, int var2);

        int getIndex(E var1);
    }

    private final class Itr implements Iterator<E> {

        private int mCursor;

        public boolean hasNext() {
            return this.mCursor < PriorityQueue.this.mSize;
        }

        public E next() {
            if (this.mCursor < PriorityQueue.this.mSize) {
                return PriorityQueue.this.mHeap[this.mCursor++];
            } else {
                throw new NoSuchElementException();
            }
        }
    }
}