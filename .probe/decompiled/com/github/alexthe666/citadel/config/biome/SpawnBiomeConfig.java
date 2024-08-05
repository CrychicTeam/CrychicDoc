package com.github.alexthe666.citadel.config.biome;

import com.github.alexthe666.citadel.Citadel;
import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.commons.io.FileUtils;

public class SpawnBiomeConfig {

    public static final Gson GSON = new GsonBuilder().setPrettyPrinting().registerTypeAdapter(SpawnBiomeData.class, new SpawnBiomeData.Deserializer()).create();

    private final ResourceLocation fileName;

    private SpawnBiomeConfig(ResourceLocation fileName) {
        if (!fileName.getNamespace().endsWith(".json")) {
            this.fileName = new ResourceLocation(fileName.getNamespace(), fileName.getPath() + ".json");
        } else {
            this.fileName = fileName;
        }
    }

    public static SpawnBiomeData create(ResourceLocation fileName, SpawnBiomeData dataDefault) {
        SpawnBiomeConfig config = new SpawnBiomeConfig(fileName);
        return config.getConfigData(dataDefault);
    }

    public static <T> T getOrCreateConfigFile(File configDir, String configName, T defaults, Type type) {
        File configFile = new File(configDir, configName);
        if (!configFile.exists()) {
            try {
                FileUtils.write(configFile, GSON.toJson(defaults));
            } catch (IOException var7) {
                Citadel.LOGGER.error("Spawn Biome Config: Could not write " + configFile, var7);
            }
        }
        try {
            return (T) GSON.fromJson(FileUtils.readFileToString(configFile), type);
        } catch (Exception var6) {
            Citadel.LOGGER.error("Spawn Biome Config: Could not load " + configFile, var6);
            return defaults;
        }
    }

    private File getConfigDirFile() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path jsonPath = Paths.get(configPath.toAbsolutePath().toString(), this.fileName.getNamespace());
        return jsonPath.toFile();
    }

    private SpawnBiomeData getConfigData(SpawnBiomeData defaultConfigData) {
        return getOrCreateConfigFile(this.getConfigDirFile(), this.fileName.getPath(), defaultConfigData, (new TypeToken<SpawnBiomeData>() {
        }).getType());
    }
}