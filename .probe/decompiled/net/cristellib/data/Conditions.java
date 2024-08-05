package net.cristellib.data;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.cristellib.ModLoadingUtil;

public class Conditions {

    public static boolean readConditions(JsonObject object) {
        if (!object.has("condition")) {
            return true;
        } else {
            JsonArray array = object.get("condition").getAsJsonArray();
            boolean bl = true;
            for (JsonElement e : array) {
                if (e instanceof JsonObject) {
                    JsonObject o = (JsonObject) e;
                    if (!readCondition(o)) {
                        bl = false;
                    }
                }
            }
            return bl;
        }
    }

    public static boolean readCondition(JsonObject object) {
        String type = object.get("type").getAsString();
        return type.equals("mod_loaded") ? ModLoadingUtil.isModLoaded(object.get("mod").getAsString()) : false;
    }
}