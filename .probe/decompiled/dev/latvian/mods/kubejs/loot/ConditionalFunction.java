package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class ConditionalFunction implements FunctionContainer, ConditionContainer {

    public JsonObject function = null;

    public JsonArray conditions = new JsonArray();

    public ConditionalFunction addFunction(JsonObject o) {
        this.function = o;
        return this;
    }

    public ConditionalFunction addCondition(JsonObject o) {
        this.conditions.add(o);
        return this;
    }
}