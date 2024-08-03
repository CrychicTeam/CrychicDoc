package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;

abstract class ArrayValueWriter implements ValueWriter {

    protected static boolean isArrayish(Object value) {
        return value instanceof Collection || value.getClass().isArray();
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    static boolean isArrayOfPrimitive(Object array) {
        Object first = peek(array);
        if (first == null) {
            return true;
        } else {
            ValueWriter valueWriter = ValueWriters.WRITERS.findWriterFor(first);
            return valueWriter.isPrimitiveType() || isArrayish(first);
        }
    }

    protected Collection<?> normalize(Object value) {
        Collection<Object> collection;
        if (value.getClass().isArray()) {
            collection = new ArrayList(Array.getLength(value));
            for (int i = 0; i < Array.getLength(value); i++) {
                Object elem = Array.get(value, i);
                collection.add(elem);
            }
        } else {
            collection = (Collection<Object>) value;
        }
        return collection;
    }

    private static Object peek(Object value) {
        if (value.getClass().isArray()) {
            return Array.getLength(value) > 0 ? Array.get(value, 0) : null;
        } else {
            Collection<?> collection = (Collection<?>) value;
            return collection.size() > 0 ? collection.iterator().next() : null;
        }
    }
}