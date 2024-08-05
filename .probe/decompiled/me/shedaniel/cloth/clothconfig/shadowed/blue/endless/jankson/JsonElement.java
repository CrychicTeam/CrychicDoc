package me.shedaniel.cloth.clothconfig.shadowed.blue.endless.jankson;

public abstract class JsonElement implements Cloneable {

    public abstract JsonElement clone();

    public String toJson() {
        return this.toJson(false, false, 0);
    }

    public String toJson(boolean comments, boolean newlines) {
        return this.toJson(comments, newlines, 0);
    }

    public abstract String toJson(boolean var1, boolean var2, int var3);

    public abstract String toJson(JsonGrammar var1, int var2);

    public String toJson(JsonGrammar grammar) {
        return this.toJson(grammar, 0);
    }
}