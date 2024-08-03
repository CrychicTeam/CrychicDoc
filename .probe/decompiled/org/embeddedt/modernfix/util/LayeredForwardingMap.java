package org.embeddedt.modernfix.util;

import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class LayeredForwardingMap<K, V> implements Map<K, V> {

    private final Map<K, V>[] layers;

    public LayeredForwardingMap(Map<K, V>[] layers) {
        if (layers.length < 1) {
            throw new IllegalArgumentException();
        } else {
            for (Map<K, V> layer : layers) {
                if (layer == null) {
                    throw new IllegalArgumentException();
                }
            }
            this.layers = layers;
        }
    }

    public int size() {
        return 1;
    }

    public boolean isEmpty() {
        for (Map<K, V> map : this.layers) {
            if (!map.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    public boolean containsKey(Object key) {
        for (Map<K, V> map : this.layers) {
            if (map.containsKey(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean containsValue(Object value) {
        for (Map<K, V> map : this.layers) {
            if (map.containsValue(value)) {
                return true;
            }
        }
        return false;
    }

    public V get(Object key) {
        for (Map<K, V> map : this.layers) {
            V value = (V) map.get(key);
            if (value != null) {
                return value;
            }
        }
        return null;
    }

    @Nullable
    public V put(K key, V value) {
        if (value == null) {
            throw new IllegalArgumentException();
        } else {
            V originalValue = null;
            for (Map<K, V> map : this.layers) {
                V oldVal = (V) map.remove(key);
                if (originalValue == null) {
                    originalValue = oldVal;
                }
                map.put(key, value);
            }
            return originalValue;
        }
    }

    public V remove(Object key) {
        for (Map<K, V> map : this.layers) {
            map.remove(key);
        }
        return null;
    }

    public void putAll(@NotNull Map<? extends K, ? extends V> m) {
        for (V value : m.values()) {
            if (value == null) {
                throw new IllegalArgumentException();
            }
        }
        for (Map<K, V> map : this.layers) {
            map.putAll(m);
        }
    }

    public void clear() {
        throw new UnsupportedOperationException();
    }

    @NotNull
    public Set<K> keySet() {
        Set<K> keys = new ObjectOpenHashSet();
        for (Map<K, V> map : this.layers) {
            keys.addAll(map.keySet());
        }
        return Collections.unmodifiableSet(keys);
    }

    @NotNull
    public Collection<V> values() {
        Set<K> keys = this.keySet();
        List<V> vals = new ArrayList();
        for (K key : keys) {
            vals.add(this.get(key));
        }
        return vals;
    }

    @NotNull
    public Set<Entry<K, V>> entrySet() {
        throw new UnsupportedOperationException();
    }
}