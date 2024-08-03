package snownee.kiwi.customization.block.loader;

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
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class BuiltInBlockTemplate extends KBlockTemplate {

    private final Optional<ResourceLocation> key;

    private MapCodec<Block> codec;

    public BuiltInBlockTemplate(Optional<BlockDefinitionProperties> properties, Optional<ResourceLocation> key) {
        super(properties);
        this.key = key;
    }

    public static Codec<BuiltInBlockTemplate> directCodec(MapCodec<Optional<KMaterial>> materialCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(BlockDefinitionProperties.mapCodecField(materialCodec).forGetter(KBlockTemplate::properties), ResourceLocation.CODEC.optionalFieldOf("codec").forGetter(BuiltInBlockTemplate::key)).apply(instance, BuiltInBlockTemplate::new));
    }

    @Override
    public KBlockTemplate.Type<?> type() {
        return KBlockTemplates.BUILT_IN.getOrCreate();
    }

    @Override
    public void resolve(ResourceLocation key) {
        this.codec = BlockCodecs.get((ResourceLocation) this.key.orElse(key));
    }

    @Override
    public Block createBlock(ResourceLocation id, BlockBehaviour.Properties properties, JsonObject json) {
        if (!json.has("properties")) {
            json.add("properties", new JsonObject());
        }
        InjectedBlockPropertiesCodec.INJECTED.set(properties);
        DataResult<Block> result = this.codec.decode(JsonOps.INSTANCE, (MapLike) JsonOps.INSTANCE.getMap(json).result().orElseThrow());
        if (result.error().isPresent()) {
            throw new IllegalStateException(((PartialResult) result.error().get()).message());
        } else {
            return (Block) result.result().orElseThrow();
        }
    }

    public Optional<ResourceLocation> key() {
        return this.key;
    }

    public String toString() {
        return "BuiltInBlockTemplate[properties=" + this.properties + ", key=" + this.key + ", codec=" + this.codec + "]";
    }
}