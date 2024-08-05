package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import se.mickelus.tetra.blocks.PropertyMatcher;

@ParametersAreNonnullByDefault
public class PropertyMatcherDeserializer implements JsonDeserializer<PropertyMatcher> {

    public PropertyMatcher deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        try {
            return PropertyMatcher.deserialize(json);
        } catch (JsonParseException var5) {
            return null;
        }
    }
}