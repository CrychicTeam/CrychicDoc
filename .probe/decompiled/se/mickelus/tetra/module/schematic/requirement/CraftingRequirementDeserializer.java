package se.mickelus.tetra.module.schematic.requirement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import se.mickelus.tetra.data.DataManager;

public class CraftingRequirementDeserializer implements JsonDeserializer<CraftingRequirement> {

    private static final Map<String, Function<JsonObject, CraftingRequirement>> requirements = new HashMap();

    public static void registerSupplier(String identifier, Class<? extends CraftingRequirement> clazz) {
        requirements.put(identifier, (Function) json -> (CraftingRequirement) DataManager.gson.fromJson(json, clazz));
    }

    public static void registerSupplier(String identifier, Function<JsonObject, CraftingRequirement> supplier) {
        requirements.put(identifier, supplier);
    }

    public static CraftingRequirement get(JsonObject jsonObject) throws JsonParseException {
        String type = jsonObject.get("type").getAsString();
        return (CraftingRequirement) Optional.ofNullable(type).map(requirements::get).map(getter -> (CraftingRequirement) getter.apply(jsonObject)).orElseThrow(() -> new JsonParseException("Crafting effect outcome type \"" + type + "\" is not valid"));
    }

    public CraftingRequirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return get(json.getAsJsonObject());
    }
}