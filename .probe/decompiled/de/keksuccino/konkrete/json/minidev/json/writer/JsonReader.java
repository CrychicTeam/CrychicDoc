package de.keksuccino.konkrete.json.minidev.json.writer;

import de.keksuccino.konkrete.json.minidev.json.JSONArray;
import de.keksuccino.konkrete.json.minidev.json.JSONAware;
import de.keksuccino.konkrete.json.minidev.json.JSONAwareEx;
import de.keksuccino.konkrete.json.minidev.json.JSONObject;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class JsonReader {

    private final ConcurrentHashMap<Type, JsonReaderI<?>> cache = new ConcurrentHashMap(100);

    public JsonReaderI<JSONAwareEx> DEFAULT;

    public JsonReaderI<JSONAwareEx> DEFAULT_ORDERED;

    public JsonReader() {
        this.cache.put(Date.class, BeansMapper.MAPPER_DATE);
        this.cache.put(int[].class, ArraysMapper.MAPPER_PRIM_INT);
        this.cache.put(Integer[].class, ArraysMapper.MAPPER_INT);
        this.cache.put(short[].class, ArraysMapper.MAPPER_PRIM_INT);
        this.cache.put(Short[].class, ArraysMapper.MAPPER_INT);
        this.cache.put(long[].class, ArraysMapper.MAPPER_PRIM_LONG);
        this.cache.put(Long[].class, ArraysMapper.MAPPER_LONG);
        this.cache.put(byte[].class, ArraysMapper.MAPPER_PRIM_BYTE);
        this.cache.put(Byte[].class, ArraysMapper.MAPPER_BYTE);
        this.cache.put(char[].class, ArraysMapper.MAPPER_PRIM_CHAR);
        this.cache.put(Character[].class, ArraysMapper.MAPPER_CHAR);
        this.cache.put(float[].class, ArraysMapper.MAPPER_PRIM_FLOAT);
        this.cache.put(Float[].class, ArraysMapper.MAPPER_FLOAT);
        this.cache.put(double[].class, ArraysMapper.MAPPER_PRIM_DOUBLE);
        this.cache.put(Double[].class, ArraysMapper.MAPPER_DOUBLE);
        this.cache.put(boolean[].class, ArraysMapper.MAPPER_PRIM_BOOL);
        this.cache.put(Boolean[].class, ArraysMapper.MAPPER_BOOL);
        this.DEFAULT = new DefaultMapper<>(this);
        this.DEFAULT_ORDERED = new DefaultMapperOrdered(this);
        this.cache.put(JSONAwareEx.class, this.DEFAULT);
        this.cache.put(JSONAware.class, this.DEFAULT);
        this.cache.put(JSONArray.class, this.DEFAULT);
        this.cache.put(JSONObject.class, this.DEFAULT);
    }

    public <T> void remapField(Class<T> type, String fromJson, String toJava) {
        JsonReaderI<T> map = this.getMapper(type);
        if (!(map instanceof MapperRemapped)) {
            map = new MapperRemapped<>(map);
            this.registerReader(type, map);
        }
        ((MapperRemapped) map).renameField(fromJson, toJava);
    }

    public <T> void registerReader(Class<T> type, JsonReaderI<T> mapper) {
        this.cache.put(type, mapper);
    }

    public <T> JsonReaderI<T> getMapper(Type type) {
        return type instanceof ParameterizedType ? this.getMapper((ParameterizedType) type) : this.getMapper((Class<T>) type);
    }

    public <T> JsonReaderI<T> getMapper(Class<T> type) {
        JsonReaderI<T> map = (JsonReaderI<T>) this.cache.get(type);
        if (map != null) {
            return map;
        } else {
            if (type instanceof Class) {
                if (Map.class.isAssignableFrom(type)) {
                    map = new DefaultMapperCollection<>(this, type);
                } else if (List.class.isAssignableFrom(type)) {
                    map = new DefaultMapperCollection<>(this, type);
                }
                if (map != null) {
                    this.cache.put(type, map);
                    return map;
                }
            }
            if (type.isArray()) {
                map = new ArraysMapper.GenericMapper<>(this, type);
            } else if (List.class.isAssignableFrom(type)) {
                map = new CollectionMapper.ListClass<>(this, type);
            } else if (Map.class.isAssignableFrom(type)) {
                map = new CollectionMapper.MapClass<>(this, type);
            } else {
                map = new BeansMapper.Bean<>(this, type);
            }
            this.cache.putIfAbsent(type, map);
            return map;
        }
    }

    public <T> JsonReaderI<T> getMapper(ParameterizedType type) {
        JsonReaderI<T> map = (JsonReaderI<T>) this.cache.get(type);
        if (map != null) {
            return map;
        } else {
            Class<T> clz = (Class<T>) type.getRawType();
            if (List.class.isAssignableFrom(clz)) {
                map = new CollectionMapper.ListType<>(this, type);
            } else if (Map.class.isAssignableFrom(clz)) {
                map = new CollectionMapper.MapType<>(this, type);
            }
            this.cache.putIfAbsent(type, map);
            return map;
        }
    }
}