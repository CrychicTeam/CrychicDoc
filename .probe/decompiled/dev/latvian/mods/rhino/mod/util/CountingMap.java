package dev.latvian.mods.rhino.mod.util;

import it.unimi.dsi.fastutil.objects.Object2LongOpenHashMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Consumer;

public class CountingMap {

    private final Object2LongOpenHashMap<Object> map = new Object2LongOpenHashMap();

    public CountingMap() {
        this.map.defaultReturnValue(0L);
    }

    public long get(Object key) {
        return this.map.getLong(key);
    }

    public long set(Object key, long value) {
        return value <= 0L ? this.map.removeLong(key) : this.map.put(key, value);
    }

    public long add(Object key, long value) {
        return this.set(key, this.get(key) + value);
    }

    public void clear() {
        this.map.clear();
    }

    public int getSize() {
        return this.map.size();
    }

    public void forEach(Consumer<Object2LongEntry> forEach) {
        this.map.object2LongEntrySet().forEach(entry -> forEach.accept(new Object2LongEntry(entry)));
    }

    public List<Object2LongEntry> getEntries() {
        List<Object2LongEntry> list = new ArrayList(this.map.size());
        this.forEach(list::add);
        return list;
    }

    public Set<Object> getKeys() {
        return this.map.keySet();
    }

    public Collection<Long> getValues() {
        return this.map.values();
    }

    public long getTotalCount() {
        long[] count = new long[] { 0L };
        this.forEach(entry -> count[0] += entry.value);
        return count[0];
    }
}