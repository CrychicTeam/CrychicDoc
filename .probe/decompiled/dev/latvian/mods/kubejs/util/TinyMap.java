package dev.latvian.mods.kubejs.util;

import java.util.Collection;
import java.util.Map;

public record TinyMap<K, V>(TinyMap.Entry<K, V>[] entries) {

    public TinyMap(Collection<TinyMap.Entry<K, V>> collection) {
        this((TinyMap.Entry<K, V>[]) collection.toArray(new TinyMap.Entry[collection.size()]));
    }

    public TinyMap(TinyMap<K, V> map) {
        this((TinyMap.Entry<K, V>[]) map.entries.clone());
    }

    public boolean isEmpty() {
        return this.entries.length == 0;
    }

    public static <K, V> TinyMap<K, V> ofMap(Map<K, V> map) {
        TinyMap.Entry[] entries = new TinyMap.Entry[map.size()];
        int i = 0;
        for (java.util.Map.Entry<K, V> entry : map.entrySet()) {
            entries[i++] = new TinyMap.Entry<>(entry.getKey(), entry.getValue());
        }
        return new TinyMap<>(entries);
    }

    public static record Entry<K, V>(K key, V value) {
    }
}