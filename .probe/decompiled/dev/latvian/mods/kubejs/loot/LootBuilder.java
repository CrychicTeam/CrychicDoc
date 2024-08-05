package dev.latvian.mods.kubejs.loot;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.function.Consumer;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class LootBuilder implements FunctionContainer, ConditionContainer {

    public String type = "minecraft:generic";

    public ResourceLocation customId = null;

    public JsonArray pools = new JsonArray();

    public JsonArray functions = new JsonArray();

    public JsonArray conditions = new JsonArray();

    public LootBuilder(@Nullable JsonElement prev) {
        if (prev instanceof JsonObject o) {
            if (o.has("pools")) {
                this.pools = o.get("pools").getAsJsonArray();
            }
            if (o.has("functions")) {
                this.functions = o.get("functions").getAsJsonArray();
            }
            if (o.has("conditions")) {
                this.conditions = o.get("conditions").getAsJsonArray();
            }
        }
    }

    public JsonObject toJson() {
        JsonObject json = new JsonObject();
        json.addProperty("type", this.type);
        if (this.pools.size() > 0) {
            json.add("pools", this.pools);
        }
        if (this.functions.size() > 0) {
            json.add("functions", this.functions);
        }
        if (this.conditions.size() > 0) {
            json.add("conditions", this.conditions);
        }
        return json;
    }

    public void addPool(Consumer<LootBuilderPool> p) {
        LootBuilderPool pool = new LootBuilderPool();
        p.accept(pool);
        this.pools.add(pool.toJson());
    }

    public LootBuilder addFunction(JsonObject o) {
        this.functions.add(o);
        return this;
    }

    public LootBuilder addCondition(JsonObject o) {
        this.conditions.add(o);
        return this;
    }

    public void clearPools() {
        this.pools = new JsonArray();
    }

    public void clearFunctions() {
        this.functions = new JsonArray();
    }

    public void clearConditions() {
        this.conditions = new JsonArray();
    }
}