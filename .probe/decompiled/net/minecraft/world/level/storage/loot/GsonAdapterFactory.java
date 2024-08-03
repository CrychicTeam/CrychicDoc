package net.minecraft.world.level.storage.loot;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import java.lang.reflect.Type;
import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class GsonAdapterFactory {

    public static <E, T extends SerializerType<E>> GsonAdapterFactory.Builder<E, T> builder(Registry<T> registryT0, String string1, String string2, Function<E, T> functionET3) {
        return new GsonAdapterFactory.Builder<>(registryT0, string1, string2, functionET3);
    }

    public static class Builder<E, T extends SerializerType<E>> {

        private final Registry<T> registry;

        private final String elementName;

        private final String typeKey;

        private final Function<E, T> typeGetter;

        @Nullable
        private Pair<T, GsonAdapterFactory.InlineSerializer<? extends E>> inlineType;

        @Nullable
        private T defaultType;

        Builder(Registry<T> registryT0, String string1, String string2, Function<E, T> functionET3) {
            this.registry = registryT0;
            this.elementName = string1;
            this.typeKey = string2;
            this.typeGetter = functionET3;
        }

        public GsonAdapterFactory.Builder<E, T> withInlineSerializer(T t0, GsonAdapterFactory.InlineSerializer<? extends E> gsonAdapterFactoryInlineSerializerExtendsE1) {
            this.inlineType = Pair.of(t0, gsonAdapterFactoryInlineSerializerExtendsE1);
            return this;
        }

        public GsonAdapterFactory.Builder<E, T> withDefaultType(T t0) {
            this.defaultType = t0;
            return this;
        }

        public Object build() {
            return new GsonAdapterFactory.JsonAdapter<>(this.registry, this.elementName, this.typeKey, this.typeGetter, this.defaultType, this.inlineType);
        }
    }

    public interface InlineSerializer<T> {

        JsonElement serialize(T var1, JsonSerializationContext var2);

        T deserialize(JsonElement var1, JsonDeserializationContext var2);
    }

    static class JsonAdapter<E, T extends SerializerType<E>> implements JsonDeserializer<E>, JsonSerializer<E> {

        private final Registry<T> registry;

        private final String elementName;

        private final String typeKey;

        private final Function<E, T> typeGetter;

        @Nullable
        private final T defaultType;

        @Nullable
        private final Pair<T, GsonAdapterFactory.InlineSerializer<? extends E>> inlineType;

        JsonAdapter(Registry<T> registryT0, String string1, String string2, Function<E, T> functionET3, @Nullable T t4, @Nullable Pair<T, GsonAdapterFactory.InlineSerializer<? extends E>> pairTGsonAdapterFactoryInlineSerializerExtendsE5) {
            this.registry = registryT0;
            this.elementName = string1;
            this.typeKey = string2;
            this.typeGetter = functionET3;
            this.defaultType = t4;
            this.inlineType = pairTGsonAdapterFactoryInlineSerializerExtendsE5;
        }

        public E deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            if (jsonElement0.isJsonObject()) {
                JsonObject $$3 = GsonHelper.convertToJsonObject(jsonElement0, this.elementName);
                String $$4 = GsonHelper.getAsString($$3, this.typeKey, "");
                T $$5;
                if ($$4.isEmpty()) {
                    $$5 = this.defaultType;
                } else {
                    ResourceLocation $$6 = new ResourceLocation($$4);
                    $$5 = this.registry.get($$6);
                }
                if ($$5 == null) {
                    throw new JsonSyntaxException("Unknown type '" + $$4 + "'");
                } else {
                    return (E) $$5.getSerializer().deserialize($$3, jsonDeserializationContext2);
                }
            } else if (this.inlineType == null) {
                throw new UnsupportedOperationException("Object " + jsonElement0 + " can't be deserialized");
            } else {
                return (E) ((GsonAdapterFactory.InlineSerializer) this.inlineType.getSecond()).deserialize(jsonElement0, jsonDeserializationContext2);
            }
        }

        public JsonElement serialize(E e0, Type type1, JsonSerializationContext jsonSerializationContext2) {
            T $$3 = (T) this.typeGetter.apply(e0);
            if (this.inlineType != null && this.inlineType.getFirst() == $$3) {
                return ((GsonAdapterFactory.InlineSerializer) this.inlineType.getSecond()).serialize(e0, jsonSerializationContext2);
            } else if ($$3 == null) {
                throw new JsonSyntaxException("Unknown type: " + e0);
            } else {
                JsonObject $$4 = new JsonObject();
                $$4.addProperty(this.typeKey, this.registry.getKey($$3).toString());
                $$3.getSerializer().serialize($$4, e0, jsonSerializationContext2);
                return $$4;
            }
        }
    }
}