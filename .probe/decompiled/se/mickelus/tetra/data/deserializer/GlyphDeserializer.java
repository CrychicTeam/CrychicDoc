package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.mutil.data.deserializer.ResourceLocationDeserializer;
import se.mickelus.tetra.items.modular.ItemColors;
import se.mickelus.tetra.module.data.GlyphData;

@ParametersAreNonnullByDefault
public class GlyphDeserializer implements JsonDeserializer<GlyphData> {

    public GlyphData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        GlyphData data = new GlyphData();
        if (jsonObject.has("textureLocation")) {
            data.textureLocation = ResourceLocationDeserializer.deserialize(jsonObject.get("textureLocation"));
        }
        if (jsonObject.has("textureX")) {
            data.textureX = jsonObject.get("textureX").getAsInt();
        }
        if (jsonObject.has("textureY")) {
            data.textureY = jsonObject.get("textureY").getAsInt();
        }
        if (jsonObject.has("tint")) {
            String tint = jsonObject.get("tint").getAsString();
            if (ItemColors.exists(tint)) {
                data.tint = ItemColors.get(tint);
            } else {
                data.tint = (int) Long.parseLong(tint, 16);
            }
        }
        return data;
    }
}