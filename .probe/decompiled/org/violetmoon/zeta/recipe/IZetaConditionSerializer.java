package org.violetmoon.zeta.recipe;

import com.google.gson.JsonObject;
import net.minecraft.resources.ResourceLocation;

public interface IZetaConditionSerializer<T extends IZetaCondition> {

    void write(JsonObject var1, T var2);

    T read(JsonObject var1);

    ResourceLocation getID();

    default JsonObject getJson(T value) {
        JsonObject json = new JsonObject();
        this.write(json, value);
        json.addProperty("type", value.getID().toString());
        return json;
    }
}