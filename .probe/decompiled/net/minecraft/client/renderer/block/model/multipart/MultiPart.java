package net.minecraft.client.renderer.block.model.multipart;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.block.model.BlockModelDefinition;
import net.minecraft.client.renderer.block.model.MultiVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.MultiPartBakedModel;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class MultiPart implements UnbakedModel {

    private final StateDefinition<Block, BlockState> definition;

    private final List<Selector> selectors;

    public MultiPart(StateDefinition<Block, BlockState> stateDefinitionBlockBlockState0, List<Selector> listSelector1) {
        this.definition = stateDefinitionBlockBlockState0;
        this.selectors = listSelector1;
    }

    public List<Selector> getSelectors() {
        return this.selectors;
    }

    public Set<MultiVariant> getMultiVariants() {
        Set<MultiVariant> $$0 = Sets.newHashSet();
        for (Selector $$1 : this.selectors) {
            $$0.add($$1.getVariant());
        }
        return $$0;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return !(object0 instanceof MultiPart $$1) ? false : Objects.equals(this.definition, $$1.definition) && Objects.equals(this.selectors, $$1.selectors);
        }
    }

    public int hashCode() {
        return Objects.hash(new Object[] { this.definition, this.selectors });
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return (Collection<ResourceLocation>) this.getSelectors().stream().flatMap(p_111969_ -> p_111969_.getVariant().getDependencies().stream()).collect(Collectors.toSet());
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> functionResourceLocationUnbakedModel0) {
        this.getSelectors().forEach(p_247936_ -> p_247936_.getVariant().resolveParents(functionResourceLocationUnbakedModel0));
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker0, Function<Material, TextureAtlasSprite> functionMaterialTextureAtlasSprite1, ModelState modelState2, ResourceLocation resourceLocation3) {
        MultiPartBakedModel.Builder $$4 = new MultiPartBakedModel.Builder();
        for (Selector $$5 : this.getSelectors()) {
            BakedModel $$6 = $$5.getVariant().bake(modelBaker0, functionMaterialTextureAtlasSprite1, modelState2, resourceLocation3);
            if ($$6 != null) {
                $$4.add($$5.getPredicate(this.definition), $$6);
            }
        }
        return $$4.build();
    }

    public static class Deserializer implements JsonDeserializer<MultiPart> {

        private final BlockModelDefinition.Context context;

        public Deserializer(BlockModelDefinition.Context blockModelDefinitionContext0) {
            this.context = blockModelDefinitionContext0;
        }

        public MultiPart deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            return new MultiPart(this.context.getDefinition(), this.getSelectors(jsonDeserializationContext2, jsonElement0.getAsJsonArray()));
        }

        private List<Selector> getSelectors(JsonDeserializationContext jsonDeserializationContext0, JsonArray jsonArray1) {
            List<Selector> $$2 = Lists.newArrayList();
            for (JsonElement $$3 : jsonArray1) {
                $$2.add((Selector) jsonDeserializationContext0.deserialize($$3, Selector.class));
            }
            return $$2;
        }
    }
}