package se.mickelus.mutil.util;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.util.Optional;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
public class JsonOptional {

    public static Optional<JsonElement> field(JsonObject object, String field) {
        return object.has(field) ? Optional.of(object.get(field)) : Optional.empty();
    }
}