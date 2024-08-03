package net.mehvahdjukaar.modelfix.moonlight_configs.forge;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;

public class ConfigHelper {

    public static ModConfig addAndLoadConfigFile(ForgeConfigSpec targetSpec, String fileName, boolean addToMod) {
        ModContainer modContainer = ModLoadingContext.get().getActiveContainer();
        ModConfig config = new ModConfig(Type.COMMON, targetSpec, modContainer, fileName);
        if (addToMod) {
            modContainer.addConfig(config);
        }
        return config;
    }

    public static void reloadConfigFile(ModConfig config) {
    }
}