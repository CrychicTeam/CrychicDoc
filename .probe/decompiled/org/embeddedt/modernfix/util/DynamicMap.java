package org.embeddedt.modernfix.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicMap<K, V> implements Map<K, V> {

    protected final Function<K, V> function;

    public DynamicMap(Function<K, V> function) {
        this.function = function;
    }

    public int size() {
        return 0;
    }

    public boolean isEmpty() {
        return false;
    }

    public boolean containsKey(Object o) {
        return true;
    }

    public boolean containsValue(Object o) {
        return true;
    }

    public V get(Object o) {
        return (V) this.function.apply(o);
    }

    @Nullable
    public V put(K k, V v) {
        throw new UnsupportedOperationException();
    }

    public V remove(Object o) {
        throw new UnsupportedOperationException();
    }

    public void putAll(@NotNull Map<? extends K, ? extends V> map) {
        throw new UnsupportedOperationException();
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<K> keySet() {
        return Collections.emptySet();
    }

    @NotNull
    public Collection<V> values() {
        return Collections.emptyList();
    }

    @NotNull
    public Set<Entry<K, V>> entrySet() {
        return Collections.emptySet();
    }
}