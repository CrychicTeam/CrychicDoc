package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

public class JsonNull extends JsonElement {

    public static final JsonNull INSTANCE = new JsonNull();

    private JsonNull() {
    }

    public String toString() {
        return "null";
    }

    public boolean equals(Object other) {
        return other == INSTANCE;
    }

    public int hashCode() {
        return 0;
    }

    @Override
    public String toJson(boolean comments, boolean newlines, int depth) {
        return "null";
    }

    @Override
    public String toJson(JsonGrammar grammar, int depth) {
        return "null";
    }

    public JsonNull clone() {
        return this;
    }
}