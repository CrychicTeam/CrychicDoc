package dev.latvian.mods.rhino;

import org.jetbrains.annotations.Nullable;

public enum MemberType {

    UNDEFINED("undefined"),
    OBJECT("object"),
    FUNCTION("function"),
    SYMBOL("symbol"),
    STRING("string"),
    NUMBER("number"),
    BOOLEAN("boolean");

    private final String name;

    public static MemberType get(@Nullable Object value, Context cx) {
        if (value == null) {
            return OBJECT;
        } else if (value == Undefined.instance) {
            return UNDEFINED;
        } else if (value instanceof Scriptable) {
            return value instanceof Callable ? FUNCTION : ((Scriptable) value).getTypeOf();
        } else if (value instanceof CharSequence) {
            return STRING;
        } else if (value instanceof Number) {
            return NUMBER;
        } else if (value instanceof Boolean) {
            return BOOLEAN;
        } else {
            throw ScriptRuntime.errorWithClassName("msg.invalid.type", value, cx);
        }
    }

    private MemberType(String n) {
        this.name = n;
    }

    public String toString() {
        return this.name;
    }
}