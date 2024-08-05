package de.keksuccino.konkrete.json.jsonpath.spi.json;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public abstract class AbstractJsonProvider implements JsonProvider {

    @Override
    public boolean isArray(Object obj) {
        return obj instanceof List;
    }

    @Override
    public Object getArrayIndex(Object obj, int idx) {
        return ((List) obj).get(idx);
    }

    @Deprecated
    @Override
    public final Object getArrayIndex(Object obj, int idx, boolean unwrap) {
        return this.getArrayIndex(obj, idx);
    }

    @Override
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (!this.isArray(array)) {
            throw new UnsupportedOperationException();
        } else {
            List l = (List) array;
            if (index == l.size()) {
                l.add(newValue);
            } else {
                l.set(index, newValue);
            }
        }
    }

    @Override
    public Object getMapValue(Object obj, String key) {
        Map m = (Map) obj;
        return !m.containsKey(key) ? JsonProvider.UNDEFINED : m.get(key);
    }

    @Override
    public void setProperty(Object obj, Object key, Object value) {
        if (this.isMap(obj)) {
            ((Map) obj).put(key.toString(), value);
        } else {
            throw new JsonPathException("setProperty operation cannot be used with " + obj != null ? obj.getClass().getName() : "null");
        }
    }

    @Override
    public void removeProperty(Object obj, Object key) {
        if (this.isMap(obj)) {
            ((Map) obj).remove(key.toString());
        } else {
            List list = (List) obj;
            int index = key instanceof Integer ? (Integer) key : Integer.parseInt(key.toString());
            list.remove(index);
        }
    }

    @Override
    public boolean isMap(Object obj) {
        return obj instanceof Map;
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        if (this.isArray(obj)) {
            throw new UnsupportedOperationException();
        } else {
            return ((Map) obj).keySet();
        }
    }

    @Override
    public int length(Object obj) {
        if (this.isArray(obj)) {
            return ((List) obj).size();
        } else if (this.isMap(obj)) {
            return this.getPropertyKeys(obj).size();
        } else if (obj instanceof String) {
            return ((String) obj).length();
        } else {
            throw new JsonPathException("length operation cannot be applied to " + (obj != null ? obj.getClass().getName() : "null"));
        }
    }

    @Override
    public Iterable<?> toIterable(Object obj) {
        if (this.isArray(obj)) {
            return (Iterable<?>) obj;
        } else {
            throw new JsonPathException("Cannot iterate over " + obj != null ? obj.getClass().getName() : "null");
        }
    }

    @Override
    public Object unwrap(Object obj) {
        return obj;
    }
}