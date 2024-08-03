package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.asm.Accessor;
import de.keksuccino.konkrete.json.minidev.asm.BeansAccess;
import de.keksuccino.konkrete.json.minidev.asm.ConvertDate;
import de.keksuccino.konkrete.json.minidev.json.JSONUtil;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;

public abstract class BeansMapper<T> extends JsonReaderI<T> {

    public static JsonReaderI<Date> MAPPER_DATE = new ArraysMapper<Date>(null) {

        public Date convert(Object current) {
            return ConvertDate.convertToDate(current);
        }
    };

    public BeansMapper(JsonReader base) {
        super(base);
    }

    @Override
    public abstract Object getValue(Object var1, String var2);

    public static class Bean<T> extends JsonReaderI<T> {

        final Class<T> clz;

        final BeansAccess<T> ba;

        final HashMap<String, Accessor> index;

        public Bean(JsonReader base, Class<T> clz) {
            super(base);
            this.clz = clz;
            this.ba = BeansAccess.get(clz, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.index = this.ba.getMap();
        }

        @Override
        public void setValue(Object current, String key, Object value) {
            this.ba.set((T) current, key, value);
        }

        @Override
        public Object getValue(Object current, String key) {
            return this.ba.get((T) current, key);
        }

        @Override
        public Type getType(String key) {
            Accessor nfo = (Accessor) this.index.get(key);
            return nfo.getGenericType();
        }

        @Override
        public JsonReaderI<?> startArray(String key) {
            Accessor nfo = (Accessor) this.index.get(key);
            if (nfo == null) {
                throw new RuntimeException("Can not find Array '" + key + "' field in " + this.clz);
            } else {
                return this.base.getMapper(nfo.getGenericType());
            }
        }

        @Override
        public JsonReaderI<?> startObject(String key) {
            Accessor f = (Accessor) this.index.get(key);
            if (f == null) {
                throw new RuntimeException("Can not find Object '" + key + "' field in " + this.clz);
            } else {
                return this.base.getMapper(f.getGenericType());
            }
        }

        @Override
        public Object createObject() {
            return this.ba.newInstance();
        }
    }

    public static class BeanNoConv<T> extends JsonReaderI<T> {

        final Class<T> clz;

        final BeansAccess<T> ba;

        final HashMap<String, Accessor> index;

        public BeanNoConv(JsonReader base, Class<T> clz) {
            super(base);
            this.clz = clz;
            this.ba = BeansAccess.get(clz, JSONUtil.JSON_SMART_FIELD_FILTER);
            this.index = this.ba.getMap();
        }

        @Override
        public void setValue(Object current, String key, Object value) {
            this.ba.set((T) current, key, value);
        }

        @Override
        public Object getValue(Object current, String key) {
            return this.ba.get((T) current, key);
        }

        @Override
        public Type getType(String key) {
            Accessor nfo = (Accessor) this.index.get(key);
            return nfo.getGenericType();
        }

        @Override
        public JsonReaderI<?> startArray(String key) {
            Accessor nfo = (Accessor) this.index.get(key);
            if (nfo == null) {
                throw new RuntimeException("Can not set " + key + " field in " + this.clz);
            } else {
                return this.base.getMapper(nfo.getGenericType());
            }
        }

        @Override
        public JsonReaderI<?> startObject(String key) {
            Accessor f = (Accessor) this.index.get(key);
            if (f == null) {
                throw new RuntimeException("Can not set " + key + " field in " + this.clz);
            } else {
                return this.base.getMapper(f.getGenericType());
            }
        }

        @Override
        public Object createObject() {
            return this.ba.newInstance();
        }
    }
}