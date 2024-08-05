package net.mehvahdjukaar.moonlight.api.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.util.function.BiFunction;
import java.util.function.Function;
import net.mehvahdjukaar.moonlight.api.platform.ClientHelper;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.Material;
import net.minecraft.client.resources.model.ModelBaker;
import net.minecraft.client.resources.model.ModelBakery;
import net.minecraft.client.resources.model.ModelState;
import net.minecraft.resources.ResourceLocation;

public class NestedModelLoader implements CustomModelLoader {

    private final BiFunction<BakedModel, ModelState, CustomBakedModel> factory;

    private final String path;

    @Deprecated(forRemoval = true)
    public NestedModelLoader(String modelPath, Function<BakedModel, CustomBakedModel> bakedModelFactory) {
        this(modelPath, (BiFunction<BakedModel, ModelState, CustomBakedModel>) ((a, b) -> (CustomBakedModel) bakedModelFactory.apply(a)));
    }

    public NestedModelLoader(String modelPath, BiFunction<BakedModel, ModelState, CustomBakedModel> bakedModelFactory) {
        this.factory = bakedModelFactory;
        this.path = modelPath;
    }

    @Override
    public CustomGeometry deserialize(JsonObject json, JsonDeserializationContext context) throws JsonParseException {
        JsonElement j = json.get(this.path);
        return (modelBaker, spriteGetter, transform, location) -> {
            BakedModel baked = CustomModelLoader.parseModel(j, modelBaker, spriteGetter, transform, location);
            return (CustomBakedModel) this.factory.apply(baked, transform);
        };
    }

    @Deprecated(forRemoval = true)
    public static BakedModel parseModel(JsonElement j, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, ResourceLocation location) {
        BlockModel model;
        if (j.isJsonPrimitive()) {
            model = (BlockModel) modelBaker.getModel(ResourceLocation.tryParse(j.getAsString()));
        } else {
            model = ClientHelper.parseBlockModel(j);
        }
        model.resolveParents(modelBaker::m_245361_);
        if (model == modelBaker.getModel(ModelBakery.MISSING_MODEL_LOCATION)) {
            throw new JsonParseException("Found missing model while parsing nested model " + location);
        } else {
            return model.bake(modelBaker, model, spriteGetter, transform, location, true);
        }
    }
}