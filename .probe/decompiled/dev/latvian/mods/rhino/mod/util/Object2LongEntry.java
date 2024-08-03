package dev.latvian.mods.rhino.mod.util;

import it.unimi.dsi.fastutil.objects.Object2LongMap.Entry;

public class Object2LongEntry implements Comparable<Object2LongEntry> {

    public final Object key;

    public final long value;

    public Object2LongEntry(Object k, long v) {
        this.key = k;
        this.value = v;
    }

    public Object2LongEntry(Entry<Object> entry) {
        this.key = entry.getKey();
        this.value = entry.getLongValue();
    }

    public int compareTo(Object2LongEntry o) {
        int c = Long.compare(o.value, this.value);
        if (c == 0 && this.key != null && o.key != null) {
            c = this.key.toString().compareToIgnoreCase(o.key.toString());
        }
        return c;
    }
}