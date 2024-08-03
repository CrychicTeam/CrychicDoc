package dev.shadowsoffire.placebo.collections;

import java.util.HashMap;
import java.util.Map;

@Deprecated
public abstract class BlockedMap<K, V> extends HashMap<K, V> {

    private static final long serialVersionUID = 3265695314048724801L;

    public abstract boolean isBlocked(K var1, V var2);

    public abstract boolean isBlocked(Map<? extends K, ? extends V> var1);

    public V put(K key, V value) {
        return (V) (this.isBlocked(key, value) ? null : super.put(key, value));
    }

    public void putAll(Map<? extends K, ? extends V> m) {
        if (!this.isBlocked(m)) {
            super.putAll(m);
        }
    }

    public V putIfAbsent(K key, V value) {
        return (V) (this.isBlocked(key, value) ? null : super.putIfAbsent(key, value));
    }
}