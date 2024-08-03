package com.rekindled.embers.util;

import java.util.ArrayList;
import java.util.Set;
import java.util.TreeMap;

public class PipePriorityMap<K, V> {

    TreeMap<K, ArrayList<V>> map = new TreeMap();

    public void put(K key, V value) {
        ArrayList<V> list = (ArrayList<V>) this.map.get(key);
        if (list == null) {
            list = new ArrayList();
            this.map.put(key, list);
        }
        list.add(value);
    }

    public ArrayList<V> get(K key) {
        return (ArrayList<V>) this.map.get(key);
    }

    public Set<K> keySet() {
        return this.map.keySet();
    }
}