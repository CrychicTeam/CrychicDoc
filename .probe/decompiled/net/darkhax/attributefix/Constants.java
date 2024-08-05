package net.darkhax.attributefix;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Constants {

    public static final String MOD_ID = "attributefix";

    public static final String MOD_NAME = "AttributeFix";

    public static final Logger LOG = LoggerFactory.getLogger("AttributeFix");

    public static final Gson GSON = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().registerTypeAdapter(Double.class, new Constants.DoubleJsonSerializer()).create();

    public static final DecimalFormat FORMAT = new DecimalFormat("#.##");

    private static final class DoubleJsonSerializer implements JsonSerializer<Double> {

        public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
            if (!src.isInfinite() && !src.isNaN()) {
                BigDecimal value = BigDecimal.valueOf(src);
                try {
                    value = new BigDecimal(value.toBigIntegerExact());
                } catch (ArithmeticException var6) {
                }
                return new JsonPrimitive(value);
            } else {
                return new JsonPrimitive(src);
            }
        }
    }
}