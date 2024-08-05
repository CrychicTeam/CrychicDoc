package net.minecraft.server.packs.metadata;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;

public interface MetadataSectionType<T> extends MetadataSectionSerializer<T> {

    JsonObject toJson(T var1);

    static <T> MetadataSectionType<T> fromCodec(final String string0, final Codec<T> codecT1) {
        return new MetadataSectionType<T>() {

            @Override
            public String getMetadataSectionName() {
                return string0;
            }

            @Override
            public T fromJson(JsonObject p_249450_) {
                return (T) codecT1.parse(JsonOps.INSTANCE, p_249450_).getOrThrow(false, p_251349_ -> {
                });
            }

            @Override
            public JsonObject toJson(T p_250691_) {
                return ((JsonElement) codecT1.encodeStart(JsonOps.INSTANCE, p_250691_).getOrThrow(false, p_251583_ -> {
                })).getAsJsonObject();
            }
        };
    }
}