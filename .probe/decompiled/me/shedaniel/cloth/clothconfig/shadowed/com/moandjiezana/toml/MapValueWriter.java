package me.shedaniel.cloth.clothconfig.shadowed.com.moandjiezana.toml;

import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MapValueWriter implements ValueWriter {

    static final ValueWriter MAP_VALUE_WRITER = new MapValueWriter();

    private static final Pattern REQUIRED_QUOTING_PATTERN = Pattern.compile("^.*[^A-Za-z\\d_-].*$");

    @Override
    public boolean canWrite(Object value) {
        return value instanceof Map;
    }

    @Override
    public void write(Object value, WriterContext context) {
        Map<?, ?> from = (Map<?, ?>) value;
        if (hasPrimitiveValues(from, context)) {
            context.writeKey();
        }
        for (Entry<?, ?> entry : from.entrySet()) {
            Object key = entry.getKey();
            Object fromValue = entry.getValue();
            if (fromValue != null) {
                ValueWriter valueWriter = ValueWriters.WRITERS.findWriterFor(fromValue);
                if (valueWriter.isPrimitiveType()) {
                    context.indent();
                    context.write(quoteKey(key)).write(" = ");
                    valueWriter.write(fromValue, context);
                    context.write('\n');
                } else if (valueWriter == PrimitiveArrayValueWriter.PRIMITIVE_ARRAY_VALUE_WRITER) {
                    context.setArrayKey(key.toString());
                    context.write(quoteKey(key)).write(" = ");
                    valueWriter.write(fromValue, context);
                    context.write('\n');
                }
            }
        }
        for (Object key : from.keySet()) {
            Object fromValue = from.get(key);
            if (fromValue != null) {
                ValueWriter valueWriter = ValueWriters.WRITERS.findWriterFor(fromValue);
                if (valueWriter == this || valueWriter == ObjectValueWriter.OBJECT_VALUE_WRITER || valueWriter == TableArrayValueWriter.TABLE_ARRAY_VALUE_WRITER) {
                    valueWriter.write(fromValue, context.pushTable(quoteKey(key)));
                }
            }
        }
    }

    @Override
    public boolean isPrimitiveType() {
        return false;
    }

    private static String quoteKey(Object key) {
        String stringKey = key.toString();
        Matcher matcher = REQUIRED_QUOTING_PATTERN.matcher(stringKey);
        if (matcher.matches()) {
            stringKey = "\"" + stringKey + "\"";
        }
        return stringKey;
    }

    private static boolean hasPrimitiveValues(Map<?, ?> values, WriterContext context) {
        for (Object key : values.keySet()) {
            Object fromValue = values.get(key);
            if (fromValue != null) {
                ValueWriter valueWriter = ValueWriters.WRITERS.findWriterFor(fromValue);
                if (valueWriter.isPrimitiveType() || valueWriter == PrimitiveArrayValueWriter.PRIMITIVE_ARRAY_VALUE_WRITER) {
                    return true;
                }
            }
        }
        return false;
    }

    private MapValueWriter() {
    }
}