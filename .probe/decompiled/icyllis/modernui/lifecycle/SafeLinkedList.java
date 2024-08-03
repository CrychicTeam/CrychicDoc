package icyllis.modernui.lifecycle;

import java.util.Iterator;
import java.util.WeakHashMap;
import java.util.function.Supplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;

public class SafeLinkedList<T, E extends Supplier<T>> implements Iterable<E> {

    private SafeLinkedList.Node<E> mHead;

    private SafeLinkedList.Node<E> mTail;

    private final WeakHashMap<SafeLinkedList.SafeRemove<E>, Boolean> mIterators = new WeakHashMap();

    private int mSize = 0;

    @Nullable
    protected SafeLinkedList.Node<E> find(T token) {
        for (SafeLinkedList.Node<E> n = this.mHead; n != null; n = n.mNext) {
            if (n.mElement.get().equals(token)) {
                return n;
            }
        }
        return null;
    }

    public E putIfAbsent(@Nonnull E e) {
        SafeLinkedList.Node<E> node = this.find((T) e.get());
        if (node != null) {
            return node.mElement;
        } else {
            this.put(e);
            return null;
        }
    }

    protected SafeLinkedList.Node<E> put(@Nonnull E e) {
        SafeLinkedList.Node<E> l = this.mTail;
        SafeLinkedList.Node<E> node = new SafeLinkedList.Node<>(l, e, null);
        this.mTail = node;
        if (l == null) {
            this.mHead = node;
        } else {
            l.mNext = node;
        }
        this.mSize++;
        return node;
    }

    @Nullable
    public E remove(@Nonnull T key) {
        SafeLinkedList.Node<E> n = this.find(key);
        if (n == null) {
            return null;
        } else {
            this.mSize--;
            if (!this.mIterators.isEmpty()) {
                for (SafeLinkedList.SafeRemove<E> iter : this.mIterators.keySet()) {
                    iter.remove(n);
                }
            }
            if (n.mPrev != null) {
                n.mPrev.mNext = n.mNext;
            } else {
                this.mHead = n.mNext;
            }
            if (n.mNext != null) {
                n.mNext.mPrev = n.mPrev;
            } else {
                this.mTail = n.mPrev;
            }
            n.mNext = null;
            n.mPrev = null;
            return n.mElement;
        }
    }

    public int size() {
        return this.mSize;
    }

    @Nonnull
    public Iterator<E> iterator() {
        SafeLinkedList.AscendingIterator<E> iterator = new SafeLinkedList.AscendingIterator<>(this.mHead, this.mTail);
        this.mIterators.put(iterator, Boolean.FALSE);
        return iterator;
    }

    @Nonnull
    public Iterator<E> descendingIterator() {
        SafeLinkedList.DescendingIterator<E> iterator = new SafeLinkedList.DescendingIterator<>(this.mTail, this.mHead);
        this.mIterators.put(iterator, Boolean.FALSE);
        return iterator;
    }

    @Nonnull
    public Iterator<E> iteratorWithAdditions() {
        SafeLinkedList<T, E>.IteratorWithAdditions iterator = new SafeLinkedList.IteratorWithAdditions();
        this.mIterators.put(iterator, Boolean.FALSE);
        return iterator;
    }

    public E head() {
        return this.mHead == null ? null : this.mHead.mElement;
    }

