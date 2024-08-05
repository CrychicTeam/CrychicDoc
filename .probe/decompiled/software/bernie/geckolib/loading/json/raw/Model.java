package software.bernie.geckolib.loading.json.raw;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import javax.annotation.Nullable;
import net.minecraft.util.GsonHelper;
import software.bernie.geckolib.loading.json.FormatVersion;
import software.bernie.geckolib.util.JsonUtil;

public record Model(@Nullable FormatVersion formatVersion, MinecraftGeometry[] minecraftGeometry) {

    public static JsonDeserializer<Model> deserializer() throws JsonParseException {
        return (json, type, context) -> {
            JsonObject obj = json.getAsJsonObject();
            FormatVersion formatVersion = (FormatVersion) context.deserialize(obj.get("format_version"), FormatVersion.class);
            MinecraftGeometry[] minecraftGeometry = JsonUtil.jsonArrayToObjectArray(GsonHelper.getAsJsonArray(obj, "minecraft:geometry", new JsonArray(0)), context, MinecraftGeometry.class);
            return new Model(formatVersion, minecraftGeometry);
        };
    }
}