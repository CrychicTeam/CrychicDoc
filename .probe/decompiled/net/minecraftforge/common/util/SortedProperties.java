package net.minecraftforge.common.util;

import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;
import java.util.Map.Entry;

public class SortedProperties extends Properties {

    private static final long serialVersionUID = -8913480931455982442L;

    public Set<Entry<Object, Object>> entrySet() {
        Set<Entry<Object, Object>> ret = new TreeSet((left, right) -> left.getKey().toString().compareTo(right.getKey().toString()));
        ret.addAll(super.entrySet());
        return ret;
    }

    public Set<Object> keySet() {
        return new TreeSet(super.keySet());
    }

    public synchronized Enumeration<Object> keys() {
        return Collections.enumeration(new TreeSet(super.keySet()));
    }

    public static void store(Properties props, Writer stream, String comment) throws IOException {
        SortedProperties sorted = new SortedProperties();
        sorted.putAll(props);
        sorted.store(stream, comment);
    }
}