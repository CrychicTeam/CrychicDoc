package snownee.kiwi.customization.item.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.JsonOps;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.MapLike;
import com.mojang.serialization.DataResult.PartialResult;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import snownee.kiwi.util.codec.CustomizationCodecs;

public final class BuiltInItemTemplate extends KItemTemplate {

    private final Optional<ResourceLocation> key;

    private MapCodec<Item> codec;

    public BuiltInItemTemplate(Optional<ItemDefinitionProperties> properties, Optional<ResourceLocation> key) {
        super(properties);
        this.key = key;
    }

    public static Codec<BuiltInItemTemplate> directCodec() {
        return RecordCodecBuilder.create(instance -> instance.group(ItemDefinitionProperties.mapCodecField().forGetter(KItemTemplate::properties), CustomizationCodecs.strictOptionalField(ResourceLocation.CODEC, "codec").forGetter(BuiltInItemTemplate::key)).apply(instance, BuiltInItemTemplate::new));
    }

    @Override
    public KItemTemplate.Type<?> type() {
        return KItemTemplates.BUILT_IN.getOrCreate();
    }

    @Override
    public void resolve(ResourceLocation key) {
        this.codec = ItemCodecs.get((ResourceLocation) this.key.orElse(key));
    }

    @Override
    public Item createItem(ResourceLocation id, Item.Properties properties, JsonObject json) {
        if (!json.has("properties")) {
            json.add("properties", new JsonObject());
        }
        DataResult<Item> result = this.codec.decode(JsonOps.INSTANCE, (MapLike) JsonOps.INSTANCE.getMap(json).result().orElseThrow());
        if (result.error().isPresent()) {
            throw new IllegalStateException(((PartialResult) result.error().get()).message());
        } else {
            return (Item) result.result().orElseThrow();
        }
    }

    public Optional<ResourceLocation> key() {
        return this.key;
    }

    public String toString() {
        return "BuiltInItemTemplate[properties=" + this.properties + ", key=" + this.key + ", codec=" + this.codec + "]";
    }
}