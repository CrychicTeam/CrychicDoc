package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import org.joml.Quaternionf;

@ParametersAreNonnullByDefault
public class QuaternionDeserializer implements JsonDeserializer<Quaternionf> {

    public static Quaternionf deserialize(JsonElement json) throws JsonParseException {
        JsonArray array = json.getAsJsonArray();
        if (array.size() == 3) {
            return new Quaternionf().setAngleAxis(0.0F, array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat());
        } else if (array.size() == 4) {
            return new Quaternionf(array.get(0).getAsFloat(), array.get(1).getAsFloat(), array.get(2).getAsFloat(), array.get(3).getAsFloat());
        } else {
            throw new JsonParseException("Tried to parse faulty Quaternion: " + json);
        }
    }

    public Quaternionf deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return deserialize(json);
    }
}