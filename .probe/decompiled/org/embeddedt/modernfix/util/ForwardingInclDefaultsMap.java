package org.embeddedt.modernfix.util;

import com.google.common.collect.ForwardingMap;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class ForwardingInclDefaultsMap<K, V> extends ForwardingMap<K, V> {

    public V getOrDefault(Object key, V defaultValue) {
        return (V) this.delegate().getOrDefault(key, defaultValue);
    }

    public void forEach(BiConsumer<? super K, ? super V> action) {
        this.delegate().forEach(action);
    }

    public void replaceAll(BiFunction<? super K, ? super V, ? extends V> function) {
        this.delegate().replaceAll(function);
    }

    @Nullable
    public V putIfAbsent(K key, V value) {
        return (V) this.delegate().putIfAbsent(key, value);
    }

    public boolean remove(Object key, Object value) {
        return this.delegate().remove(key, value);
    }

    public boolean replace(K key, V oldValue, V newValue) {
        return this.delegate().replace(key, oldValue, newValue);
    }

    @Nullable
    public V replace(K key, V value) {
        return (V) this.delegate().replace(key, value);
    }

    public V computeIfAbsent(K key, @NotNull Function<? super K, ? extends V> mappingFunction) {
        return (V) this.delegate().computeIfAbsent(key, mappingFunction);
    }

    public V computeIfPresent(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return (V) this.delegate().computeIfPresent(key, remappingFunction);
    }

    public V compute(K key, @NotNull BiFunction<? super K, ? super V, ? extends V> remappingFunction) {
        return (V) this.delegate().compute(key, remappingFunction);
    }

    public V merge(K key, @NotNull V value, @NotNull BiFunction<? super V, ? super V, ? extends V> remappingFunction) {
        return (V) this.delegate().merge(key, value, remappingFunction);
    }
}