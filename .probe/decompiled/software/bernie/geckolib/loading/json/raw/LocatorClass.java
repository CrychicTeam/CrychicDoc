package software.bernie.geckolib.loading.json.raw;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import software.bernie.geckolib.util.JsonUtil;

public record LocatorClass(@Nullable Boolean ignoreInheritedScale, double[] offset, double[] rotation) {

    public static JsonDeserializer<LocatorClass> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();
            Boolean ignoreInheritedScale = JsonUtil.getOptionalBoolean(obj, "ignore_inherited_scale");
            double[] offset = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "offset", null));
            double[] rotation = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "rotation", null));
            return new LocatorClass(ignoreInheritedScale, offset, rotation);
        };
    }
}