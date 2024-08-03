package org.embeddedt.modernfix.util;

import com.google.common.base.Function;
import com.google.common.collect.Interner;
import com.google.common.collect.Interners;
import com.google.common.collect.Maps;
import java.util.HashMap;
import java.util.Map;

public class CanonizingStringMap<T> extends HashMap<String, T> {

    private static final Interner<String> KEY_INTERNER = Interners.newWeakInterner();

    private static String intern(String key) {
        return key != null ? (String) KEY_INTERNER.intern(key) : null;
    }

    public T put(String key, T value) {
        return (T) super.put(intern(key), value);
    }

    public void putAll(Map<? extends String, ? extends T> m) {
        if (!m.isEmpty()) {
            HashMap<String, T> tmp = new HashMap();
            m.forEach((k, v) -> tmp.put(intern(k), v));
            super.putAll(tmp);
        }
    }

    private void putAllWithoutInterning(Map<? extends String, ? extends T> m) {
        super.putAll(m);
    }

    public static <T> CanonizingStringMap<T> deepCopy(CanonizingStringMap<T> incomingMap, Function<T, T> deepCopier) {
        CanonizingStringMap<T> newMap = new CanonizingStringMap<>();
        newMap.putAllWithoutInterning(Maps.transformValues(incomingMap, deepCopier));
        return newMap;
    }
}