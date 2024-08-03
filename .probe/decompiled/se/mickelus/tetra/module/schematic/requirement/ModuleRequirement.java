package se.mickelus.tetra.module.schematic.requirement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import se.mickelus.mutil.util.JsonOptional;
import se.mickelus.tetra.module.schematic.CraftingContext;

public class ModuleRequirement implements CraftingRequirement {

    String moduleKey;

    String moduleVariant;

    String materialPattern;

    public ModuleRequirement(String moduleKey, String moduleVariant, String moduleMaterial) {
        this.moduleKey = moduleKey;
        this.moduleVariant = moduleVariant;
        if (moduleMaterial != null) {
            this.materialPattern = "\\/" + moduleMaterial + "(?:_|$)";
        }
    }

    @Override
    public boolean test(CraftingContext context) {
        if (context.targetModule != null) {
            if (this.moduleKey != null && !this.moduleKey.equals(context.targetModule.getKey())) {
                return false;
            } else {
                String currentVariant = context.targetModule.getVariantData(context.targetStack).key;
                return this.moduleVariant != null && !this.moduleVariant.equals(currentVariant) ? false : this.materialPattern == null || currentVariant.matches(this.materialPattern);
            }
        } else {
            return false;
        }
    }

    public static class Deserializer implements JsonDeserializer<CraftingRequirement> {

        public CraftingRequirement deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return new ModuleRequirement((String) JsonOptional.field(json.getAsJsonObject(), "module").map(JsonElement::getAsString).orElse(null), (String) JsonOptional.field(json.getAsJsonObject(), "variant").map(JsonElement::getAsString).orElse(null), (String) JsonOptional.field(json.getAsJsonObject(), "material").map(JsonElement::getAsString).orElse(null));
        }
    }
}