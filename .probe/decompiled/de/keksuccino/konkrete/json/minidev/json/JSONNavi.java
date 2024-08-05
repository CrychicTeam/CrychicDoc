package de.keksuccino.konkrete.json.minidev.json;

import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSONNavi<T> {

    private JsonReaderI<? super T> mapper;

    private T root;

    private Stack<Object> stack = new Stack();

    private Stack<Object> path = new Stack();

    private Object current;

    private boolean failure = false;

    private String failureMessage;

    private boolean readonly = false;

    private Object missingKey = null;

    private static final JSONStyle ERROR_COMPRESS = new JSONStyle(2);

    public static JSONNavi<JSONAwareEx> newInstance() {
        return new JSONNavi<>(JSONValue.defaultReader.DEFAULT_ORDERED);
    }

    public static JSONNavi<JSONObject> newInstanceObject() {
        JSONNavi<JSONObject> o = new JSONNavi<>(JSONValue.defaultReader.getMapper(JSONObject.class));
        o.object();
        return o;
    }

    public static JSONNavi<JSONArray> newInstanceArray() {
        JSONNavi<JSONArray> o = new JSONNavi<>(JSONValue.defaultReader.getMapper(JSONArray.class));
        o.array();
        return o;
    }

    public JSONNavi(JsonReaderI<? super T> mapper) {
        this.mapper = mapper;
    }

    public JSONNavi(String json) {
        this.root = (T) JSONValue.parse(json);
        this.current = this.root;
        this.readonly = true;
    }

    public JSONNavi(String json, JsonReaderI<T> mapper) {
        this.root = JSONValue.parse(json, mapper);
        this.mapper = mapper;
        this.current = this.root;
        this.readonly = true;
    }

    public JSONNavi(String json, Class<T> mapTo) {
        this.root = JSONValue.parse(json, mapTo);
        this.mapper = JSONValue.defaultReader.getMapper(mapTo);
        this.current = this.root;
        this.readonly = true;
    }

    public JSONNavi<T> root() {
        this.current = this.root;
        this.stack.clear();
        this.path.clear();
        this.failure = false;
        this.missingKey = null;
        this.failureMessage = null;
        return this;
    }

    public boolean hasFailure() {
        return this.failure;
    }

    public Object getCurrentObject() {
        return this.current;
    }

    public Collection<String> getKeys() {
        return this.current instanceof Map ? ((Map) this.current).keySet() : null;
    }

    public int getSize() {
        if (this.current == null) {
            return 0;
        } else if (this.isArray()) {
            return ((List) this.current).size();
        } else {
            return this.isObject() ? ((Map) this.current).size() : 1;
        }
    }

    public String getString(String key) {
        String v = null;
        if (!this.hasKey(key)) {
            return v;
        } else {
            this.at(key);
            v = this.asString();
            this.up();
            return v;
        }
    }

    public int getInt(String key) {
        int v = 0;
        if (!this.hasKey(key)) {
            return v;
        } else {
            this.at(key);
            v = this.asInt();
            this.up();
            return v;
        }
    }

    public Integer getInteger(String key) {
        Integer v = null;
        if (!this.hasKey(key)) {
            return v;
        } else {
            this.at(key);
            v = this.asIntegerObj();
            this.up();
            return v;
        }
    }

    public double getDouble(String key) {
        double v = 0.0;
        if (!this.hasKey(key)) {
            return v;
        } else {
            this.at(key);
            v = this.asDouble();
            this.up();
            return v;
        }
    }

    public boolean hasKey(String key) {
        return !this.isObject() ? false : this.o(this.current).containsKey(key);
    }

    public JSONNavi<?> at(String key) {
        if (this.failure) {
            return this;
        } else {
            if (!this.isObject()) {
                this.object();
            }
            if (!(this.current instanceof Map)) {
                return this.failure("current node is not an Object", key);
            } else if (!this.o(this.current).containsKey(key)) {
                if (this.readonly) {
                    return this.failure("current Object have no key named " + key, key);
                } else {
                    this.stack.add(this.current);
                    this.path.add(key);
                    this.current = null;
                    this.missingKey = key;
                    return this;
                }
            } else {
                Object next = this.o(this.current).get(key);
                this.stack.add(this.current);
                this.path.add(key);
                this.current = next;
                return this;
            }
        }
    }

    public Object get(String key) {
        if (this.failure) {
            return this;
        } else {
            if (!this.isObject()) {
                this.object();
            }
            return !(this.current instanceof Map) ? this.failure("current node is not an Object", key) : this.o(this.current).get(key);
        }
    }

    public Object get(int index) {
        if (this.failure) {
            return this;
        } else {
            if (!this.isArray()) {
                this.array();
            }
            return !(this.current instanceof List) ? this.failure("current node is not an List", index) : this.a(this.current).get(index);
        }
    }

    public JSONNavi<T> set(String key, String value) {
        this.object();
        if (this.failure) {
            return this;
        } else {
            this.o(this.current).put(key, value);
            return this;
        }
    }

    public JSONNavi<T> set(String key, Number value) {
        this.object();
        if (this.failure) {
            return this;
        } else {
            this.o(this.current).put(key, value);
            return this;
        }
    }

    public JSONNavi<T> set(String key, long value) {
        return this.set(key, Long.valueOf(value));
    }

    public JSONNavi<T> set(String key, int value) {
        return this.set(key, Integer.valueOf(value));
    }

    public JSONNavi<T> set(String key, double value) {
        return this.set(key, Double.valueOf(value));
    }

    public JSONNavi<T> set(String key, float value) {
        return this.set(key, Float.valueOf(value));
    }

    public JSONNavi<T> add(Object... values) {
        this.array();
        if (this.failure) {
            return this;
        } else {
            List<Object> list = this.a(this.current);
            for (Object o : values) {
                list.add(o);
            }
            return this;
        }
    }

    public String asString() {
        if (this.current == null) {
            return null;
        } else {
            return this.current instanceof String ? (String) this.current : this.current.toString();
        }
    }

    public double asDouble() {
        return this.current instanceof Number ? ((Number) this.current).doubleValue() : Double.NaN;
    }

    public Double asDoubleObj() {
        if (this.current == null) {
            return null;
        } else if (this.current instanceof Number) {
            return this.current instanceof Double ? (Double) this.current : ((Number) this.current).doubleValue();
        } else {
            return Double.NaN;
        }
    }

    public float asFloat() {
        return this.current instanceof Number ? ((Number) this.current).floatValue() : Float.NaN;
    }

    public Float asFloatObj() {
        if (this.current == null) {
            return null;
        } else if (this.current instanceof Number) {
            return this.current instanceof Float ? (Float) this.current : ((Number) this.current).floatValue();
        } else {
            return Float.NaN;
        }
    }

    public int asInt() {
        return this.current instanceof Number ? ((Number) this.current).intValue() : 0;
    }

    public Integer asIntegerObj() {
        if (this.current == null) {
            return null;
        } else if (this.current instanceof Number) {
            if (this.current instanceof Integer) {
                return (Integer) this.current;
            } else {
                if (this.current instanceof Long l && l == (long) l.intValue()) {
                    return l.intValue();
                }
                return null;
            }
        } else {
            return null;
        }
    }

    public long asLong() {
        return this.current instanceof Number ? ((Number) this.current).longValue() : 0L;
    }

    public Long asLongObj() {
        if (this.current == null) {
            return null;
        } else if (this.current instanceof Number) {
            if (this.current instanceof Long) {
                return (Long) this.current;
            } else {
                return this.current instanceof Integer ? ((Number) this.current).longValue() : null;
            }
        } else {
            return null;
        }
    }

    public boolean asBoolean() {
        return this.current instanceof Boolean ? (Boolean) this.current : false;
    }

    public Boolean asBooleanObj() {
        if (this.current == null) {
            return null;
        } else {
            return this.current instanceof Boolean ? (Boolean) this.current : null;
        }
    }

    public JSONNavi<T> object() {
        if (this.failure) {
            return this;
        } else {
            if (this.current == null && this.readonly) {
                this.failure("Can not create Object child in readonly", null);
            }
            if (this.current != null) {
                if (this.isObject()) {
                    return this;
                }
                if (this.isArray()) {
                    this.failure("can not use Object feature on Array.", null);
                }
                this.failure("Can not use current position as Object", null);
            } else {
                this.current = this.mapper.createObject();
            }
            if (this.root == null) {
                this.root = (T) this.current;
            } else {
                this.store();
            }
            return this;
        }
    }

    public JSONNavi<T> array() {
        if (this.failure) {
            return this;
        } else {
            if (this.current == null && this.readonly) {
                this.failure("Can not create Array child in readonly", null);
            }
            if (this.current != null) {
                if (this.isArray()) {
                    return this;
                }
                if (this.isObject()) {
                    this.failure("can not use Object feature on Array.", null);
                }
                this.failure("Can not use current position as Object", null);
            } else {
                this.current = this.mapper.createArray();
            }
            if (this.root == null) {
                this.root = (T) this.current;
            } else {
                this.store();
            }
            return this;
        }
    }

    public JSONNavi<T> set(Number num) {
        if (this.failure) {
            return this;
        } else {
            this.current = num;
            this.store();
            return this;
        }
    }

    public JSONNavi<T> set(Boolean bool) {
        if (this.failure) {
            return this;
        } else {
            this.current = bool;
            this.store();
            return this;
        }
    }

    public JSONNavi<T> set(String text) {
        if (this.failure) {
            return this;
        } else {
            this.current = text;
            this.store();
            return this;
        }
    }

    public T getRoot() {
        return this.root;
    }

    private void store() {
        Object parent = this.stack.peek();
        if (this.isObject(parent)) {
            this.o(parent).put((String) this.missingKey, this.current);
        } else if (this.isArray(parent)) {
            int index = ((Number) this.missingKey).intValue();
            List<Object> lst = this.a(parent);
            while (lst.size() <= index) {
                lst.add(null);
            }
            lst.set(index, this.current);
        }
    }

    public boolean isArray() {
        return this.isArray(this.current);
    }

    public boolean isObject() {
        return this.isObject(this.current);
    }

    private boolean isArray(Object obj) {
        return obj == null ? false : obj instanceof List;
    }

    private boolean isObject(Object obj) {
        return obj == null ? false : obj instanceof Map;
    }

    private List<Object> a(Object obj) {
        return (List<Object>) obj;
    }

    private Map<String, Object> o(Object obj) {
        return (Map<String, Object>) obj;
    }

    public JSONNavi<?> at(int index) {
        if (this.failure) {
            return this;
        } else if (!(this.current instanceof List<Object> lst)) {
            return this.failure("current node is not an Array", index);
        } else {
            if (index < 0) {
                index += lst.size();
                if (index < 0) {
                    index = 0;
                }
            }
            if (index >= lst.size()) {
                if (this.readonly) {
                    return this.failure("Out of bound exception for index", index);
                } else {
                    this.stack.add(this.current);
                    this.path.add(index);
                    this.current = null;
                    this.missingKey = index;
                    return this;
                }
            } else {
                Object next = lst.get(index);
                this.stack.add(this.current);
                this.path.add(index);
                this.current = next;
                return this;
            }
        }
    }

    public JSONNavi<?> atNext() {
        if (this.failure) {
            return this;
        } else {
            return !(this.current instanceof List<Object> lst) ? this.failure("current node is not an Array", null) : this.at(lst.size());
        }
    }

    public JSONNavi<?> up(int level) {
        while (level-- > 0 && this.stack.size() > 0) {
            this.current = this.stack.pop();
            this.path.pop();
        }
        return this;
    }

    public JSONNavi<?> up() {
        if (this.stack.size() > 0) {
            this.current = this.stack.pop();
            this.path.pop();
        }
        return this;
    }

    public String toString() {
        return this.failure ? JSONValue.toJSONString(this.failureMessage, ERROR_COMPRESS) : JSONValue.toJSONString(this.root);
    }

    public String toString(JSONStyle compression) {
        return this.failure ? JSONValue.toJSONString(this.failureMessage, compression) : JSONValue.toJSONString(this.root, compression);
    }

    private JSONNavi<?> failure(String err, Object jPathPostfix) {
        this.failure = true;
        StringBuilder sb = new StringBuilder();
        sb.append("Error: ");
        sb.append(err);
        sb.append(" at ");
        sb.append(this.getJPath());
        if (jPathPostfix != null) {
            if (jPathPostfix instanceof Integer) {
                sb.append('[').append(jPathPostfix).append(']');
            } else {
                sb.append('/').append(jPathPostfix);
            }
        }
        this.failureMessage = sb.toString();
        return this;
    }

    public String getJPath() {
        StringBuilder sb = new StringBuilder();
        for (Object o : this.path) {
            if (o instanceof String) {
                sb.append('/').append(o.toString());
            } else {
                sb.append('[').append(o.toString()).append(']');
            }
        }
        return sb.toString();
    }
}