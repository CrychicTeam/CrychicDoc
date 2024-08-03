package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.mojang.math.Transformation;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import se.mickelus.mutil.util.JsonOptional;

@ParametersAreNonnullByDefault
public class TransformationDeserializer implements JsonDeserializer<Transformation> {

    public Transformation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject object = json.getAsJsonObject();
        try {
            return new Transformation((Vector3f) JsonOptional.field(object, "translation").map(VectorDeserializer::deserialize).orElse(null), (Quaternionf) JsonOptional.field(object, "rotation").map(QuaternionDeserializer::deserialize).orElse(null), (Vector3f) JsonOptional.field(object, "scale").map(VectorDeserializer::deserialize).orElse(null), (Quaternionf) JsonOptional.field(object, "rotation").map(QuaternionDeserializer::deserialize).orElse(null));
        } catch (JsonParseException var6) {
            throw new JsonParseException("Tried to parse faulty Transformation: " + json, var6);
        }
    }
}