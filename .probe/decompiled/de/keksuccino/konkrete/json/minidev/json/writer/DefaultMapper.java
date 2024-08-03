package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.JSONArray;
import de.keksuccino.konkrete.json.minidev.json.JSONAwareEx;
import de.keksuccino.konkrete.json.minidev.json.JSONObject;

public class DefaultMapper<T> extends JsonReaderI<T> {

    protected DefaultMapper(JsonReader base) {
        super(base);
    }

    @Override
    public JsonReaderI<JSONAwareEx> startObject(String key) {
        return this.base.DEFAULT;
    }

    @Override
    public JsonReaderI<JSONAwareEx> startArray(String key) {
        return this.base.DEFAULT;
    }

    @Override
    public Object createObject() {
        return new JSONObject();
    }

    @Override
    public Object createArray() {
        return new JSONArray();
    }

    @Override
    public void setValue(Object current, String key, Object value) {
        ((JSONObject) current).put(key, value);
    }

    @Override
    public void addValue(Object current, Object value) {
        ((JSONArray) current).add(value);
    }
}