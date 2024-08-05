package dev.latvian.mods.rhino.util;

import java.util.HashMap;
import java.util.function.Function;

public class DynamicMap<V> extends HashMap<String, V> {

    private final Function<String, ? extends V> objectFactory;

    public DynamicMap(Function<String, ? extends V> f) {
        this.objectFactory = f;
    }

    public V get(Object key) {
        String k = key.toString();
        V v = (V) super.get(k);
        if (v == null) {
            v = (V) this.objectFactory.apply(k);
            this.put(k, v);
        }
        return v;
    }

    public boolean containsKey(Object name) {
        return true;
    }
}