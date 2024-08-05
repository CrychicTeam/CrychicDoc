package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.JSONArray;
import de.keksuccino.konkrete.json.minidev.json.JSONAwareEx;
import java.util.LinkedHashMap;
import java.util.Map;

public class DefaultMapperOrdered extends JsonReaderI<JSONAwareEx> {

    protected DefaultMapperOrdered(JsonReader base) {
        super(base);
    }

    @Override
    public JsonReaderI<JSONAwareEx> startObject(String key) {
        return this.base.DEFAULT_ORDERED;
    }

    @Override
    public JsonReaderI<JSONAwareEx> startArray(String key) {
        return this.base.DEFAULT_ORDERED;
    }

    @Override
    public void setValue(Object current, String key, Object value) {
        ((Map) current).put(key, value);
    }

    @Override
    public Object createObject() {
        return new LinkedHashMap();
    }

    @Override
    public void addValue(Object current, Object value) {
        ((JSONArray) current).add(value);
    }

    @Override
    public Object createArray() {
        return new JSONArray();
    }
}