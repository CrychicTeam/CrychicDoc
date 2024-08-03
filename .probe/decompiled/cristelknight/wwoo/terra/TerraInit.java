package cristelknight.wwoo.terra;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.JsonOps;
import cristelknight.wwoo.EERL;
import cristelknight.wwoo.ExpandedEcosphere;
import cristelknight.wwoo.config.configs.ReplaceBiomesConfig;
import cristelknight.wwoo.utils.BiomeReplace;
import cristelknight.wwoo.utils.Util;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import net.cristellib.CristelLibExpectPlatform;
import net.cristellib.util.TerrablenderUtil;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.levelgen.SurfaceRules;
import terrablender.api.RegionType;
import terrablender.api.Regions;
import terrablender.api.SurfaceRuleManager;
import terrablender.api.SurfaceRuleManager.RuleCategory;

public class TerraInit {

    private static final String OVERWORLD = "resources/ee_default/data/minecraft/dimension/overworld.json";

    private static final String NOISE = "resources/ee_default/data/minecraft/worldgen/noise_settings/overworld.json";

    public static List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> readParameterPoints() {
        InputStream im;
        try {
            Path path = CristelLibExpectPlatform.getResourceDirectory("expanded_ecosphere", "resources/ee_default/data/minecraft/dimension/overworld.json");
            if (path == null) {
                throw new RuntimeException();
            }
            im = Files.newInputStream(path);
        } catch (IOException var16) {
            ExpandedEcosphere.LOGGER.error("Couldn't read resources/ee_default/data/minecraft/dimension/overworld.json, crashing instead");
            throw new RuntimeException(var16);
        }
        try {
            InputStreamReader reader = new InputStreamReader(im);
            Object var18;
            try {
                JsonElement el = JsonParser.parseReader(reader);
                if (!el.isJsonObject()) {
                    throw new RuntimeException("Input stream is on JsonElement");
                }
                List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>> list = new ArrayList();
                JsonObject o = el.getAsJsonObject();
                if (ReplaceBiomesConfig.DEFAULT.getConfig().enableBiomes()) {
                    BiomeReplace.replaceObject(o, true);
                }
                JsonArray jsonArray = o.get("generator").getAsJsonObject().get("biome_source").getAsJsonObject().get("biomes").getAsJsonArray();
                for (int i = 0; i < jsonArray.size(); i++) {
                    JsonObject e = jsonArray.get(i).getAsJsonObject();
                    String b = e.get("biome").getAsString();
                    if (!b.contains("minecraft:")) {
                        JsonObject jo = e.get("parameters").getAsJsonObject();
                        Climate.ParameterPoint point = Util.readConfig(jo, Climate.ParameterPoint.CODEC, JsonOps.INSTANCE);
                        Pair<Climate.ParameterPoint, ResourceKey<Biome>> pair = new Pair(point, ResourceKey.create(Registries.BIOME, new ResourceLocation(b)));
                        list.add(pair);
                    }
                }
                var18 = list;
            } catch (Throwable var13) {
                try {
                    reader.close();
                } catch (Throwable var12) {
                    var13.addSuppressed(var12);
                }
                throw var13;
            }
            reader.close();
            return (List<Pair<Climate.ParameterPoint, ResourceKey<Biome>>>) var18;
        } catch (FileNotFoundException var14) {
            ExpandedEcosphere.LOGGER.error("Couldn't find resources/ee_default/data/minecraft/dimension/overworld.json, crashing instead");
            throw new RuntimeException(var14);
        } catch (JsonSyntaxException | IOException var15) {
            ExpandedEcosphere.LOGGER.error("Couldn't parse resources/ee_default/data/minecraft/dimension/overworld.json, crashing instead");
            throw new RuntimeException(var15);
        }
    }

    public static SurfaceRules.RuleSource readSurfaceRulesFromNoise() {
        InputStream im;
        try {
            Path path = CristelLibExpectPlatform.getResourceDirectory("expanded_ecosphere", "resources/ee_default/data/minecraft/worldgen/noise_settings/overworld.json");
            if (path == null) {
                throw new RuntimeException();
            }
            im = Files.newInputStream(path);
        } catch (IOException var8) {
            ExpandedEcosphere.LOGGER.error("Couldn't read resources/ee_default/data/minecraft/worldgen/noise_settings/overworld.json, crashing instead");
            throw new RuntimeException(var8);
        }
        try {
            InputStreamReader reader = new InputStreamReader(im);
            SurfaceRules.RuleSource var4;
            try {
                JsonElement load = JsonParser.parseReader(reader);
                JsonElement element = load.getAsJsonObject().get("surface_rule");
                var4 = Util.readConfig(element, SurfaceRules.RuleSource.CODEC, JsonOps.INSTANCE);
            } catch (Throwable var6) {
                try {
                    reader.close();
                } catch (Throwable var5) {
                    var6.addSuppressed(var5);
                }
                throw var6;
            }
            reader.close();
            return var4;
        } catch (Exception var7) {
            throw new IllegalArgumentException("Couldn't parse resources/ee_default/data/minecraft/worldgen/noise_settings/overworld.json, crashing instead.");
        }
    }

    public static void terraEnableDisable() {
        if (ExpandedEcosphere.currentMode.equals(ExpandedEcosphere.Mode.COMPATIBLE)) {
            terraEnable();
        } else {
            TerrablenderUtil.setMixinEnabled(false);
            Regions.remove(RegionType.OVERWORLD, new EERL("overworld"));
            SurfaceRuleManager.removeSurfaceRules(RuleCategory.OVERWORLD, "wythers");
        }
    }

    public static void terraEnable() {
        TerrablenderUtil.setMixinEnabled(true);
        registerRegions();
        readOverworldSurfaceRules();
    }

    private static void registerRegions() {
        Regions.register(new WWOORegion(new EERL("overworld"), 10));
    }

    private static void readOverworldSurfaceRules() {
        SurfaceRules.RuleSource s = readSurfaceRulesFromNoise();
        SurfaceRuleManager.addSurfaceRules(RuleCategory.OVERWORLD, "wythers", s);
    }
}