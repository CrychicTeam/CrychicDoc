package de.keksuccino.konkrete.json.minidev.json.reader;

import de.keksuccino.konkrete.json.minidev.json.JSONAware;
import de.keksuccino.konkrete.json.minidev.json.JSONAwareEx;
import de.keksuccino.konkrete.json.minidev.json.JSONStreamAware;
import de.keksuccino.konkrete.json.minidev.json.JSONStreamAwareEx;
import de.keksuccino.konkrete.json.minidev.json.JSONStyle;
import de.keksuccino.konkrete.json.minidev.json.JSONValue;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class JsonWriter {

    private ConcurrentHashMap<Class<?>, JsonWriterI<?>> data = new ConcurrentHashMap();

    private LinkedList<JsonWriter.WriterByInterface> writerInterfaces = new LinkedList();

    public static final JsonWriterI<JSONStreamAwareEx> JSONStreamAwareWriter = new JsonWriterI<JSONStreamAwareEx>() {

        public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            value.writeJSONString(out);
        }
    };

    public static final JsonWriterI<JSONStreamAwareEx> JSONStreamAwareExWriter = new JsonWriterI<JSONStreamAwareEx>() {

        public <E extends JSONStreamAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            value.writeJSONString(out, compression);
        }
    };

    public static final JsonWriterI<JSONAwareEx> JSONJSONAwareExWriter = new JsonWriterI<JSONAwareEx>() {

        public <E extends JSONAwareEx> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            out.append(value.toJSONString(compression));
        }
    };

    public static final JsonWriterI<JSONAware> JSONJSONAwareWriter = new JsonWriterI<JSONAware>() {

        public <E extends JSONAware> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            out.append(value.toJSONString());
        }
    };

    public static final JsonWriterI<Iterable<? extends Object>> JSONIterableWriter = new JsonWriterI<Iterable<? extends Object>>() {

        public <E extends Iterable<? extends Object>> void writeJSONString(E list, Appendable out, JSONStyle compression) throws IOException {
            boolean first = true;
            compression.arrayStart(out);
            for (Object value : list) {
                if (first) {
                    first = false;
                    compression.arrayfirstObject(out);
                } else {
                    compression.arrayNextElm(out);
                }
                if (value == null) {
                    out.append("null");
                } else {
                    JSONValue.writeJSONString(value, out, compression);
                }
                compression.arrayObjectEnd(out);
            }
            compression.arrayStop(out);
        }
    };

    public static final JsonWriterI<Enum<?>> EnumWriter = new JsonWriterI<Enum<?>>() {

        public <E extends Enum<?>> void writeJSONString(E value, Appendable out, JSONStyle compression) throws IOException {
            String s = value.name();
            compression.writeString(out, s);
        }
    };

    public static final JsonWriterI<Map<String, ? extends Object>> JSONMapWriter = new JsonWriterI<Map<String, ? extends Object>>() {

        public <E extends Map<String, ? extends Object>> void writeJSONString(E map, Appendable out, JSONStyle compression) throws IOException {
            boolean first = true;
            compression.objectStart(out);
            for (Entry<?, ?> entry : map.entrySet()) {
                Object v = entry.getValue();
                if (v != null || !compression.ignoreNull()) {
                    if (first) {
                        compression.objectFirstStart(out);
                        first = false;
                    } else {
                        compression.objectNext(out);
                    }
                    JsonWriter.writeJSONKV(entry.getKey().toString(), v, out, compression);
                }
            }
            compression.objectStop(out);
        }
    };

    public static final JsonWriterI<Object> beansWriterASM = new BeansWriterASM();

    public static final JsonWriterI<Object> beansWriter = new BeansWriter();

    public static final JsonWriterI<Object> arrayWriter = new ArrayWriter();

    public static final JsonWriterI<Object> toStringWriter = new JsonWriterI<Object>() {

        @Override
        public void writeJSONString(Object value, Appendable out, JSONStyle compression) throws IOException {
            out.append(value.toString());
        }
    };

    public JsonWriter() {
        this.init();
    }

    public <T> void remapField(Class<T> type, String fromJava, String toJson) {
        JsonWriterI map = this.getWrite(type);
        if (!(map instanceof BeansWriterASMRemap)) {
            map = new BeansWriterASMRemap();
            this.registerWriter(map, type);
        }
        ((BeansWriterASMRemap) map).renameField(fromJava, toJson);
    }

    public JsonWriterI getWriterByInterface(Class<?> clazz) {
        for (JsonWriter.WriterByInterface w : this.writerInterfaces) {
            if (w._interface.isAssignableFrom(clazz)) {
                return w._writer;
            }
        }
        return null;
    }

    public JsonWriterI getWrite(Class cls) {
        return (JsonWriterI) this.data.get(cls);
    }

    public void init() {
        this.registerWriter(new JsonWriterI<String>() {

            public void writeJSONString(String value, Appendable out, JSONStyle compression) throws IOException {
                compression.writeString(out, value);
            }
        }, String.class);
        this.registerWriter(new JsonWriterI<Double>() {

            public void writeJSONString(Double value, Appendable out, JSONStyle compression) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Double.class);
        this.registerWriter(new JsonWriterI<Date>() {

            public void writeJSONString(Date value, Appendable out, JSONStyle compression) throws IOException {
                out.append('"');
                JSONValue.escape(value.toString(), out, compression);
                out.append('"');
            }
        }, Date.class);
        this.registerWriter(new JsonWriterI<Float>() {

            public void writeJSONString(Float value, Appendable out, JSONStyle compression) throws IOException {
                if (value.isInfinite()) {
                    out.append("null");
                } else {
                    out.append(value.toString());
                }
            }
        }, Float.class);
        this.registerWriter(toStringWriter, Integer.class, Long.class, Byte.class, Short.class, BigInteger.class, BigDecimal.class);
        this.registerWriter(toStringWriter, Boolean.class);
        this.registerWriter(new JsonWriterI<int[]>() {

            public void writeJSONString(int[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (int b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Integer.toString(b));
                }
                compression.arrayStop(out);
            }
        }, int[].class);
        this.registerWriter(new JsonWriterI<short[]>() {

            public void writeJSONString(short[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (short b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Short.toString(b));
                }
                compression.arrayStop(out);
            }
        }, short[].class);
        this.registerWriter(new JsonWriterI<long[]>() {

            public void writeJSONString(long[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (long b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Long.toString(b));
                }
                compression.arrayStop(out);
            }
        }, long[].class);
        this.registerWriter(new JsonWriterI<float[]>() {

            public void writeJSONString(float[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (float b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Float.toString(b));
                }
                compression.arrayStop(out);
            }
        }, float[].class);
        this.registerWriter(new JsonWriterI<double[]>() {

            public void writeJSONString(double[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (double b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Double.toString(b));
                }
                compression.arrayStop(out);
            }
        }, double[].class);
        this.registerWriter(new JsonWriterI<boolean[]>() {

            public void writeJSONString(boolean[] value, Appendable out, JSONStyle compression) throws IOException {
                boolean needSep = false;
                compression.arrayStart(out);
                for (boolean b : value) {
                    if (needSep) {
                        compression.objectNext(out);
                    } else {
                        needSep = true;
                    }
                    out.append(Boolean.toString(b));
                }
                compression.arrayStop(out);
            }
        }, boolean[].class);
        this.registerWriterInterface(JSONStreamAwareEx.class, JSONStreamAwareExWriter);
        this.registerWriterInterface(JSONStreamAware.class, JSONStreamAwareWriter);
        this.registerWriterInterface(JSONAwareEx.class, JSONJSONAwareExWriter);
        this.registerWriterInterface(JSONAware.class, JSONJSONAwareWriter);
        this.registerWriterInterface(Map.class, JSONMapWriter);
        this.registerWriterInterface(Iterable.class, JSONIterableWriter);
        this.registerWriterInterface(Enum.class, EnumWriter);
        this.registerWriterInterface(Number.class, toStringWriter);
    }

    /**
     * @deprecated
     */
    public void addInterfaceWriterFirst(Class<?> interFace, JsonWriterI<?> writer) {
        this.registerWriterInterfaceFirst(interFace, writer);
    }

    /**
     * @deprecated
     */
    public void addInterfaceWriterLast(Class<?> interFace, JsonWriterI<?> writer) {
        this.registerWriterInterfaceLast(interFace, writer);
    }

    public void registerWriterInterfaceLast(Class<?> interFace, JsonWriterI<?> writer) {
        this.writerInterfaces.addLast(new JsonWriter.WriterByInterface(interFace, writer));
    }

    public void registerWriterInterfaceFirst(Class<?> interFace, JsonWriterI<?> writer) {
        this.writerInterfaces.addFirst(new JsonWriter.WriterByInterface(interFace, writer));
    }

    public void registerWriterInterface(Class<?> interFace, JsonWriterI<?> writer) {
        this.registerWriterInterfaceLast(interFace, writer);
    }

    public <T> void registerWriter(JsonWriterI<T> writer, Class<?>... cls) {
        for (Class<?> c : cls) {
            this.data.put(c, writer);
        }
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
        compression.objectEndOfKey(out);
        if (value instanceof String) {
            compression.writeString(out, (String) value);
        } else {
            JSONValue.writeJSONString(value, out, compression);
        }
        compression.objectElmStop(out);
    }

    static class WriterByInterface {

        public Class<?> _interface;

        public JsonWriterI<?> _writer;

        public WriterByInterface(Class<?> _interface, JsonWriterI<?> _writer) {
            this._interface = _interface;
            this._writer = _writer;
        }
    }
}