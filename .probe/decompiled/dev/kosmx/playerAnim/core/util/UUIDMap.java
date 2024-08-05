package dev.kosmx.playerAnim.core.util;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.UUID;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class UUIDMap<T extends Supplier<UUID>> extends HashMap<UUID, T> implements Iterable<T> {

    public T put(T v) {
        return (T) this.put((UUID) v.get(), v);
    }

    public void addAll(Collection<T> m) {
        for (T t : m) {
            this.put(t);
        }
    }

    public Iterator<T> iterator() {
        return this.values().iterator();
    }

    public void add(T value) {
        this.put(value);
    }

    public boolean contains(T value) {
        return this.containsKey(value.get());
    }

    public void removeIf(Predicate<T> predicate) {
        this.values().removeIf(predicate);
    }
}