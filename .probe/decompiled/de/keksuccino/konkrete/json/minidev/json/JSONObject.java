package de.keksuccino.konkrete.json.minidev.json;

import de.keksuccino.konkrete.json.minidev.json.reader.JsonWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class JSONObject extends HashMap<String, Object> implements JSONAwareEx, JSONStreamAwareEx {

    private static final long serialVersionUID = -503443796854799292L;

    public JSONObject() {
    }

    public JSONObject(int initialCapacity) {
        super(initialCapacity);
    }

    public static String escape(String s) {
        return JSONValue.escape(s);
    }

    public static String toJSONString(Map<String, ? extends Object> map) {
        return toJSONString(map, JSONValue.COMPRESSION);
    }

    public static String toJSONString(Map<String, ? extends Object> map, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSON(map, sb, compression);
        } catch (IOException var4) {
        }
        return sb.toString();
    }

    public static void writeJSONKV(String key, Object value, Appendable out, JSONStyle compression) throws IOException {
        if (key == null) {
            out.append("null");
        } else if (!compression.mustProtectKey(key)) {
            out.append(key);
        } else {
            out.append('"');
            JSONValue.escape(key, out, compression);
            out.append('"');
        }
        out.append(':');
        if (value instanceof String) {
            compression.writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out, compression);
        }
    }

    public JSONObject appendField(String fieldName, Object fieldValue) {
        this.put(fieldName, fieldValue);
        return this;
    }

    public String getAsString(String key) {
        Object obj = this.get(key);
        return obj == null ? null : obj.toString();
    }

    public Number getAsNumber(String key) {
        Object obj = this.get(key);
        if (obj == null) {
            return null;
        } else {
            return (Number) (obj instanceof Number ? (Number) obj : Long.valueOf(obj.toString()));
        }
    }

    public JSONObject(Map<String, ?> map) {
        super(map);
    }

    public static void writeJSON(Map<String, ? extends Object> map, Appendable out) throws IOException {
        writeJSON(map, out, JSONValue.COMPRESSION);
    }

    public static void writeJSON(Map<String, ? extends Object> map, Appendable out, JSONStyle compression) throws IOException {
        if (map == null) {
            out.append("null");
        } else {
            JsonWriter.JSONMapWriter.writeJSONString(map, out, compression);
        }
    }

    @Override
    public void writeJSONString(Appendable out) throws IOException {
        writeJSON(this, out, JSONValue.COMPRESSION);
    }

    @Override
    public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
        writeJSON(this, out, compression);
    }

    public void merge(Object o2) {
        merge(this, o2);
    }

    protected static JSONObject merge(JSONObject o1, Object o2) {
        if (o2 == null) {
            return o1;
        } else if (o2 instanceof JSONObject) {
            return merge(o1, (JSONObject) o2);
        } else {
            throw new RuntimeException("JSON merge can not merge JSONObject with " + o2.getClass());
        }
    }

    private static JSONObject merge(JSONObject o1, JSONObject o2) {
        if (o2 == null) {
            return o1;
        } else {
            for (String key : o1.keySet()) {
                Object value1 = o1.get(key);
                Object value2 = o2.get(key);
                if (value2 != null) {
                    if (value1 instanceof JSONArray) {
                        o1.put(key, merge((JSONArray) value1, value2));
                    } else if (value1 instanceof JSONObject) {
                        o1.put(key, merge((JSONObject) value1, value2));
                    } else if (!value1.equals(value2)) {
                        if (value1.getClass().equals(value2.getClass())) {
                            throw new RuntimeException("JSON merge can not merge two " + value1.getClass().getName() + " Object together");
                        }
                        throw new RuntimeException("JSON merge can not merge " + value1.getClass().getName() + " with " + value2.getClass().getName());
                    }
                }
            }
            for (String keyx : o2.keySet()) {
                if (!o1.containsKey(keyx)) {
                    o1.put(keyx, o2.get(keyx));
                }
            }
            return o1;
        }
    }

    protected static JSONArray merge(JSONArray o1, Object o2) {
        if (o2 == null) {
            return o1;
        } else if (o1 instanceof JSONArray) {
            return merge(o1, (JSONArray) o2);
        } else {
            o1.add(o2);
            return o1;
        }
    }

    private static JSONArray merge(JSONArray o1, JSONArray o2) {
        o1.addAll(o2);
        return o1;
    }

    @Override
    public String toJSONString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }

    @Override
    public String toJSONString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    public String toString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    public String toString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }
}