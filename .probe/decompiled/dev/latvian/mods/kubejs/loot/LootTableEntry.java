package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public class LootTableEntry implements FunctionContainer, ConditionContainer {

    public final JsonObject json;

    public LootTableEntry(JsonObject o) {
        this.json = o;
    }

    public LootTableEntry weight(int weight) {
        this.json.addProperty("weight", weight);
        return this;
    }

    public LootTableEntry quality(int quality) {
        this.json.addProperty("quality", quality);
        return this;
    }

    public LootTableEntry addFunction(JsonObject o) {
        JsonArray a = (JsonArray) this.json.get("functions");
        if (a == null) {
            a = new JsonArray();
            this.json.add("functions", a);
        }
        a.add(o);
        return this;
    }

    public LootTableEntry addCondition(JsonObject o) {
        JsonArray a = (JsonArray) this.json.get("conditions");
        if (a == null) {
            a = new JsonArray();
            this.json.add("conditions", a);
        }
        a.add(o);
        return this;
    }
}