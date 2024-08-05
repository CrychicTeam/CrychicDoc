package dev.xkmc.l2hostility.init.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import dev.xkmc.l2serial.serialization.codec.JsonCodec;
import java.util.Objects;
import net.minecraft.world.level.storage.loot.Serializer;

public record TraitSerializer<T>(Class<T> cls) implements Serializer<T> {

    @Override
    public void serialize(JsonObject json, T conditions, JsonSerializationContext ctx) {
        JsonCodec.toJsonObject(conditions, json);
    }

    @Override
    public T deserialize(JsonObject json, JsonDeserializationContext ctx) {
        return (T) Objects.requireNonNull(JsonCodec.from(json, this.cls, null));
    }
}