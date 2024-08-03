package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.parser.ParseException;
import java.io.IOException;
import java.lang.reflect.Type;

public abstract class JsonReaderI<T> {

    public final JsonReader base;

    private static String ERR_MSG = "Invalid or non Implemented status";

    public JsonReaderI(JsonReader base) {
        this.base = base;
    }

    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " startObject(String key) in " + this.getClass() + " key=" + key);
    }

    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " startArray in " + this.getClass() + " key=" + key);
    }

    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " setValue in " + this.getClass() + " key=" + key);
    }

    public Object getValue(Object current, String key) {
        throw new RuntimeException(ERR_MSG + " getValue(Object current, String key) in " + this.getClass() + " key=" + key);
    }

    public Type getType(String key) {
        throw new RuntimeException(ERR_MSG + " getType(String key) in " + this.getClass() + " key=" + key);
    }

    public void addValue(Object current, Object value) throws ParseException, IOException {
        throw new RuntimeException(ERR_MSG + " addValue(Object current, Object value) in " + this.getClass());
    }

    public Object createObject() {
        throw new RuntimeException(ERR_MSG + " createObject() in " + this.getClass());
    }

    public Object createArray() {
        throw new RuntimeException(ERR_MSG + " createArray() in " + this.getClass());
    }

    public T convert(Object current) {
        return (T) current;
    }
}