package se.mickelus.tetra.module.schematic;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.module.data.ToolData;

@ParametersAreNonnullByDefault
public class OutcomeDefinition {

    public OutcomeMaterial material = new OutcomeMaterial();

    public boolean hidden = false;

    public int materialSlot = 0;

    public int experienceCost = 0;

    public ToolData requiredTools = new ToolData();

    public String moduleKey;

    public String moduleVariant;

    public Map<String, Integer> improvements = new HashMap();

    public static class Deserializer implements JsonDeserializer<OutcomeDefinition> {

        public OutcomeDefinition deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            return jsonObject.has("materials") ? (OutcomeDefinition) context.deserialize(json, MaterialOutcomeDefinition.class) : (OutcomeDefinition) context.deserialize(json, UniqueOutcomeDefinition.class);
        }
    }
}