package de.keksuccino.konkrete.json.jsonpath.spi.json;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import de.keksuccino.konkrete.json.jsonpath.InvalidJsonException;
import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map.Entry;

public class GsonJsonProvider extends AbstractJsonProvider {

    private static final JsonParser PARSER = new JsonParser();

    private final Gson gson;

    public GsonJsonProvider() {
        this(new Gson());
    }

    public GsonJsonProvider(Gson gson) {
        this.gson = gson;
    }

    @Override
    public Object unwrap(Object o) {
        if (o == null) {
            return null;
        } else if (!(o instanceof JsonElement e)) {
            return o;
        } else if (e.isJsonNull()) {
            return null;
        } else {
            if (e.isJsonPrimitive()) {
                JsonPrimitive p = e.getAsJsonPrimitive();
                if (p.isString()) {
                    return p.getAsString();
                }
                if (p.isBoolean()) {
                    return p.getAsBoolean();
                }
                if (p.isNumber()) {
                    return unwrapNumber(p.getAsNumber());
                }
            }
            return o;
        }
    }

    private static boolean isPrimitiveNumber(Number n) {
        return n instanceof Integer || n instanceof Float || n instanceof Double || n instanceof Long || n instanceof BigDecimal || n instanceof BigInteger;
    }

    private static Number unwrapNumber(Number n) {
        Number unwrapped;
        if (!isPrimitiveNumber(n)) {
            BigDecimal bigDecimal = new BigDecimal(n.toString());
            if (bigDecimal.scale() <= 0) {
                if (bigDecimal.abs().compareTo(new BigDecimal(Integer.MAX_VALUE)) <= 0) {
                    unwrapped = bigDecimal.intValue();
                } else if (bigDecimal.abs().compareTo(new BigDecimal(Long.MAX_VALUE)) <= 0) {
                    unwrapped = bigDecimal.longValue();
                } else {
                    unwrapped = bigDecimal;
                }
            } else {
                double doubleValue = bigDecimal.doubleValue();
                if (BigDecimal.valueOf(doubleValue).compareTo(bigDecimal) != 0) {
                    unwrapped = bigDecimal;
                } else {
                    unwrapped = doubleValue;
                }
            }
        } else {
            unwrapped = n;
        }
        return unwrapped;
    }

    @Override
    public Object parse(String json) throws InvalidJsonException {
        return PARSER.parse(json);
    }

    @Override
    public Object parse(InputStream jsonStream, String charset) throws InvalidJsonException {
        try {
            return PARSER.parse(new InputStreamReader(jsonStream, charset));
        } catch (UnsupportedEncodingException var4) {
            throw new JsonPathException(var4);
        }
    }

    @Override
    public String toJson(Object obj) {
        return this.gson.toJson(obj);
    }

    @Override
    public Object createArray() {
        return new JsonArray();
    }

    @Override
    public Object createMap() {
        return new JsonObject();
    }

    @Override
    public boolean isArray(Object obj) {
        return obj instanceof JsonArray || obj instanceof List;
    }

    @Override
    public Object getArrayIndex(Object obj, int idx) {
        return this.toJsonArray(obj).get(idx);
    }

    @Override
    public void setArrayIndex(Object array, int index, Object newValue) {
        if (!this.isArray(array)) {
            throw new UnsupportedOperationException();
        } else {
            JsonArray arr = this.toJsonArray(array);
            if (index == arr.size()) {
                arr.add(this.createJsonElement(newValue));
            } else {
                arr.set(index, this.createJsonElement(newValue));
            }
        }
    }

    @Override
    public Object getMapValue(Object obj, String key) {
        JsonObject jsonObject = this.toJsonObject(obj);
        Object o = jsonObject.get(key);
        return !jsonObject.has(key) ? UNDEFINED : this.unwrap(o);
    }

    @Override
    public void setProperty(Object obj, Object key, Object value) {
        if (this.isMap(obj)) {
            this.toJsonObject(obj).add(key.toString(), this.createJsonElement(value));
        } else {
            JsonArray array = this.toJsonArray(obj);
            int index;
            if (key != null) {
                index = key instanceof Integer ? (Integer) key : Integer.parseInt(key.toString());
            } else {
                index = array.size();
            }
            if (index == array.size()) {
                array.add(this.createJsonElement(value));
            } else {
                array.set(index, this.createJsonElement(value));
            }
        }
    }

    @Override
    public void removeProperty(Object obj, Object key) {
        if (this.isMap(obj)) {
            this.toJsonObject(obj).remove(key.toString());
        } else {
            JsonArray array = this.toJsonArray(obj);
            int index = key instanceof Integer ? (Integer) key : Integer.parseInt(key.toString());
            array.remove(index);
        }
    }

    @Override
    public boolean isMap(Object obj) {
        return obj instanceof JsonObject;
    }

    @Override
    public Collection<String> getPropertyKeys(Object obj) {
        List<String> keys = new ArrayList();
        for (Entry<String, JsonElement> entry : this.toJsonObject(obj).entrySet()) {
            keys.add((String) entry.getKey());
        }
        return keys;
    }

    @Override
    public int length(Object obj) {
        if (this.isArray(obj)) {
            return this.toJsonArray(obj).size();
        } else if (this.isMap(obj)) {
            return this.toJsonObject(obj).entrySet().size();
        } else {
            if (obj instanceof JsonElement) {
                JsonElement element = this.toJsonElement(obj);
                if (element.isJsonPrimitive()) {
                    return element.toString().length();
                }
            }
            throw new JsonPathException("length operation can not applied to " + (obj != null ? obj.getClass().getName() : "null"));
        }
    }

    @Override
    public Iterable<?> toIterable(Object obj) {
        JsonArray arr = this.toJsonArray(obj);
        List<Object> values = new ArrayList(arr.size());
        for (Object o : arr) {
            values.add(this.unwrap(o));
        }
        return values;
    }

    private JsonElement createJsonElement(Object o) {
        return this.gson.toJsonTree(o);
    }

    private JsonArray toJsonArray(Object o) {
        return (JsonArray) o;
    }

    private JsonObject toJsonObject(Object o) {
        return (JsonObject) o;
    }

    private JsonElement toJsonElement(Object o) {
        return (JsonElement) o;
    }
}