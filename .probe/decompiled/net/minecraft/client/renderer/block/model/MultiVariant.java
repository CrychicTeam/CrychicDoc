package net.minecraft.client.renderer.block.model;

import com.google.common.collect.Lists;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import javax.annotation.Nullable;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.client.resources.model.UnbakedModel;
import net.minecraft.client.resources.model.WeightedBakedModel;
import net.minecraft.resources.ResourceLocation;

public class MultiVariant implements UnbakedModel {

    private final List<Variant> variants;

    public MultiVariant(List<Variant> listVariant0) {
        this.variants = listVariant0;
    }

    public List<Variant> getVariants() {
        return this.variants;
    }

    public boolean equals(Object object0) {
        if (this == object0) {
            return true;
        } else {
            return object0 instanceof MultiVariant $$1 ? this.variants.equals($$1.variants) : false;
        }
    }

    public int hashCode() {
        return this.variants.hashCode();
    }

    @Override
    public Collection<ResourceLocation> getDependencies() {
        return (Collection<ResourceLocation>) this.getVariants().stream().map(Variant::m_111883_).collect(Collectors.toSet());
    }

    @Override
    public void resolveParents(Function<ResourceLocation, UnbakedModel> functionResourceLocationUnbakedModel0) {
        this.getVariants().stream().map(Variant::m_111883_).distinct().forEach(p_247934_ -> ((UnbakedModel) functionResourceLocationUnbakedModel0.apply(p_247934_)).resolveParents(functionResourceLocationUnbakedModel0));
    }

    @Nullable
    @Override
    public BakedModel bake(ModelBaker modelBaker0, Function<Material, TextureAtlasSprite> functionMaterialTextureAtlasSprite1, ModelState modelState2, ResourceLocation resourceLocation3) {
        if (this.getVariants().isEmpty()) {
            return null;
        } else {
            WeightedBakedModel.Builder $$4 = new WeightedBakedModel.Builder();
            for (Variant $$5 : this.getVariants()) {
                BakedModel $$6 = modelBaker0.bake($$5.getModelLocation(), $$5);
                $$4.add($$6, $$5.getWeight());
            }
            return $$4.build();
        }
    }

    public static class Deserializer implements JsonDeserializer<MultiVariant> {

        public MultiVariant deserialize(JsonElement jsonElement0, Type type1, JsonDeserializationContext jsonDeserializationContext2) throws JsonParseException {
            List<Variant> $$3 = Lists.newArrayList();
            if (jsonElement0.isJsonArray()) {
                JsonArray $$4 = jsonElement0.getAsJsonArray();
                if ($$4.size() == 0) {
                    throw new JsonParseException("Empty variant array");
                }
                for (JsonElement $$5 : $$4) {
                    $$3.add((Variant) jsonDeserializationContext2.deserialize($$5, Variant.class));
                }
            } else {
                $$3.add((Variant) jsonDeserializationContext2.deserialize(jsonElement0, Variant.class));
            }
            return new MultiVariant($$3);
        }
    }
}