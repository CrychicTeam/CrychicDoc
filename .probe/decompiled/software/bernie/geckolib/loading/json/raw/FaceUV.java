package software.bernie.geckolib.loading.json.raw;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import software.bernie.geckolib.util.JsonUtil;

public record FaceUV(@Nullable String materialInstance, double[] uv, double[] uvSize) {

    public static JsonDeserializer<FaceUV> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();
            String materialInstance = GsonHelper.getAsString(obj, "material_instance", null);
            double[] uv = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "uv", null));
            double[] uvSize = JsonUtil.jsonArrayToDoubleArray(GsonHelper.getAsJsonArray(obj, "uv_size", null));
            return new FaceUV(materialInstance, uv, uvSize);
        };
    }
}