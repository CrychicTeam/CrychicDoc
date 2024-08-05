package dev.latvian.mods.kubejs.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import dev.latvian.mods.rhino.mod.util.JsonUtils;
import dev.latvian.mods.rhino.mod.util.NBTUtils;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import org.jetbrains.annotations.Nullable;

public interface MapJS {

    @Nullable
    static Map<?, ?> of(@Nullable Object o) {
        if (o instanceof Map) {
            return (Map<?, ?>) o;
        } else if (o instanceof CompoundTag tag) {
            LinkedHashMap<String, Tag> map = new LinkedHashMap();
            for (String key : tag.getAllKeys()) {
                map.put(key, tag.get(key));
            }
            return map;
        } else if (!(o instanceof JsonObject json)) {
            return null;
        } else {
            LinkedHashMap<String, Object> map = new LinkedHashMap();
            for (Entry<String, JsonElement> entry : json.entrySet()) {
                map.put((String) entry.getKey(), JsonUtils.toObject((JsonElement) entry.getValue()));
            }
            return map;
        }
    }

    static Map<?, ?> orEmpty(@Nullable Object o) {
        Map<?, ?> map = of(o);
        return map != null ? map : Map.of();
    }

    @Deprecated
    @Nullable
    static CompoundTag nbt(@Nullable Object map) {
        return NBTUtils.toTagCompound(map);
    }

    @Nullable
    static JsonObject json(@Nullable Object map) {
        if (map instanceof JsonObject) {
            return (JsonObject) map;
        } else if (map instanceof CharSequence) {
            try {
                return (JsonObject) JsonIO.GSON.fromJson(map.toString(), JsonObject.class);
            } catch (Exception var9) {
                return null;
            }
        } else {
            Map<?, ?> m = of(map);
            if (m == null) {
                return null;
            } else {
                JsonObject json = new JsonObject();
                for (Entry<?, ?> entry : m.entrySet()) {
                    JsonElement e = JsonIO.of(entry.getValue());
                    if (e instanceof JsonPrimitive p && p.isNumber() && p.getAsNumber() instanceof Double d && d <= 9.223372E18F && d >= -9.223372E18F && d == (double) d.longValue()) {
                        json.add(String.valueOf(entry.getKey()), new JsonPrimitive(d.longValue()));
                        continue;
                    }
                    json.add(String.valueOf(entry.getKey()), e);
                }
                return json;
            }
        }
    }
}