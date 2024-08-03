package net.mehvahdjukaar.supplementaries.client.block_models;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import net.mehvahdjukaar.moonlight.api.client.model.CustomGeometry;
import net.mehvahdjukaar.moonlight.api.client.model.CustomModelLoader;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.util.GsonHelper;

public class JarModelLoader implements CustomModelLoader {

    @Override
    public CustomGeometry deserialize(JsonObject json, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonElement model = json.get("model");
        float width = GsonHelper.getAsFloat(json, "liquid_width") / 16.0F;
        float height = GsonHelper.getAsFloat(json, "liquid_max_height") / 16.0F;
        float y0 = GsonHelper.getAsFloat(json, "liquid_y_offset") / 16.0F;
        return (modelBaker, spriteGetter, transform, location) -> {
            BakedModel g = CustomModelLoader.parseModel(model, modelBaker, spriteGetter, transform, location);
            return new JarBakedModel(g, width, height, y0, transform);
        };
    }
}