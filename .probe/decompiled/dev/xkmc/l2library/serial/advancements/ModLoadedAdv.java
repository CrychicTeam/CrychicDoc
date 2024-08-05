package dev.xkmc.l2library.serial.advancements;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

public record ModLoadedAdv(String... modid) implements IAdvBuilder {

    @Override
    public void addConditions(JsonArray conditions) {
        for (String str : this.modid) {
            JsonObject condition = new JsonObject();
            condition.addProperty("type", "forge:mod_loaded");
            condition.addProperty("modid", str);
            conditions.add(condition);
        }
    }
}