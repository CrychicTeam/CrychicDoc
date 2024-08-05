package net.mehvahdjukaar.moonlight.api.misc;

import java.util.AbstractSet;
import java.util.Iterator;
import java.util.Map;
import java.util.WeakHashMap;

public class WeakHashSet<T> extends AbstractSet<T> {

    private final Map<T, Object> map = new WeakHashMap();

    public boolean contains(Object obj) {
        return this.map.containsKey(obj);
    }

    public boolean add(T obj) {
        return this.map.put(obj, Boolean.TRUE) == null;
    }

    public boolean remove(Object obj) {
        return this.map.remove(obj) != null;
    }

    public Iterator<T> iterator() {
        return this.map.keySet().iterator();
    }

    public int size() {
        return this.map.size();
    }
}