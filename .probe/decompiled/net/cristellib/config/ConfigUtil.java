package net.cristellib.config;

import blue.endless.jankson.Jankson;
import blue.endless.jankson.JsonArray;
import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonGrammar;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import blue.endless.jankson.JsonGrammar.Builder;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.function.Supplier;
import net.cristellib.CristelLib;
import net.cristellib.CristelLibExpectPlatform;
import net.cristellib.StructureConfig;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

public class ConfigUtil {

    public static final Path CONFIG_DIR = CristelLibExpectPlatform.getConfigDirectory();

    public static final Path CONFIG_LIB = CONFIG_DIR.resolve("cristellib");

    public static final Jankson JANKSON = Jankson.builder().build();

    public static final Supplier<Builder> JSON_GRAMMAR_BUILDER = () -> new Builder().withComments(true).bareSpecialNumerics(true).printCommas(true);

    public static final JsonGrammar JSON_GRAMMAR = ((Builder) JSON_GRAMMAR_BUILDER.get()).build();

    public static void createConfig(StructureConfig config) {
        Path path = config.getPath();
        if (!path.toFile().exists()) {
            Map<ResourceLocation, List<String>> sets = config.getStructures();
            JsonObject object = new JsonObject();
            JsonElement jsonBoolean = JsonPrimitive.of(true);
            for (ResourceLocation location : sets.keySet()) {
                JsonObject inObjectObject = new JsonObject();
                Map<String, JsonObject> l = new HashMap();
                for (String s : (List) sets.get(location)) {
                    String sWN = s.split(":")[1];
                    if (sWN.contains("/")) {
                        String key = sWN.split("/")[0];
                        String value = sWN.split("/")[1];
                        if (!l.containsKey(key)) {
                            JsonObject jsonObject = new JsonObject();
                            jsonObject.put(value, jsonBoolean);
                            l.put(key, jsonObject);
                        } else {
                            ((JsonObject) l.get(key)).put(value, jsonBoolean);
                        }
                    } else {
                        inObjectObject.put(sWN, jsonBoolean);
                    }
                }
                for (String sx : l.keySet()) {
                    inObjectObject.put(sx, (JsonElement) l.get(sx));
                }
                object.put(location.toString().split(":")[1], inObjectObject);
            }
            addComments(config.getComments(), object, "");
            try {
                Files.createDirectories(path.getParent());
                String output = config.getHeader() + "\n" + object.toJson(JSON_GRAMMAR);
                Files.write(path, output.getBytes(), new OpenOption[0]);
            } catch (IOException var15) {
                CristelLib.LOGGER.error("Couldn't create Enable Disable Config for Path {}", path, var15);
            }
        }
    }

    public static void createPlacementConfig(StructureConfig config) {
        Path path = config.getPath();
        if (!path.toFile().exists()) {
            Map<ResourceLocation, Placement> sets = config.getStructurePlacement();
            JsonObject object = new JsonObject();
            for (ResourceLocation location : sets.keySet()) {
                JsonElement element = JANKSON.toJson((Placement) sets.get(location));
                if (element instanceof JsonObject object1 && object1.getDouble("frequency", 0.0) == 0.0) {
                    object1.remove("frequency");
                }
                object.put(location.toString().split(":")[1], element);
            }
            addComments(config.getComments(), object, "");
            try {
                Files.createDirectories(path.getParent());
                String output = config.getHeader() + "\n" + object.toJson(JSON_GRAMMAR);
                Files.write(path, output.getBytes(), new OpenOption[0]);
            } catch (IOException var8) {
                CristelLib.LOGGER.error("Couldn't create Placement Config for Path {}", path, var8);
            }
        }
    }

    public static Map<String, Placement> readPlacementConfig(Path path) {
        try {
            JsonObject load = JANKSON.load(path.toFile());
            return stringPlacementMap(load);
        } catch (Exception var2) {
            throw new IllegalArgumentException("Couldn't read " + path + ", crashing instead. Maybe try to delete the config files!");
        }
    }

    public static Map<String, Placement> stringPlacementMap(JsonObject object) {
        Map<String, Placement> map = new HashMap();
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            if (entry.getValue() instanceof JsonObject jsonObject) {
                map.put((String) entry.getKey(), (Placement) JANKSON.fromJson(jsonObject, Placement.class));
            }
        }
        return map;
    }

    public static Map<String, Boolean> readConfig(Path path) {
        try {
            JsonObject load = JANKSON.load(path.toFile());
            return newStringBooleanMap(load);
        } catch (Exception var2) {
            throw new IllegalArgumentException("Couldn't read " + path + ", crashing instead. Maybe try to delete the config files!");
        }
    }

    public static Map<String, Boolean> stringBooleanMap(JsonObject object, String parent) {
        Map<String, Boolean> map = new HashMap();
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            String key = parent.equals("") ? (String) entry.getKey() : parent + "/" + (String) entry.getKey();
            JsonElement e = (JsonElement) entry.getValue();
            if (e instanceof JsonPrimitive primitive && primitive.getValue() instanceof Boolean bool) {
                map.put(key, bool);
                continue;
            }
            if (e instanceof JsonObject jsonObject) {
                map.putAll(stringBooleanMap(jsonObject, (String) entry.getKey()));
            }
        }
        return map;
    }

    public static Map<String, Boolean> newStringBooleanMap(JsonObject object) {
        Map<String, Boolean> map = new HashMap();
        for (Entry<String, JsonElement> entry : object.entrySet()) {
            JsonElement e = (JsonElement) entry.getValue();
            if (e instanceof JsonObject jsonObject) {
                map.putAll(stringBooleanMap(jsonObject, ""));
            }
        }
        return map;
    }

    @Nullable
    public static com.google.gson.JsonElement getSetElement(String getDataFromModId, ResourceLocation location) {
        return getElement(getDataFromModId, "data/" + location.getNamespace() + "/worldgen/structure_set/" + location.getPath() + ".json");
    }

    @Nullable
    public static com.google.gson.JsonElement getElement(String getDataFromModId, String location) {
        Path pathC = CristelLibExpectPlatform.getResourceDirectory(getDataFromModId, location);
        if (pathC == null) {
            return null;
        } else {
            InputStream im;
            try {
                im = Files.newInputStream(pathC);
            } catch (IOException var10) {
                CristelLib.LOGGER.warn("Couldn't create Input Stream for Path " + pathC, var10);
                return null;
            }
            try {
                InputStreamReader reader = new InputStreamReader(im);
                com.google.gson.JsonElement var5;
                try {
                    var5 = JsonParser.parseReader(reader);
                } catch (Throwable var8) {
                    try {
                        reader.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                    throw var8;
                }
                reader.close();
                return var5;
            } catch (IOException var9) {
                CristelLib.LOGGER.warn("Couldn't read " + location + " from mod: " + getDataFromModId, var9);
                return null;
            }
        }
    }

    public static JsonObject addComments(Map<String, String> comments, JsonObject object, String parentKey) {
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
                        sortedJsonElements.add(addComments(comments, nestedObject, (String) entry.getKey() + "."));
                    } else if (element instanceof JsonArray array1) {
                        JsonArray arrayOfArrays = new JsonArray();
                        arrayOfArrays.addAll(array1);
                        sortedJsonElements.add(arrayOfArrays);
                    }
                }
                if (!sortedJsonElements.isEmpty()) {
                    object.put(objectKey, sortedJsonElements, comment);
                }
            }
            if (value instanceof JsonObject nestedObject) {
                object.put(objectKey, addComments(comments, nestedObject, (String) entry.getKey() + "."), comment);
            }
        }
        return object;
    }
}