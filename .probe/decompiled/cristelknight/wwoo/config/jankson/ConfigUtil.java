package cristelknight.wwoo.config.jankson;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonGrammar.Builder;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.DynamicOps;
import com.mojang.serialization.DataResult.PartialResult;
import cristelknight.wwoo.ExpandedEcosphere;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.Map;
import java.util.Optional;
import java.util.TreeMap;
import java.util.Map.Entry;
import java.util.function.Supplier;

public class ConfigUtil {

    public static final Jankson JANKSON = Jankson.builder().build();

    public static final Supplier<Builder> JSON_GRAMMAR_BUILDER = () -> new Builder().withComments(true).bareSpecialNumerics(true).printCommas(true);

    public static final JsonGrammar JSON_GRAMMAR = ((Builder) JSON_GRAMMAR_BUILDER.get()).build();

    public static final String MODID = "Do Api";

    public static <T> T readConfig(Path path, Codec<T> codec, DynamicOps<JsonElement> ops) {
        try {
            JsonElement load = JANKSON.load(path.toFile());
            DataResult<Pair<T, JsonElement>> decode = codec.decode(ops, load);
            Optional<PartialResult<Pair<T, JsonElement>>> error = decode.error();
            if (error.isPresent()) {
                throw new IllegalArgumentException("[Do Api] Couldn't read " + path + ", crashing instead. Maybe try to delete the config files!");
            } else {
                return (T) ((Pair) decode.result().orElseThrow()).getFirst();
            }
        } catch (Exception var6) {
            throw new IllegalArgumentException("[Do Api] Couldn't read " + path + ", crashing instead. Maybe try to delete the config files!");
        }
    }

    public static <T> void createConfig(Path path, Codec<T> codec, Map<String, String> comments, DynamicOps<JsonElement> ops, T from, boolean sorted, String header) {
        DataResult<JsonElement> dataResult = codec.encodeStart(ops, from);
        Optional<PartialResult<JsonElement>> error = dataResult.error();
        if (error.isPresent()) {
            throw new IllegalArgumentException(String.format("Jankson file creation for \"%s\" failed due to the following error(s):\n%s", path.toString(), ((PartialResult) error.get()).message()));
        } else {
            JsonElement jsonElement = (JsonElement) dataResult.result().orElseThrow();
            if (jsonElement instanceof JsonObject jsonObject) {
                jsonElement = addCommentsAndAlphabeticallySortRecursively(comments, jsonObject, "", sorted);
            }
            try {
                Files.createDirectories(path.getParent());
                if (header != null) {
                    header = header + "\n";
                } else {
                    header = "";
                }
                String output = header + jsonElement.toJson(JSON_GRAMMAR);
                Files.write(path, output.getBytes(), new OpenOption[0]);
            } catch (IOException var11) {
                ExpandedEcosphere.LOGGER.error(var11.toString());
            }
        }
    }

    public static JsonObject addCommentsAndAlphabeticallySortRecursively(Map<String, String> comments, JsonObject object, String parentKey, boolean alphabeticallySorted) {
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            String objectKey = (String) entry.getKey();
            String commentsKey = parentKey + objectKey;
            String comment = object.getComment((String) entry.getKey());
            if (comments.containsKey(commentsKey) && comment == null) {
                String commentToAdd = (String) comments.get(commentsKey);
                object.setComment(objectKey, commentToAdd);
                comment = commentToAdd;
            }
            JsonElement value = (JsonElement) entry.getValue();
            if (value instanceof JsonArray array) {
                JsonArray sortedJsonElements = new JsonArray();
                for (JsonElement element : array) {
                    if (element instanceof JsonObject nestedObject) {
                        sortedJsonElements.add(addCommentsAndAlphabeticallySortRecursively(comments, nestedObject, (String) entry.getKey() + ".", alphabeticallySorted));
                    } else if (element instanceof JsonArray array1) {
                        Array2 arrayOfArrays = new Array2();
                        arrayOfArrays.addAll(array1);
                        sortedJsonElements.add(arrayOfArrays);
                    }
                }
                if (!sortedJsonElements.isEmpty()) {
                    object.put(objectKey, sortedJsonElements, comment);
                }
            }
            if (value instanceof JsonObject nestedObject) {
                object.put(objectKey, addCommentsAndAlphabeticallySortRecursively(comments, nestedObject, (String) entry.getKey() + ".", alphabeticallySorted), comment);
            }
        }
        if (alphabeticallySorted) {
            JsonObject alphabeticallySortedJsonObject = new JsonObject();
            TreeMap<String, JsonElement> map = new TreeMap(String::compareTo);
            map.putAll(object);
            alphabeticallySortedJsonObject.putAll(map);
            alphabeticallySortedJsonObject.forEach((key, entry) -> alphabeticallySortedJsonObject.setComment(key, object.getComment(key)));
            return alphabeticallySortedJsonObject;
        } else {
            return object;
        }
    }
}