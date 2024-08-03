package net.minecraft.client.renderer.block.model;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.multipart.MultiPart;
import net.minecraft.client.renderer.block.model.multipart.Selector;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class BlockModelDefinition {

    private final Map<String, MultiVariant> variants = Maps.newLinkedHashMap();

    private MultiPart multiPart;

    public static BlockModelDefinition fromStream(BlockModelDefinition.Context blockModelDefinitionContext0, Reader reader1) {
        return GsonHelper.fromJson(blockModelDefinitionContext0.gson, reader1, BlockModelDefinition.class);
    }

    public static BlockModelDefinition fromJsonElement(BlockModelDefinition.Context blockModelDefinitionContext0, JsonElement jsonElement1) {
        return (BlockModelDefinition) blockModelDefinitionContext0.gson.fromJson(jsonElement1, BlockModelDefinition.class);
    }

    public BlockModelDefinition(Map<String, MultiVariant> mapStringMultiVariant0, MultiPart multiPart1) {
        this.multiPart = multiPart1;
        this.variants.putAll(mapStringMultiVariant0);
    }

    public BlockModelDefinition(List<BlockModelDefinition> listBlockModelDefinition0) {
        BlockModelDefinition $$1 = null;
        for (BlockModelDefinition $$2 : listBlockModelDefinition0) {
            if ($$2.isMultiPart()) {
                this.variants.clear();
                $$1 = $$2;
            }
            this.variants.putAll($$2.variants);
        }
        if ($$1 != null) {
            this.multiPart = $$1.multiPart;
        }
    }

    @VisibleForTesting
    public boolean hasVariant(String string0) {
        return this.variants.get(string0) != null;
    }

    @VisibleForTesting
    public MultiVariant getVariant(String string0) {
        MultiVariant $$1 = (MultiVariant) this.variants.get(string0);
        if ($$1 == null) {
            throw new BlockModelDefinition.MissingVariantException();
        } else {
            return $$1;
        }
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            if (object0 instanceof BlockModelDefinition $$1 && this.variants.equals($$1.variants)) {
                return this.isMultiPart() ? this.multiPart.equals($$1.multiPart) : !$$1.isMultiPart();
            }
            return false;
        }
    }

    public int hashCode() {
        return 31 * this.variants.hashCode() + (this.isMultiPart() ? this.multiPart.hashCode() : 0);
    }

    public Map<String, MultiVariant> getVariants() {
        return this.variants;
    }

    @VisibleForTesting
    public Set<MultiVariant> getMultiVariants() {
        Set<MultiVariant> $$0 = Sets.newHashSet(this.variants.values());
        if (this.isMultiPart()) {
            $$0.addAll(this.multiPart.getMultiVariants());
        }
        return $$0;
    }

    public boolean isMultiPart() {
        return this.multiPart != null;
    }

    public MultiPart getMultiPart() {
        return this.multiPart;
    }

    public static final class Context {

        protected final Gson gson = new GsonBuilder().registerTypeAdapter(BlockModelDefinition.class, new BlockModelDefinition.Deserializer()).registerTypeAdapter(Variant.class, new Variant.Deserializer()).registerTypeAdapter(MultiVariant.class, new MultiVariant.Deserializer()).registerTypeAdapter(MultiPart.class, new MultiPart.Deserializer(this)).registerTypeAdapter(Selector.class, new Selector.Deserializer()).create();

        private StateDefinition<Block, BlockState> definition;

        public StateDefinition<Block, BlockState> getDefinition() {
            return this.definition;
        }

        public void setDefinition(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0) {
            this.definition = stateDefinitionBlockBlockState0;
        }
    }

    public static class Deserializer implements JsonDeserializer<BlockModelDefinition> {

        public BlockModelDefinition deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            JsonObject $$3 = jsonElement0.getAsJsonObject();
            Map<String, MultiVariant> $$4 = this.getVariants(jsonDeserializationContext2, $$3);
            MultiPart $$5 = this.getMultiPart(jsonDeserializationContext2, $$3);
            if (!$$4.isEmpty() || $$5 != null && !$$5.getMultiVariants().isEmpty()) {
                return new BlockModelDefinition($$4, $$5);
            } else {
                throw new JsonParseException("Neither 'variants' nor 'multipart' found");
            }
        }

        protected Map<String, MultiVariant> getVariants(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            Map<String, MultiVariant> $$2 = Maps.newHashMap();
            if (jsonObject1.has("variants")) {
                JsonObject $$3 = GsonHelper.getAsJsonObject(jsonObject1, "variants");
                for (Entry<String, JsonElement> $$4 : $$3.entrySet()) {
                    $$2.put((String) $$4.getKey(), (MultiVariant) jsonDeserializationContext0.deserialize((JsonElement) $$4.getValue(), MultiVariant.class));
                }
            }
            return $$2;
        }

        @Nullable
        protected MultiPart getMultiPart(JsonDeserializationContext jsonDeserializationContext0, JsonObject jsonObject1) {
            if (!jsonObject1.has("multipart")) {
                return null;
            } else {
                JsonArray $$2 = GsonHelper.getAsJsonArray(jsonObject1, "multipart");
                return (MultiPart) jsonDeserializationContext0.deserialize($$2, MultiPart.class);
            }
        }
    }

    protected class MissingVariantException extends RuntimeException {
    }
}