package dev.latvian.mods.rhino.util;

import java.util.AbstractList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

public class JavaSetWrapper<T> extends AbstractList<T> {

    public final Set<T> set;

    public JavaSetWrapper(Set<T> set) {
        this.set = set;
    }

    public T get(int index) {
        if (index >= 0 && index < this.size()) {
            if (index == 0) {
                return (T) this.set.iterator().next();
            } else {
                for (T element : this.set) {
                    if (index == 0) {
                        return element;
                    }
                    index--;
                }
                throw new IndexOutOfBoundsException(index);
            }
        } else {
            throw new IndexOutOfBoundsException(index);
        }
    }

    public int size() {
        return this.set.size();
    }

    public boolean add(T t) {
        return this.set.add(t);
    }

    public void add(int index, T element) {
        this.set.add(element);
    }

    public boolean addAll(Collection<? extends T> c) {
        return this.set.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return this.set.addAll(c);
    }

    public T set(int index, T element) {
        if (this.set.remove(element)) {
            this.set.add(element);
            return null;
        } else {
            this.set.add(element);
            return element;
        }
    }

    public T remove(int index) {
        if (index >= 0 && index < this.size()) {
            for (Iterator<T> iterator = this.set.iterator(); iterator.hasNext(); index--) {
                T element = (T) iterator.next();
                if (index == 0) {
                    iterator.remove();
                    return element;
                }
            }
            throw new IndexOutOfBoundsException(index);
        } else {
            throw new IndexOutOfBoundsException(index);
        }
    }

    public boolean remove(Object o) {
        return this.set.remove(o);
    }

    public void clear() {
        this.set.clear();
    }
}