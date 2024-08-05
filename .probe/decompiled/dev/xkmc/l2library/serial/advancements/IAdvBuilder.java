package dev.xkmc.l2library.serial.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import net.minecraft.advancements.Advancement;

public interface IAdvBuilder {

    default void modify(String id, Advancement.Builder builder) {
    }

    default void modifyJson(JsonObject obj) {
        JsonArray conditions = obj.get("conditions") instanceof JsonArray arr ? arr : new JsonArray();
        this.addConditions(conditions);
        if (conditions.size() > 0) {
            obj.add("conditions", conditions);
        }
    }

    default void addConditions(JsonArray conditions) {
    }

    default void onBuild() {
    }
}