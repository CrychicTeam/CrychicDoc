package org.embeddedt.modernfix.core.config;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class OptionCategories {

    private static String defaultCategory = "default";

    private static final Map<String, String> categoryByName = new HashMap();

    private static final List<String> categoryOrder = new ArrayList();

    public static void load() {
        try {
            InputStream stream = OptionCategories.class.getResourceAsStream("/modernfix/option_categories.json");
            try {
                if (stream == null) {
                    throw new FileNotFoundException("option_categories.json");
                }
                JsonObject object = new JsonParser().parse(new JsonReader(new InputStreamReader(stream, StandardCharsets.UTF_8))).getAsJsonObject();
                defaultCategory = object.get("default").getAsString();
                JsonObject obj = object.get("categories").getAsJsonObject();
                for (Entry<String, JsonElement> category : obj.entrySet()) {
                    categoryOrder.add((String) category.getKey());
                    for (JsonElement e : ((JsonElement) category.getValue()).getAsJsonArray()) {
                        categoryByName.put(e.getAsString(), (String) category.getKey());
                    }
                }
            } catch (Throwable var8) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (Throwable var7) {
                        var8.addSuppressed(var7);
                    }
                }
                throw var8;
            }
            if (stream != null) {
                stream.close();
            }
        } catch (RuntimeException | IOException var9) {
            var9.printStackTrace();
            categoryOrder.clear();
            categoryByName.clear();
            categoryOrder.add("default");
        }
    }

    public static List<String> getCategoriesInOrder() {
        return Collections.unmodifiableList(categoryOrder);
    }

    public static String getCategoryForOption(String optionName) {
        String category = (String) categoryByName.get(optionName);
        if (category == null) {
            int lastDotIdx = optionName.lastIndexOf(46);
            if (lastDotIdx > 0) {
                category = getCategoryForOption(optionName.substring(0, lastDotIdx - 1));
            } else {
                category = defaultCategory;
            }
        }
        return category;
    }
}