package fuzs.puzzleslib.api.config.v3.json;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.FileReader;
import java.util.Optional;
import java.util.stream.Stream;

public class JsonSerializationUtil {

    public static final String FILE_FORMAT_STRING = "__file_format";

    public static final String COMMENT_STRING = "__comment";

    public static JsonObject getConfigBase(String... comments) {
        return getConfigBase(-1, comments);
    }

    public static JsonObject getConfigBase(int fileFormat, String... comments) {
        JsonObject jsonobject = new JsonObject();
        if (fileFormat != -1) {
            jsonobject.addProperty("__file_format", fileFormat);
        }
        addConfigComment(jsonobject, comments);
        return jsonobject;
    }

    private static void addConfigComment(JsonObject jsonobject, String... comments) {
        if (comments.length == 1) {
            jsonobject.addProperty("__comment", comments[0]);
        } else if (comments.length > 1) {
            JsonArray jsonarray = new JsonArray();
            Stream.of(comments).forEach(jsonarray::add);
            jsonobject.add("__comment", jsonarray);
        }
    }

    public static int readFileFormat(JsonObject jsonObject) {
        return (Integer) Optional.ofNullable(jsonObject.get("__file_format")).map(JsonElement::getAsInt).orElse(-1);
    }

    public static JsonObject readJsonObject(FileReader reader) {
        JsonElement jsonElement = (JsonElement) JsonConfigFileUtil.GSON.fromJson(reader, JsonElement.class);
        if (!jsonElement.isJsonObject()) {
            throw new IllegalArgumentException("unable to get json object from file reader");
        } else {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            jsonObject.remove("__file_format");
            jsonObject.remove("__comment");
            return jsonObject;
        }
    }
}