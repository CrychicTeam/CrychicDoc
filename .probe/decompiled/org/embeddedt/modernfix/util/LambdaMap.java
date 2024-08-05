package org.embeddedt.modernfix.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LambdaMap<K, V> implements Map<K, V> {

    private final Function<K, V> mapSupplier;

    public LambdaMap(Function<K, V> supplier) {
        this.mapSupplier = supplier;
    }

    public int size() {
        return 1;
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
        return (V) this.mapSupplier.apply(o);
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