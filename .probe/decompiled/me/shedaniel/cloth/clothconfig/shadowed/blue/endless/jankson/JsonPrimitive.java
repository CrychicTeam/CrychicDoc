package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

import java.util.Objects;
import javax.annotation.Nonnull;

public class JsonPrimitive extends JsonElement {

    public static JsonPrimitive TRUE = new JsonPrimitive(Boolean.TRUE);

    public static JsonPrimitive FALSE = new JsonPrimitive(Boolean.FALSE);

    @Nonnull
    private Object value;

    public JsonPrimitive(@Nonnull Object value) {
        this.value = value;
    }

    @Nonnull
    public String asString() {
        return this.value == null ? "null" : this.value.toString();
    }

    public boolean asBoolean(boolean defaultValue) {
        return this.value instanceof Boolean ? (Boolean) this.value : defaultValue;
    }

    public byte asByte(byte defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).byteValue() : defaultValue;
    }

    public char asChar(char defaultValue) {
        if (this.value instanceof Number) {
            return (char) ((Number) this.value).intValue();
        } else if (this.value instanceof Character) {
            return (Character) this.value;
        } else if (this.value instanceof String) {
            return ((String) this.value).length() == 1 ? ((String) this.value).charAt(0) : defaultValue;
        } else {
            return defaultValue;
        }
    }

    public short asShort(short defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).shortValue() : defaultValue;
    }

    public int asInt(int defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).intValue() : defaultValue;
    }

    public long asLong(long defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).longValue() : defaultValue;
    }

    public float asFloat(float defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).floatValue() : defaultValue;
    }

    public double asDouble(double defaultValue) {
        return this.value instanceof Number ? ((Number) this.value).doubleValue() : defaultValue;
    }

    @Nonnull
    public String toString() {
        return this.toJson();
    }

    @Nonnull
    public Object getValue() {
        return this.value;
    }

    public boolean equals(Object other) {
        if (other == null) {
            return false;
        } else {
            return other instanceof JsonPrimitive ? Objects.equals(this.value, ((JsonPrimitive) other).value) : false;
        }
    }

    public int hashCode() {
        return this.value.hashCode();
    }

    public static String escape(String s) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            switch(ch) {
                case '\b':
                    result.append("\\b");
                    break;
                case '\t':
                    result.append("\\t");
                    break;
                case '\n':
                    result.append("\\n");
                    break;
                case '\f':
                    result.append("\\f");
                    break;
                case '\r':
                    result.append("\\r");
                    break;
                case '"':
                    result.append("\\\"");
                    break;
                case '\\':
                    result.append("\\\\");
                    break;
                default:
                    result.append(ch);
            }
        }
        return result.toString();
    }

    @Override
    public String toJson(boolean comments, boolean newlines, int depth) {
        return this.toJson(JsonGrammar.builder().withComments(comments).printWhitespace(newlines).build(), depth);
    }

    @Override
    public String toJson(JsonGrammar grammar, int depth) {
        if (this.value == null) {
            return "null";
        } else if (this.value instanceof Double && grammar.bareSpecialNumerics) {
            double d = (Double) this.value;
            if (Double.isNaN(d)) {
                return "NaN";
            } else if (!Double.isInfinite(d)) {
                return this.value.toString();
            } else {
                return d < 0.0 ? "-Infinity" : "Infinity";
            }
        } else if (this.value instanceof Number) {
            return this.value.toString();
        } else {
            return this.value instanceof Boolean ? this.value.toString() : '"' + escape(this.value.toString()) + '"';
        }
    }

    public JsonPrimitive clone() {
        return this;
    }
}