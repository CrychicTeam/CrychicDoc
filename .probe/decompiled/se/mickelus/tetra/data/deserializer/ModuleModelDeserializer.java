package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import se.mickelus.mutil.data.deserializer.ResourceLocationDeserializer;
import se.mickelus.tetra.items.modular.ItemColors;
import se.mickelus.tetra.module.Priority;
import se.mickelus.tetra.module.data.ModuleModel;

@ParametersAreNonnullByDefault
public class ModuleModelDeserializer implements JsonDeserializer<ModuleModel> {

    public ModuleModel deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ModuleModel data = new ModuleModel();
        if (jsonObject.has("type")) {
            data.type = jsonObject.get("type").getAsString();
        }
        if (jsonObject.has("location")) {
            data.location = ResourceLocationDeserializer.deserialize(jsonObject.get("location"));
        }
        if (jsonObject.has("tint")) {
            data.tint = this.getTint(jsonObject.get("tint").getAsString());
            data.overlayTint = data.tint;
        }
        if (jsonObject.has("overlayTint")) {
            data.overlayTint = this.getTint(jsonObject.get("overlayTint").getAsString());
        }
        if (jsonObject.has("emission")) {
            data.emission = Mth.clamp(0, jsonObject.get("emission").getAsInt(), 15);
        }
        if (jsonObject.has("renderType")) {
            data.renderType = ResourceLocationDeserializer.deserialize(jsonObject.get("renderType"));
        }
        if (jsonObject.has("transform")) {
            data.transform = (Transformation) context.deserialize(jsonObject.getAsJsonObject("transform"), Transformation.class);
        }
        if (jsonObject.has("renderLayer")) {
            data.renderLayer = (Priority) context.deserialize(jsonObject.get("renderLayer"), Priority.class);
        }
        if (jsonObject.has("invertPerspectives")) {
            data.invertPerspectives = (Boolean) context.deserialize(jsonObject.get("invertPerspectives"), Boolean.class);
        }
        if (jsonObject.has("perspectives")) {
            data.contexts = (ItemDisplayContext[]) context.deserialize(jsonObject.get("perspectives"), ItemDisplayContext[].class);
        }
        return data;
    }

    private int getTint(String value) {
        if (ItemColors.exists(value)) {
            return ItemColors.get(value);
        } else {
            try {
                return (int) Long.parseLong(value, 16);
            } catch (NumberFormatException var3) {
                throw new JsonParseException("Could not parse tint '" + value + "' when deserializing module model, unknown color or malformed hexadecimal", var3);
            }
        }
    }
}