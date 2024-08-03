package com.mna.tools;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import java.util.Map.Entry;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.nbt.ListTag;

public class NBTUtilities {

    public static CompoundTag fromJSON(JsonObject object) {
        CompoundTag output = new CompoundTag();
        for (Entry<String, JsonElement> elem : object.entrySet()) {
            if (((JsonElement) elem.getValue()).isJsonPrimitive()) {
                JsonPrimitive primitive = ((JsonElement) elem.getValue()).getAsJsonPrimitive();
                if (primitive.isBoolean()) {
                    output.putBoolean((String) elem.getKey(), primitive.getAsBoolean());
                } else if (primitive.isNumber()) {
                    Number val = primitive.getAsNumber();
                    if (val.floatValue() % 1.0F == 0.0F) {
                        output.putInt((String) elem.getKey(), val.intValue());
                    } else {
                        output.putFloat((String) elem.getKey(), val.floatValue());
                    }
                } else if (primitive.isString()) {
                    output.putString((String) elem.getKey(), primitive.getAsString());
                }
            } else if (((JsonElement) elem.getValue()).isJsonObject()) {
                output.put((String) elem.getKey(), fromJSON(((JsonElement) elem.getValue()).getAsJsonObject()));
            }
        }
        return output;
    }

    public static ListTag writeVec3i(Vec3i vec) {
        ListTag tag = new ListTag();
        tag.add(IntTag.valueOf(vec.getX()));
        tag.add(IntTag.valueOf(vec.getY()));
        tag.add(IntTag.valueOf(vec.getZ()));
        return tag;
    }
}