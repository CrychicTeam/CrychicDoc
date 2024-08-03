package se.mickelus.tetra.data.deserializer;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.reflect.TypeToken;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.registries.ForgeRegistries;

@ParametersAreNonnullByDefault
public class AttributesDeserializer implements JsonDeserializer<Multimap<Attribute, AttributeModifier>> {

    public static final TypeToken<Multimap<Attribute, AttributeModifier>> typeToken = new TypeToken<Multimap<Attribute, AttributeModifier>>() {
    };

    private static AttributeModifier.Operation getOperation(String key) {
        if (key.startsWith("**")) {
            return AttributeModifier.Operation.MULTIPLY_TOTAL;
        } else {
            return key.startsWith("*") ? AttributeModifier.Operation.MULTIPLY_BASE : AttributeModifier.Operation.ADDITION;
        }
    }

    private static Attribute getAttribute(String key) {
        ResourceLocation rl = new ResourceLocation(key.replace("*", ""));
        return ForgeRegistries.ATTRIBUTES.getValue(rl);
    }

    public Multimap<Attribute, AttributeModifier> deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ArrayListMultimap<Attribute, AttributeModifier> result = ArrayListMultimap.create();
        jsonObject.entrySet().forEach(entry -> {
            Attribute attribute = getAttribute((String) entry.getKey());
            if (attribute != null) {
                result.put(attribute, new AttributeModifier("module_data", ((JsonElement) entry.getValue()).getAsDouble(), getOperation((String) entry.getKey())));
            }
        });
        return result;
    }
}