package de.keksuccino.konkrete.json.minidev.json;

import de.keksuccino.konkrete.json.minidev.json.reader.JsonWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class JSONArray extends ArrayList<Object> implements List<Object>, JSONAwareEx, JSONStreamAwareEx {

    private static final long serialVersionUID = 9106884089231309568L;

    public JSONArray() {
    }

    public JSONArray(int initialCapacity) {
        super(initialCapacity);
    }

    public static String toJSONString(List<? extends Object> list) {
        return toJSONString(list, JSONValue.COMPRESSION);
    }

    public static String toJSONString(List<? extends Object> list, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(list, sb, compression);
        } catch (IOException var4) {
        }
        return sb.toString();
    }

    public static void writeJSONString(Iterable<? extends Object> list, Appendable out, JSONStyle compression) throws IOException {
        if (list == null) {
            out.append("null");
        } else {
            JsonWriter.JSONIterableWriter.writeJSONString(list, out, compression);
        }
    }

    public static void writeJSONString(List<? extends Object> list, Appendable out) throws IOException {
        writeJSONString(list, out, JSONValue.COMPRESSION);
    }

    public JSONArray appendElement(Object element) {
        this.add(element);
        return this;
    }

    public void merge(Object o2) {
        JSONObject.merge(this, o2);
    }

    @Override
    public String toJSONString() {
        return toJSONString(this, JSONValue.COMPRESSION);
    }

    @Override
    public String toJSONString(JSONStyle compression) {
        return toJSONString(this, compression);
    }

    public String toString() {
        return this.toJSONString();
    }

    public String toString(JSONStyle compression) {
        return this.toJSONString(compression);
    }

    @Override
    public void writeJSONString(Appendable out) throws IOException {
        writeJSONString(this, out, JSONValue.COMPRESSION);
    }

    @Override
    public void writeJSONString(Appendable out, JSONStyle compression) throws IOException {
        writeJSONString(this, out, compression);
    }
}