package dev.shadowsoffire.placebo.collections;

import java.util.ArrayList;
import java.util.Collection;

@Deprecated
public abstract class BlockedList<T> extends ArrayList<T> {

    private static final long serialVersionUID = -7008599240020028661L;

    public abstract boolean isBlocked(T var1);

    public abstract boolean isBlocked(Collection<? extends T> var1);

    public void add(int index, T t) {
        if (!this.isBlocked(t)) {
            super.add(index, t);
        }
    }

    public boolean add(T t) {
        return this.isBlocked(t) ? false : super.add(t);
    }

    public boolean addAll(Collection<? extends T> c) {
        return this.isBlocked(c) ? false : super.addAll(c);
    }

    public boolean addAll(int index, Collection<? extends T> c) {
        return this.isBlocked(c) ? false : super.addAll(index, c);
    }
}