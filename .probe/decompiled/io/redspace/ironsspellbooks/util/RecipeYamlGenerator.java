package io.redspace.ironsspellbooks.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;

public class RecipeYamlGenerator {

    private static final String RECIPE_DATA_TEMPLATE = "- name: \"%s\"\n  path: \"%s\"\n  item0: \"%s\"\n  item0Path: \"%s\"\n  item1: \"%s\"\n  item1Path: \"%s\"\n  item2: \"%s\"\n  item2Path: \"%s\"\n  item3: \"%s\"\n  item3Path: \"%s\"\n  item4: \"%s\"\n  item4Path: \"%s\"\n  item5: \"%s\"\n  item5Path: \"%s\"\n  item6: \"%s\"\n  item6Path: \"%s\"\n  item7: \"%s\"\n  item7Path: \"%s\"\n  item8: \"%s\"\n  item8Path: \"%s\"\n  item9: \"%s\"\n  item9Path: \"%s\"\n\n";

    public static void main(String[] args) {
        String baseDir = System.getProperty("user.dir");
        String recipesDir = "src/main/resources/data/irons_spellbooks/recipes/";
        new StringBuilder();
        new Gson();
        try {
            Files.walk(Path.of(baseDir, recipesDir)).forEach(path -> {
                System.out.println(path);
                if (path.toFile().isFile()) {
                    try {
                        JsonObject jsonObject = JsonParser.parseReader(new FileReader(path.toFile())).getAsJsonObject();
                        String type = jsonObject.get("type").getAsString();
                        if (type.equals("minecraft:crafting_shaped")) {
                            String result = jsonObject.get("result").getAsJsonObject().get("item").getAsString();
                            System.out.println(result);
                            RecipeYamlGenerator.ItemInfo[] itemInfoArray = new RecipeYamlGenerator.ItemInfo[9];
                            JsonArray pattern = jsonObject.get("pattern").getAsJsonArray();
                            Map<String, JsonElement> keyMap = jsonObject.get("key").getAsJsonObject().asMap();
                            int count = 0;
                            for (int i = 0; i < pattern.size(); i++) {
                                String slots = pattern.get(i).getAsString();
                                for (int j = 0; j < slots.length(); j++) {
                                    char key = slots.charAt(j);
                                    if (key != ' ') {
                                        JsonElement item = ((JsonElement) keyMap.get(String.valueOf(key))).getAsJsonObject().get("item");
                                        if (item == null) {
                                            item = ((JsonElement) keyMap.get(String.valueOf(key))).getAsJsonObject().get("tag");
                                        }
                                        itemInfoArray[count] = new RecipeYamlGenerator.ItemInfo(item.getAsString(), item.getAsString());
                                    }
                                    count++;
                                }
                            }
                            for (int i = 0; i < itemInfoArray.length; i++) {
                                System.out.println(String.format("%d: %s", i, itemInfoArray[i]));
                            }
                        }
                    } catch (Exception var13) {
                        System.out.println(var13.getMessage());
                        Arrays.stream(var13.getStackTrace()).forEach(System.out::println);
                    }
                }
            });
        } catch (IOException var6) {
            throw new RuntimeException(var6);
        }
    }

    private static record ItemInfo(String name, String path) {
    }
}