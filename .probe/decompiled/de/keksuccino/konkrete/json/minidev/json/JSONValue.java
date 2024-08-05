package de.keksuccino.konkrete.json.minidev.json;

import de.keksuccino.konkrete.json.minidev.json.parser.JSONParser;
import de.keksuccino.konkrete.json.minidev.json.parser.ParseException;
import de.keksuccino.konkrete.json.minidev.json.reader.JsonWriter;
import de.keksuccino.konkrete.json.minidev.json.reader.JsonWriterI;
import de.keksuccino.konkrete.json.minidev.json.writer.CompessorMapper;
import de.keksuccino.konkrete.json.minidev.json.writer.FakeMapper;
import de.keksuccino.konkrete.json.minidev.json.writer.JsonReader;
import de.keksuccino.konkrete.json.minidev.json.writer.JsonReaderI;
import de.keksuccino.konkrete.json.minidev.json.writer.UpdaterMapper;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;

public class JSONValue {

    public static JSONStyle COMPRESSION = JSONStyle.NO_COMPRESS;

    public static final JsonWriter defaultWriter = new JsonWriter();

    public static final JsonReader defaultReader = new JsonReader();

    public static Object parse(InputStream in) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
        } catch (Exception var2) {
            return null;
        }
    }

    public static Object parse(byte[] in) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T parse(InputStream in, Class<T> mapTo) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, defaultReader.getMapper(mapTo));
        } catch (Exception var3) {
            return null;
        }
    }

    public static Object parse(Reader in) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in);
        } catch (Exception var2) {
            return null;
        }
    }

    public static <T> T parse(byte[] in, Class<T> mapTo) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, defaultReader.getMapper(mapTo));
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T parse(Reader in, Class<T> mapTo) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, defaultReader.getMapper(mapTo));
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T parse(Reader in, T toUpdate) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, new UpdaterMapper<>(defaultReader, toUpdate));
        } catch (Exception var3) {
            return null;
        }
    }

    protected static <T> T parse(Reader in, JsonReaderI<T> mapper) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, mapper);
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T parse(String in, Class<T> mapTo) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, defaultReader.getMapper(mapTo));
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T parse(InputStream in, T toUpdate) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, new UpdaterMapper<>(defaultReader, toUpdate));
        } catch (Exception var3) {
            return null;
        }
    }

    public static <T> T parse(String in, T toUpdate) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, new UpdaterMapper<>(defaultReader, toUpdate));
        } catch (Exception var3) {
            return null;
        }
    }

    protected static <T> T parse(byte[] in, JsonReaderI<T> mapper) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, mapper);
        } catch (Exception var3) {
            return null;
        }
    }

    protected static <T> T parse(String in, JsonReaderI<T> mapper) {
        try {
            JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
            return p.parse(in, mapper);
        } catch (Exception var3) {
            return null;
        }
    }

    public static Object parse(String s) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(s);
        } catch (Exception var2) {
            return null;
        }
    }

    public static Object parseKeepingOrder(Reader in) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, defaultReader.DEFAULT_ORDERED);
        } catch (Exception var2) {
            return null;
        }
    }

    public static Object parseKeepingOrder(String in) {
        try {
            return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, defaultReader.DEFAULT_ORDERED);
        } catch (Exception var2) {
            return null;
        }
    }

    public static String compress(String input, JSONStyle style) {
        try {
            StringBuilder sb = new StringBuilder();
            new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(input, new CompessorMapper(defaultReader, sb, style));
            return sb.toString();
        } catch (Exception var3) {
            return input;
        }
    }

    public static String compress(String input) {
        return compress(input, JSONStyle.MAX_COMPRESS);
    }

    public static String uncompress(String input) {
        return compress(input, JSONStyle.NO_COMPRESS);
    }

    public static Object parseWithException(byte[] in) throws IOException, ParseException {
        return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, defaultReader.DEFAULT);
    }

    public static Object parseWithException(InputStream in) throws IOException, ParseException {
        return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, defaultReader.DEFAULT);
    }

    public static Object parseWithException(Reader in) throws IOException, ParseException {
        return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, defaultReader.DEFAULT);
    }

    public static Object parseWithException(String input) throws ParseException {
        return new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(input, defaultReader.DEFAULT);
    }

    public static <T> T parseWithException(String in, Class<T> mapTo) throws ParseException {
        JSONParser p = new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE);
        return p.parse(in, defaultReader.getMapper(mapTo));
    }

    public static Object parseStrict(Reader in) throws IOException, ParseException {
        return new JSONParser(656).parse(in, defaultReader.DEFAULT);
    }

    public static Object parseStrict(String s) throws ParseException {
        return new JSONParser(656).parse(s, defaultReader.DEFAULT);
    }

    public static boolean isValidJsonStrict(Reader in) throws IOException {
        try {
            new JSONParser(656).parse(in, FakeMapper.DEFAULT);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    public static boolean isValidJsonStrict(String s) {
        try {
            new JSONParser(656).parse(s, FakeMapper.DEFAULT);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    public static boolean isValidJson(Reader in) throws IOException {
        try {
            new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(in, FakeMapper.DEFAULT);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    public static boolean isValidJson(String s) {
        try {
            new JSONParser(JSONParser.DEFAULT_PERMISSIVE_MODE).parse(s, FakeMapper.DEFAULT);
            return true;
        } catch (ParseException var2) {
            return false;
        }
    }

    public static void writeJSONString(Object value, Appendable out) throws IOException {
        writeJSONString(value, out, COMPRESSION);
    }

    public static <T> void remapField(Class<T> type, String jsonFieldName, String javaFieldName) {
        defaultReader.remapField(type, jsonFieldName, javaFieldName);
        defaultWriter.remapField(type, javaFieldName, jsonFieldName);
    }

    public static <T> void registerWriter(Class<?> cls, JsonWriterI<T> writer) {
        defaultWriter.registerWriter(writer, cls);
    }

    public static <T> void registerReader(Class<T> type, JsonReaderI<T> mapper) {
        defaultReader.registerReader(type, mapper);
    }

    public static void writeJSONString(Object value, Appendable out, JSONStyle compression) throws IOException {
        if (value == null) {
            out.append("null");
        } else {
            Class<?> clz = value.getClass();
            JsonWriterI w = defaultWriter.getWrite(clz);
            if (w == null) {
                if (clz.isArray()) {
                    w = JsonWriter.arrayWriter;
                } else {
                    w = defaultWriter.getWriterByInterface(value.getClass());
                    if (w == null) {
                        w = JsonWriter.beansWriterASM;
                    }
                }
                defaultWriter.registerWriter(w, clz);
            }
            w.writeJSONString(value, out, compression);
        }
    }

    public static String toJSONString(Object value) {
        return toJSONString(value, COMPRESSION);
    }

    public static String toJSONString(Object value, JSONStyle compression) {
        StringBuilder sb = new StringBuilder();
        try {
            writeJSONString(value, sb, compression);
        } catch (IOException var4) {
        }
        return sb.toString();
    }

    public static String escape(String s) {
        return escape(s, COMPRESSION);
    }

    public static String escape(String s, JSONStyle compression) {
        if (s == null) {
            return null;
        } else {
            StringBuilder sb = new StringBuilder();
            compression.escape(s, sb);
            return sb.toString();
        }
    }

    public static void escape(String s, Appendable ap) {
        escape(s, ap, COMPRESSION);
    }

    public static void escape(String s, Appendable ap, JSONStyle compression) {
        if (s != null) {
            compression.escape(s, ap);
        }
    }
}