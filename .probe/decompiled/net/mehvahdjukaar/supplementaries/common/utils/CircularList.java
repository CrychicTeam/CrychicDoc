package net.mehvahdjukaar.supplementaries.common.utils;

import java.util.LinkedList;

public class CircularList<T> extends LinkedList<T> {

    private final int size;

    public CircularList(int size) {
        this.size = size;
    }

    public void addFirst(T t) {
        if (this.size() >= this.size) {
            this.removeLast();
        }
        super.addFirst(t);
    }

    public void addLast(T t) {
        if (this.size() >= this.size) {
            this.removeFirst();
        }
        super.addLast(t);
    }
}