package dev.shadowsoffire.placebo.json;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import dev.shadowsoffire.placebo.Placebo;
import java.lang.reflect.Type;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;

public class NBTAdapter implements JsonDeserializer<CompoundTag>, JsonSerializer<CompoundTag> {

    @Deprecated
    public static final NBTAdapter INSTANCE = new NBTAdapter();

    public static final Codec<CompoundTag> EITHER_CODEC = Codec.either(Codec.STRING, CompoundTag.CODEC).xmap(either -> (CompoundTag) either.map(t -> {
        try {
            return TagParser.parseTag(t);
        } catch (CommandSyntaxException var2) {
            throw new RuntimeException(var2);
        }
    }, Function.identity()), Either::right);

    public JsonElement serialize(CompoundTag src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    public CompoundTag deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (CompoundTag) EITHER_CODEC.parse(JsonOps.INSTANCE, json).getOrThrow(false, Placebo.LOGGER::error);
    }
}