package net.minecraftforge.common.util;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.reflect.TypeParameter;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.TagParser;
import net.minecraft.util.GsonHelper;
import org.jetbrains.annotations.Nullable;

public class JsonUtils {

    private static <E> TypeToken<List<E>> listOf(Type arg) {
        return (new TypeToken<List<E>>() {
        }).where(new TypeParameter<E>() {
        }, TypeToken.of(arg));
    }

    @Nullable
    public static CompoundTag readNBT(JsonObject json, String key) {
        if (GsonHelper.isValidNode(json, key)) {
            try {
                return TagParser.parseTag(GsonHelper.getAsString(json, key));
            } catch (CommandSyntaxException var3) {
                throw new JsonSyntaxException("Malformed NBT tag", var3);
            }
        } else {
            return null;
        }
    }

    private static <E> TypeToken<Map<String, E>> mapOf(Type arg) {
        return (new TypeToken<Map<String, E>>() {
        }).where(new TypeParameter<E>() {
        }, TypeToken.of(arg));
    }

    public static enum ImmutableListTypeAdapter implements JsonDeserializer<ImmutableList<?>>, JsonSerializer<ImmutableList<?>> {

        INSTANCE;

        public ImmutableList<?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type parametrizedType = JsonUtils.listOf(typeArguments[0]).getType();
            List<?> list = (List<?>) context.deserialize(json, parametrizedType);
            return ImmutableList.copyOf(list);
        }

        public JsonElement serialize(ImmutableList<?> src, Type type, JsonSerializationContext context) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type parametrizedType = JsonUtils.listOf(typeArguments[0]).getType();
            return context.serialize(src, parametrizedType);
        }
    }

    public static enum ImmutableMapTypeAdapter implements JsonDeserializer<ImmutableMap<String, ?>>, JsonSerializer<ImmutableMap<String, ?>> {

        INSTANCE;

        public ImmutableMap<String, ?> deserialize(JsonElement json, Type type, JsonDeserializationContext context) throws JsonParseException {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type parameterizedType = JsonUtils.mapOf(typeArguments[1]).getType();
            Map<String, ?> map = (Map<String, ?>) context.deserialize(json, parameterizedType);
            return ImmutableMap.copyOf(map);
        }

        public JsonElement serialize(ImmutableMap<String, ?> src, Type type, JsonSerializationContext context) {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type parameterizedType = JsonUtils.mapOf(typeArguments[1]).getType();
            return context.serialize(src, parameterizedType);
        }
    }
}