package com.simibubi.create.foundation.utility;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

public class UniqueLinkedList<E> extends LinkedList<E> {

    private static final long serialVersionUID = 1L;

    private final HashSet<E> contained = new HashSet();

    public boolean contains(Object o) {
        return this.contained.contains(o);
    }

    public E poll() {
        E e = (E) super.poll();
        this.contained.remove(e);
        return e;
    }

    public boolean add(E e) {
        return this.contained.add(e) ? super.add(e) : false;
    }

    public void add(int index, E element) {
        if (this.contained.add(element)) {
            super.add(index, element);
        }
    }

    public void addFirst(E e) {
        if (this.contained.add(e)) {
            super.addFirst(e);
        }
    }

    public void addLast(E e) {
        if (this.contained.add(e)) {
            super.addLast(e);
        }
    }

    public boolean addAll(Collection<? extends E> c) {
        List<? extends E> filtered = (List<? extends E>) c.stream().filter(it -> !this.contained.contains(it)).collect(Collectors.toList());
        return super.addAll(filtered);
    }

    public boolean addAll(int index, Collection<? extends E> c) {
        List<? extends E> filtered = (List<? extends E>) c.stream().filter(it -> !this.contained.contains(it)).collect(Collectors.toList());
        return super.addAll(index, filtered);
    }

    public boolean remove(Object o) {
        this.contained.remove(o);
        return super.remove(o);
    }

    public E remove(int index) {
        E e = (E) super.remove(index);
        this.contained.remove(e);
        return e;
    }

    public E removeFirst() {
        E e = (E) super.removeFirst();
        this.contained.remove(e);
        return e;
    }

    public E removeLast() {
        E e = (E) super.removeLast();
        this.contained.remove(e);
        return e;
    }

    public void clear() {
        super.clear();
        this.contained.clear();
    }
}