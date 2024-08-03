package me.lucko.spark.lib.protobuf;

import java.util.AbstractList;
import java.util.Collection;
import java.util.List;
import java.util.RandomAccess;

abstract class AbstractProtobufList<E> extends AbstractList<E> implements Internal.ProtobufList<E> {

    protected static final int DEFAULT_CAPACITY = 10;

    private boolean isMutable = true;

    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (!(o instanceof List)) {
            return false;
        } else if (!(o instanceof RandomAccess)) {
            return super.equals(o);
        } else {
            List<?> other = (List<?>) o;
            int size = this.size();
            if (size != other.size()) {
                return false;
            } else {
                for (int i = 0; i < size; i++) {
                    if (!this.get(i).equals(other.get(i))) {
                        return false;
                    }
                }
                return true;
            }
        }
    }

    public int hashCode() {
        int size = this.size();
        int hashCode = 1;
        for (int i = 0; i < size; i++) {
            hashCode = 31 * hashCode + this.get(i).hashCode();
        }
        return hashCode;
    }

    public boolean add(E e) {
        this.ensureIsMutable();
        return super.add(e);
    }

    public void add(int index, E element) {
        this.ensureIsMutable();
        super.add(index, element);
    }

    public boolean addAll(Collection<? extends E> c) {
        this.ensureIsMutable();
        return super.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        this.ensureIsMutable();
        return super.addAll(index, c);
    }

    public void clear() {
        this.ensureIsMutable();
        super.clear();
    }

    @Override
    public boolean isModifiable() {
        return this.isMutable;
    }

    @Override
    public final void makeImmutable() {
        this.isMutable = false;
    }

    public E remove(int index) {
        this.ensureIsMutable();
        return (E) super.remove(index);
    }

    public boolean remove(Object o) {
        this.ensureIsMutable();
        int index = this.indexOf(o);
        if (index == -1) {
            return false;
        } else {
            this.remove(index);
            return true;
        }
    }

    public boolean removeAll(Collection<?> c) {
        this.ensureIsMutable();
        return super.removeAll(c);
    }

    public boolean retainAll(Collection<?> c) {
        this.ensureIsMutable();
        return super.retainAll(c);
    }

    public E set(int index, E element) {
        this.ensureIsMutable();
        return (E) super.set(index, element);
    }

    protected void ensureIsMutable() {
        if (!this.isMutable) {
            throw new UnsupportedOperationException();
        }
    }
}