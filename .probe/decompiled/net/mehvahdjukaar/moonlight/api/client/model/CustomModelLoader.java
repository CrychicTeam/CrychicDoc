package net.mehvahdjukaar.moonlight.api.client.model;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
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

public interface CustomModelLoader {

    CustomGeometry deserialize(JsonObject var1, JsonDeserializationContext var2) throws JsonParseException;

    static BakedModel parseModel(JsonElement j, ModelBaker modelBaker, Function<Material, TextureAtlasSprite> spriteGetter, ModelState transform, ResourceLocation location) {
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