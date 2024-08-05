package snownee.kiwi.customization.item.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.Encoder;
import com.mojang.serialization.codecs.KeyDispatchCodec;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ExtraCodecs;
import snownee.kiwi.util.codec.CustomizationCodecs;

public record ConfiguredItemTemplate(KItemTemplate template, JsonObject json) {

    public static final JsonObject DEFAULT_JSON = new JsonObject();

    public ConfiguredItemTemplate(KItemTemplate template) {
        this(template, DEFAULT_JSON);
    }

    public static Codec<ConfiguredItemTemplate> codec(Map<ResourceLocation, KItemTemplate> templates) {
        Function<ConfiguredItemTemplate, DataResult<KItemTemplate>> type = $ -> DataResult.success($.template());
        Function<KItemTemplate, DataResult<Codec<ConfiguredItemTemplate>>> codec = $ -> DataResult.success(ExtraCodecs.JSON.flatXmap(json -> DataResult.success(new ConfiguredItemTemplate($, json.getAsJsonObject())), template -> DataResult.error(() -> "Unsupported operation")));
        return CustomizationCodecs.withAlternative(KeyDispatchCodec.unsafe("kiwi:type", CustomizationCodecs.simpleByNameCodec(templates), type, codec, v -> getCodec(type, codec, v)).codec(), ResourceLocation.CODEC.flatXmap(id -> {
            KItemTemplate template = (KItemTemplate) templates.get(id);
            return template == null ? DataResult.error(() -> "Unknown template: " + id) : DataResult.success(new ConfiguredItemTemplate(template));
        }, template -> DataResult.error(() -> "Unsupported operation")));
    }

    private static <K, V> DataResult<? extends Encoder<V>> getCodec(Function<? super V, ? extends DataResult<? extends K>> type, Function<? super K, ? extends DataResult<? extends Encoder<? extends V>>> encoder, V input) {
        return ((DataResult) type.apply(input)).flatMap(k -> ((DataResult) encoder.apply(k)).map(Function.identity())).map(c -> c);
    }

    static {
        DEFAULT_JSON.add("properties", new JsonObject());
    }
}