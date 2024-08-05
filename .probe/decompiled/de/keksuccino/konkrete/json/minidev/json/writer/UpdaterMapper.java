package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.parser.ParseException;
import java.io.IOException;
import java.lang.reflect.Type;

public class UpdaterMapper<T> extends JsonReaderI<T> {

    final T obj;

    final JsonReaderI<?> mapper;

    public UpdaterMapper(JsonReader base, T obj) {
        super(base);
        if (obj == null) {
            throw new NullPointerException("can not update null Object");
        } else {
            this.obj = obj;
            this.mapper = base.getMapper(obj.getClass());
        }
    }

    public UpdaterMapper(JsonReader base, T obj, Type type) {
        super(base);
        if (obj == null) {
            throw new NullPointerException("can not update null Object");
        } else {
            this.obj = obj;
            this.mapper = base.getMapper(type);
        }
    }

    @Override
    public JsonReaderI<?> startObject(String key) throws ParseException, IOException {
        Object bean = this.mapper.getValue(this.obj, key);
        return (JsonReaderI<?>) (bean == null ? this.mapper.startObject(key) : new UpdaterMapper<>(this.base, bean, this.mapper.getType(key)));
    }

    @Override
    public JsonReaderI<?> startArray(String key) throws ParseException, IOException {
        return this.mapper.startArray(key);
    }

    @Override
    public void setValue(Object current, String key, Object value) throws ParseException, IOException {
        this.mapper.setValue(current, key, value);
    }

    @Override
    public void addValue(Object current, Object value) throws ParseException, IOException {
        this.mapper.addValue(current, value);
    }

    @Override
    public Object createObject() {
        return this.obj != null ? this.obj : this.mapper.createObject();
    }

    @Override
    public Object createArray() {
        return this.obj != null ? this.obj : this.mapper.createArray();
    }

    @Override
    public T convert(Object current) {
        return (T) (this.obj != null ? this.obj : this.mapper.convert(current));
    }
}