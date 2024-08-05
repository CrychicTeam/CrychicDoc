package se.mickelus.tetra.module.schematic.requirement;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import java.util.function.Predicate;
import se.mickelus.mutil.util.JsonOptional;

public class IntegerPredicate implements Predicate<Integer> {

    Integer min;

    Integer max;

    public IntegerPredicate(Integer min, Integer max) {
        this.min = min;
        this.max = max;
    }

    public boolean test(Integer value) {
        return this.min != null && this.min > value ? false : this.max == null || this.max >= value;
    }

    public static class Deserializer implements JsonDeserializer<IntegerPredicate> {

        public IntegerPredicate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            return deserialize(json);
        }

        public static IntegerPredicate deserialize(JsonElement json) throws JsonParseException {
            if (json.isJsonObject()) {
                JsonObject jsonObject = json.getAsJsonObject();
                return new IntegerPredicate((Integer) JsonOptional.field(jsonObject, "min").map(JsonElement::getAsInt).orElse(null), (Integer) JsonOptional.field(jsonObject, "max").map(JsonElement::getAsInt).orElse(null));
            } else {
                int value = json.getAsInt();
                return new IntegerPredicate(value, value);
            }
        }
    }
}