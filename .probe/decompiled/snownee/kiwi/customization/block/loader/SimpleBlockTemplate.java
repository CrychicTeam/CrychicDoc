package snownee.kiwi.customization.block.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;

public final class SimpleBlockTemplate extends KBlockTemplate {

    private final String clazz;

    private Function<BlockBehaviour.Properties, Block> constructor;

    public SimpleBlockTemplate(Optional<BlockDefinitionProperties> properties, String clazz) {
        super(properties);
        this.clazz = clazz;
    }

    public static Codec<SimpleBlockTemplate> directCodec(MapCodec<Optional<KMaterial>> materialCodec) {
        return RecordCodecBuilder.create(instance -> instance.group(BlockDefinitionProperties.mapCodecField(materialCodec).forGetter(KBlockTemplate::properties), Codec.STRING.optionalFieldOf("class", "").forGetter(SimpleBlockTemplate::clazz)).apply(instance, SimpleBlockTemplate::new));
    }

    @Override
    public KBlockTemplate.Type<?> type() {
        return KBlockTemplates.SIMPLE.getOrCreate();
    }

    @Override
    public void resolve(ResourceLocation key) {
        if (this.clazz.isEmpty()) {
            this.constructor = BlockCodecs.SIMPLE_BLOCK_FACTORY;
        } else {
            try {
                Class<?> clazz = Class.forName(this.clazz);
                this.constructor = $ -> {
                    try {
                        return (Block) clazz.getConstructor(BlockBehaviour.Properties.class).newInstance($);
                    } catch (Throwable var3x) {
                        throw new RuntimeException(var3x);
                    }
                };
            } catch (Throwable var3) {
                throw new IllegalStateException(var3);
            }
        }
    }

    @Override
    public Block createBlock(ResourceLocation id, BlockBehaviour.Properties settings, JsonObject input) {
        return (Block) this.constructor.apply(settings);
    }

    public String clazz() {
        return this.clazz;
    }

    public String toString() {
        return "SimpleBlockTemplate[properties=" + this.properties + ", clazz=" + this.clazz + ", constructor=" + this.constructor + "]";
    }
}