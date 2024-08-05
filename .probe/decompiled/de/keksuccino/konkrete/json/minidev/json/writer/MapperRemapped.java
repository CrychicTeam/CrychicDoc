package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.parser.ParseException;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;

public class MapperRemapped<T> extends JsonReaderI<T> {

    private Map<String, String> rename;

    private JsonReaderI<T> parent;

    public MapperRemapped(JsonReaderI<T> parent) {
        super(parent.base);
        this.parent = parent;
        this.rename = new HashMap();
    }

    public void renameField(String source, String dest) {
        this.rename.put(source, dest);
    }

    private String rename(String key) {
        String k2 = (String) this.rename.get(key);
        return k2 != null ? k2 : key;
    }

    @Override
    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        key = this.rename(key);
        this.parent.setValue(current, key, value);
    }

    @Override
    public Object getValue(Object current, String key) {
        key = this.rename(key);
        return this.parent.getValue(current, key);
    }

    @Override
    public Type getType(String key) {
        key = this.rename(key);
        return this.parent.getType(key);
    }

    @Override
    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        key = this.rename(key);
        return this.parent.startArray(key);
    }

    @Override
    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        key = this.rename(key);
        return this.parent.startObject(key);
    }

    @Override
    public Object createObject() {
        return this.parent.createObject();
    }
}