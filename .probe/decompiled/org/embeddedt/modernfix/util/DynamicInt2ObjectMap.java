package org.embeddedt.modernfix.util;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import it.unimi.dsi.fastutil.ints.IntSet;
import it.unimi.dsi.fastutil.ints.IntSets;
import it.unimi.dsi.fastutil.objects.ObjectCollection;
import it.unimi.dsi.fastutil.objects.ObjectLists;
import it.unimi.dsi.fastutil.objects.ObjectSet;
import it.unimi.dsi.fastutil.objects.ObjectSets;
import java.util.Map.Entry;
import java.util.function.Function;

public class DynamicInt2ObjectMap<V> extends DynamicMap<Integer, V> implements Int2ObjectMap<V> {

    public DynamicInt2ObjectMap(Function<Integer, V> function) {
        super(function);
    }

    public IntSet keySet() {
        return IntSets.EMPTY_SET;
    }

    public ObjectCollection<V> values() {
        return ObjectLists.emptyList();
    }

    public ObjectSet<Entry<Integer, V>> entrySet() {
        return ObjectSets.emptySet();
    }

    public void defaultReturnValue(V rv) {
        throw new UnsupportedOperationException();
    }

    public V defaultReturnValue() {
        throw new UnsupportedOperationException();
    }

    public ObjectSet<it.unimi.dsi.fastutil.ints.Int2ObjectMap.Entry<V>> int2ObjectEntrySet() {
        throw new UnsupportedOperationException();
    }

    public V getOrDefault(int key, V defaultValue) {
        V value = this.get(key);
        return value == null ? defaultValue : value;
    }

    public V get(int key) {
        return (V) this.function.apply(key);
    }

    public boolean containsKey(int key) {
        return true;
    }
}