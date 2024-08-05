package com.github.alexmodguy.alexscaves.server.config;

import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRarity;
import com.github.alexmodguy.alexscaves.server.level.biome.ACBiomeRegistry;
import com.github.alexmodguy.alexscaves.server.misc.VoronoiGenerator;
import com.github.alexthe666.citadel.Citadel;
import com.github.alexthe666.citadel.server.event.EventReplaceBiome;
import com.google.common.reflect.TypeToken;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.function.Predicate;
import javax.annotation.Nullable;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.biome.Biome;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

public class BiomeGenerationConfig {

    public static final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).setPrettyPrinting().create();

    private static final String OVERWORLD = "minecraft:overworld";

    private static final BiomeGenerationNoiseCondition MAGNETIC_CAVES_CONDITION = new BiomeGenerationNoiseCondition.Builder().dimensions("minecraft:overworld").distanceFromSpawn(400).alexscavesRarityOffset(0).continentalness(0.6F, 1.0F).depth(0.2F, 1.0F).build();

    private static final BiomeGenerationNoiseCondition PRIMORDIAL_CAVES_CONDITION = new BiomeGenerationNoiseCondition.Builder().dimensions("minecraft:overworld").distanceFromSpawn(450).alexscavesRarityOffset(1).continentalness(0.4F, 1.0F).depth(0.15F, 1.5F).build();

    private static final BiomeGenerationNoiseCondition TOXIC_CAVES_CONDITION = new BiomeGenerationNoiseCondition.Builder().dimensions("minecraft:overworld").distanceFromSpawn(650).alexscavesRarityOffset(2).continentalness(0.5F, 1.0F).depth(0.3F, 1.5F).build();

    private static final BiomeGenerationNoiseCondition ABYSSAL_CHASM_CONDITION = new BiomeGenerationNoiseCondition.Builder().dimensions("minecraft:overworld").distanceFromSpawn(400).alexscavesRarityOffset(3).continentalness(-0.95F, -0.65F).temperature(-1.0F, 0.5F).depth(0.2F, 1.5F).build();

    private static final BiomeGenerationNoiseCondition FORLORN_HOLLOWS_CONDITION = new BiomeGenerationNoiseCondition.Builder().dimensions("minecraft:overworld").distanceFromSpawn(650).alexscavesRarityOffset(4).continentalness(0.6F, 1.0F).depth(0.3F, 1.5F).build();

    private static LinkedHashMap<ResourceKey<Biome>, BiomeGenerationNoiseCondition> biomes = new LinkedHashMap();

    public static void reloadConfig() {
        biomes.put(ACBiomeRegistry.MAGNETIC_CAVES, getConfigData("magnetic_caves", MAGNETIC_CAVES_CONDITION));
        biomes.put(ACBiomeRegistry.PRIMORDIAL_CAVES, getConfigData("primordial_caves", PRIMORDIAL_CAVES_CONDITION));
        biomes.put(ACBiomeRegistry.TOXIC_CAVES, getConfigData("toxic_caves", TOXIC_CAVES_CONDITION));
        biomes.put(ACBiomeRegistry.ABYSSAL_CHASM, getConfigData("abyssal_chasm", ABYSSAL_CHASM_CONDITION));
        biomes.put(ACBiomeRegistry.FORLORN_HOLLOWS, getConfigData("forlorn_hollows", FORLORN_HOLLOWS_CONDITION));
    }

    @Nullable
    public static ResourceKey<Biome> getBiomeForEvent(EventReplaceBiome event) {
        VoronoiGenerator.VoronoiInfo voronoiInfo = ACBiomeRarity.getRareBiomeInfoForQuad(event.getWorldSeed(), event.getX(), event.getZ());
        if (voronoiInfo != null) {
            int foundRarityOffset = ACBiomeRarity.getRareBiomeOffsetId(voronoiInfo);
            for (Entry<ResourceKey<Biome>, BiomeGenerationNoiseCondition> condition : biomes.entrySet()) {
                if (foundRarityOffset == ((BiomeGenerationNoiseCondition) condition.getValue()).getRarityOffset() && ((BiomeGenerationNoiseCondition) condition.getValue()).test(event, voronoiInfo)) {
                    return (ResourceKey<Biome>) condition.getKey();
                }
            }
        }
        return null;
    }

    public static int getBiomeCount() {
        return biomes.size();
    }

    public static boolean isBiomeDisabledCompletely(ResourceKey<Biome> biome) {
        BiomeGenerationNoiseCondition noiseCondition = (BiomeGenerationNoiseCondition) biomes.get(biome);
        return noiseCondition != null && noiseCondition.isDisabledCompletely();
    }

    private static <T> T getOrCreateConfigFile(File configDir, String configName, T defaults, Type type, Predicate<T> isInvalid) {
        File configFile = new File(configDir, configName + ".json");
        if (!configFile.exists()) {
            try {
                FileUtils.write(configFile, GSON.toJson(defaults));
            } catch (IOException var10) {
                Citadel.LOGGER.error("Biome Generation Config: Could not write " + configFile, var10);
            }
        }
        try {
            T found = (T) GSON.fromJson(FileUtils.readFileToString(configFile), type);
            if (!isInvalid.test(found)) {
                return found;
            }
            Citadel.LOGGER.warn("Old Biome Generation Config format found for " + configName + ", replacing with new one.");
            try {
                FileUtils.write(configFile, GSON.toJson(defaults));
            } catch (IOException var8) {
                Citadel.LOGGER.error("Biome Generation Config: Could not write " + configFile, var8);
            }
        } catch (Exception var9) {
            Citadel.LOGGER.error("Biome Generation Config: Could not load " + configFile, var9);
        }
        return defaults;
    }

    private static File getConfigDirectory() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path jsonPath = Paths.get(configPath.toAbsolutePath().toString(), "alexscaves_biome_generation");
        return jsonPath.toFile();
    }

    private static BiomeGenerationNoiseCondition getConfigData(String fileName, BiomeGenerationNoiseCondition defaultConfigData) {
        return getOrCreateConfigFile(getConfigDirectory(), fileName, defaultConfigData, (new TypeToken<BiomeGenerationNoiseCondition>() {
        }).getType(), BiomeGenerationNoiseCondition::isInvalid);
    }
}