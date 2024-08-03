package snownee.kiwi.customization.block.loader;

import com.google.gson.JsonObject;
import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import java.util.Optional;
import java.util.function.Function;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import snownee.kiwi.customization.CustomizationRegistries;

public abstract class KBlockTemplate {

    protected final Optional<BlockDefinitionProperties> properties;

    public static Codec<KBlockTemplate> codec(MapCodec<Optional<KMaterial>> materialCodec) {
        return CustomizationRegistries.BLOCK_TEMPLATE.byNameCodec().dispatch("type", KBlockTemplate::type, type -> (Codec) type.codec().apply(materialCodec));
    }

    protected KBlockTemplate(Optional<BlockDefinitionProperties> properties) {
        this.properties = properties;
    }

    public abstract KBlockTemplate.Type<?> type();

    public abstract void resolve(ResourceLocation var1);

    abstract Block createBlock(ResourceLocation var1, BlockBehaviour.Properties var2, JsonObject var3);

    public final Optional<BlockDefinitionProperties> properties() {
        return this.properties;
    }

    public static record Type<T extends KBlockTemplate>(Function<MapCodec<Optional<KMaterial>>, Codec<T>> codec) {
    }
}