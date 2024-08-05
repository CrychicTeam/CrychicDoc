package cristelknight.wwoo.utils;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import cristelknight.wwoo.config.configs.ReplaceBiomesConfig;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import net.cristellib.CristelLib;
import net.cristellib.CristelLibExpectPlatform;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;

public class BiomeReplace {

    public static void replace() {
        Path path = CristelLibExpectPlatform.getResourceDirectory("expanded_ecosphere", "resources/ee_default/data/minecraft/dimension/overworld.json");
        if (path == null) {
            throw new RuntimeException();
        } else {
            JsonObject object = Util.getObjectFromPath(path);
            replaceObject(object, false);
            addDimensionFile(new ResourceLocation("minecraft", "overworld"), object);
        }
    }

    public static void replaceObject(JsonObject object, boolean skipVanilla) {
        JsonObject generator = (JsonObject) object.get("generator");
        JsonObject biome_source = (JsonObject) generator.get("biome_source");
        JsonArray biomes = (JsonArray) biome_source.get("biomes");
        ReplaceBiomesConfig config = ReplaceBiomesConfig.DEFAULT.getConfig();
        Map<String, String> replaces = config.bannedBiomes();
        if (!replaces.isEmpty()) {
            Map<String, Set<Integer>> strings = new HashMap();
            for (Entry<String, String> replace : replaces.entrySet()) {
                String b = (String) replace.getKey();
                if (!strings.containsKey(b) && !skipIfVanilla(skipVanilla, replace)) {
                    Set<Integer> integers = getAllBiomes(biomes, b);
                    strings.put(b, integers);
                }
            }
            for (Entry<String, String> replacex : replaces.entrySet()) {
                String b = (String) replacex.getKey();
                if (strings.containsKey(b)) {
                    String r = (String) replacex.getValue();
                    replaceBiomes(biomes, r, (Set<Integer>) strings.get(b));
                }
            }
        }
    }

    private static boolean skipIfVanilla(boolean skipVanilla, Entry<String, String> replace) {
        return !skipVanilla ? false : ((String) replace.getKey()).contains("minecraft:") || ((String) replace.getValue()).contains("minecraft:");
    }

    public static byte[] addDimensionFile(ResourceLocation identifier, JsonObject structure) {
        return CristelLib.DATA_PACK.addDataForJsonLocation("dimension", identifier, structure);
    }

    public static Set<Integer> getAllBiomes(JsonArray biomes, String biome) {
        Set<Integer> integers = new HashSet();
        for (int i = 0; i < biomes.size(); i++) {
            String b = GsonHelper.getAsString((JsonObject) biomes.get(i), "biome");
            if (b.equals(biome)) {
                integers.add(i);
            }
        }
        return integers;
    }

    public static void replaceBiomes(JsonArray biomes, String biome, Set<Integer> integers) {
        for (int i : integers) {
            JsonObject object = (JsonObject) biomes.get(i);
            object.addProperty("biome", biome);
        }
    }
}