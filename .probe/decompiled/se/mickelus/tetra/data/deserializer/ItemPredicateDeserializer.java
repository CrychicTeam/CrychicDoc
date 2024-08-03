package se.mickelus.tetra.data.deserializer;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import java.lang.reflect.Type;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.advancements.critereon.ItemPredicate;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@ParametersAreNonnullByDefault
public class ItemPredicateDeserializer implements JsonDeserializer<ItemPredicate> {

    private static final Logger logger = LogManager.getLogger();

    public static ItemPredicate deserialize(JsonElement json) {
        try {
            return ItemPredicate.fromJson(json);
        } catch (JsonParseException var2) {
            logger.debug("Failed to parse item predicate from \"{}\": '{}'", json, var2.getMessage());
            return null;
        }
    }

    public ItemPredicate deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return deserialize(json);
    }
}