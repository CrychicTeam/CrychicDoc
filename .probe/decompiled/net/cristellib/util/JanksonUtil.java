package net.cristellib.util;

import blue.endless.jankson.JsonElement;
import blue.endless.jankson.JsonObject;
import blue.endless.jankson.JsonPrimitive;
import com.mojang.datafixers.util.Pair;
import java.util.List;
import net.cristellib.CristelLib;

public class JanksonUtil {

    public static void addToObject(JsonObject jsonObject, String path, List<Pair<String, String>> toAdd) {
        String[] pathSegments = path.split("/");
        JsonElement currentElement = jsonObject;
        for (String segment : pathSegments) {
            if (!(currentElement instanceof JsonObject object)) {
                CristelLib.LOGGER.error("Invalid path or element type.");
                return;
            }
            currentElement = object.get(segment);
            if (currentElement == null) {
                CristelLib.LOGGER.error("Path segment not found: " + segment);
                return;
            }
        }
        if (currentElement instanceof JsonObject object) {
            for (Pair<String, String> s : toAdd) {
                if (!object.containsKey(s.getFirst())) {
                    object.put((String) s.getFirst(), JsonPrimitive.of((String) s.getSecond()));
                }
            }
        }
    }
}