package me.jellysquid.mods.lithium.common.util.collections;

import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;

public class ListeningLong2ObjectOpenHashMap<V> extends Long2ObjectOpenHashMap<V> {

    private final ListeningLong2ObjectOpenHashMap.Callback<V> addCallback;

    private final ListeningLong2ObjectOpenHashMap.Callback<V> removeCallback;

    public ListeningLong2ObjectOpenHashMap(ListeningLong2ObjectOpenHashMap.Callback<V> addCallback, ListeningLong2ObjectOpenHashMap.Callback<V> removeCallback) {
        this.addCallback = addCallback;
        this.removeCallback = removeCallback;
    }

    public V put(long k, V v) {
        V ret = (V) super.put(k, v);
        if (ret != v) {
            if (ret != null) {
                this.removeCallback.apply(k, v);
            }
            this.addCallback.apply(k, v);
        }
        return ret;
    }

    public V remove(long k) {
        V ret = (V) super.remove(k);
        if (ret != null) {
            this.removeCallback.apply(k, ret);
        }
        return ret;
    }

    public interface Callback<V> {

        void apply(long var1, V var3);
    }
}