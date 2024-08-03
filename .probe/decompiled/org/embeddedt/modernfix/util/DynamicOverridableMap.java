package org.embeddedt.modernfix.util;

import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import java.util.Map;
import java.util.function.Function;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class DynamicOverridableMap<K, V> extends DynamicMap<K, V> {

    private final Map<K, V> overrideMap = new Object2ObjectOpenHashMap();

    public DynamicOverridableMap(Function<K, V> function) {
        super(function);
    }

    @Nullable
    @Override
    public V put(K k, V v) {
        if (v == null) {
            throw new IllegalArgumentException();
        } else {
            this.overrideMap.put(k, v);
            return null;
        }
    }

    @Override
    public V get(Object o) {
        V val = (V) this.overrideMap.get(o);
        return val != null ? val : super.get(o);
    }

    @Override
    public void putAll(@NotNull Map<? extends K, ? extends V> map) {
        for (V val : map.values()) {
            if (val == null) {
                throw new IllegalArgumentException();
            }
        }
        this.overrideMap.putAll(map);
    }
}