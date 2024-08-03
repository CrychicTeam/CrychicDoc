package se.mickelus.tetra.module.data;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class ImprovementData extends VariantData {

    public int level = 0;

    public boolean enchantment = false;

    public String group = null;

    public int getLevel() {
        return this.level;
    }

    public static class Deserializer implements JsonDeserializer<ImprovementData> {

        public ImprovementData deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return jsonObject.has("materials") ? (ImprovementData) context.deserialize(json, MaterialImprovementData.class) : (ImprovementData) context.deserialize(json, UniqueImprovementData.class);
        }
    }
}