    public E tail() {
        return this.mTail == null ? null : this.mTail.mElement;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        } else if (!(obj instanceof SafeLinkedList<?, ?> map)) {
            return false;
        } else if (this.size() != map.size()) {
            return false;
        } else {
            Iterator<E> iterator1 = this.iterator();
            Iterator<?> iterator2 = map.iterator();
            while (iterator1.hasNext() && iterator2.hasNext()) {
                E next1 = (E) iterator1.next();
                Object next2 = iterator2.next();
                if (next1 == null && next2 != null || next1 != null && !next1.equals(next2)) {
                    return false;
                }
            }
            return !iterator1.hasNext() && !iterator2.hasNext();
        }
    }

    public int hashCode() {
        int h = 0;
        for (E e : this) {
            h += e.hashCode();
        }
        return h;
    }

    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[");
        Iterator<E> iterator = this.iterator();
        while (iterator.hasNext()) {
            builder.append(((Supplier) iterator.next()).toString());
            if (iterator.hasNext()) {
                builder.append(", ");
            }
        }
        builder.append("]");
        return builder.toString();
    }

    static class AscendingIterator<E> extends SafeLinkedList.SafeIterator<E> {

        AscendingIterator(SafeLinkedList.Node<E> start, SafeLinkedList.Node<E> expectedEnd) {
            super(start, expectedEnd);
        }

        @Override
        SafeLinkedList.Node<E> forward(@Nonnull SafeLinkedList.Node<E> node) {
            return node.mNext;
        }

        @Override
        SafeLinkedList.Node<E> backward(@Nonnull SafeLinkedList.Node<E> node) {
            return node.mPrev;
        }
    }

    private static class DescendingIterator<E> extends SafeLinkedList.SafeIterator<E> {

        DescendingIterator(SafeLinkedList.Node<E> start, SafeLinkedList.Node<E> expectedEnd) {
            super(start, expectedEnd);
        }

        @Override
        SafeLinkedList.Node<E> forward(@Nonnull SafeLinkedList.Node<E> node) {
            return node.mPrev;
        }

        @Override
        SafeLinkedList.Node<E> backward(@Nonnull SafeLinkedList.Node<E> node) {
            return node.mNext;
        }
    }

    private class IteratorWithAdditions implements Iterator<E>, SafeLinkedList.SafeRemove<E> {

        private SafeLinkedList.Node<E> mCurrent;

        private boolean mBeforeHead = true;

        IteratorWithAdditions() {
        }

        @Override
        public void remove(@Nonnull SafeLinkedList.Node<E> node) {
            if (node == this.mCurrent) {
                this.mCurrent = this.mCurrent.mPrev;
                this.mBeforeHead = this.mCurrent == null;
            }
        }

        public boolean hasNext() {
            return this.mBeforeHead ? SafeLinkedList.this.mHead != null : this.mCurrent != null && this.mCurrent.mNext != null;
        }

        public E next() {
            if (this.mBeforeHead) {
                this.mBeforeHead = false;
                this.mCurrent = SafeLinkedList.this.mHead;
            } else {
                this.mCurrent = this.mCurrent.mNext;
            }
            return this.mCurrent.mElement;
        }
    }

    static class Node<E> {

        E mElement;

        SafeLinkedList.Node<E> mNext;

        SafeLinkedList.Node<E> mPrev;

        Node(SafeLinkedList.Node<E> prev, E element, SafeLinkedList.Node<E> next) {
            this.mElement = element;
            this.mNext = next;
            this.mPrev = prev;
        }
    }

    private abstract static class SafeIterator<E> implements Iterator<E>, SafeLinkedList.SafeRemove<E> {

        SafeLinkedList.Node<E> mExpectedEnd;

        SafeLinkedList.Node<E> mNext;

        SafeIterator(SafeLinkedList.Node<E> start, SafeLinkedList.Node<E> expectedEnd) {
            this.mExpectedEnd = expectedEnd;
            this.mNext = start;
        }

        @Override
        public void remove(@Nonnull SafeLinkedList.Node<E> node) {
            if (this.mExpectedEnd == node && node == this.mNext) {
                this.mNext = null;
                this.mExpectedEnd = null;
            }
            if (this.mExpectedEnd == node) {
                this.mExpectedEnd = this.backward(this.mExpectedEnd);
            }
            if (this.mNext == node) {
                this.mNext = this.nextNode();
            }
        }

        @Nullable
        private SafeLinkedList.Node<E> nextNode() {
            return this.mNext != this.mExpectedEnd && this.mExpectedEnd != null ? this.forward(this.mNext) : null;
        }

        public boolean hasNext() {
            return this.mNext != null;
        }

        public E next() {
            E result = this.mNext.mElement;
            this.mNext = this.nextNode();
            return result;
        }

        abstract SafeLinkedList.Node<E> forward(@Nonnull SafeLinkedList.Node<E> var1);

        abstract SafeLinkedList.Node<E> backward(@Nonnull SafeLinkedList.Node<E> var1);
    }

    interface SafeRemove<V> {

        void remove(@Nonnull SafeLinkedList.Node<V> var1);
    }
}