package fuzs.puzzleslib.api.config.v3.json;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.util.GsonHelper;

public class GsonEnumHelper {

    public static <T extends Enum<T>> T convertToEnum(String enumName, Class<T> clazz) {
        try {
            return (T) Enum.valueOf(clazz, enumName);
        } catch (IllegalArgumentException var3) {
            throw new JsonSyntaxException("Unable to deserialize enum value" + enumName + "of type " + clazz, var3);
        }
    }

    public static <T extends Enum<T>> T getAsEnum(JsonObject jsonObject, String key, Class<T> clazz) {
        if (jsonObject.has(key)) {
            return convertToEnum(GsonHelper.getAsString(jsonObject, key), clazz);
        } else {
            throw new JsonSyntaxException("Missing " + key + ", expected to find a " + clazz);
        }
    }

    public static <T extends Enum<T>> T getAsEnum(JsonObject jsonObject, String key, Class<T> clazz, T fallback) {
        return jsonObject.has(key) ? convertToEnum(GsonHelper.getAsString(jsonObject, key), clazz) : fallback;
    }
}