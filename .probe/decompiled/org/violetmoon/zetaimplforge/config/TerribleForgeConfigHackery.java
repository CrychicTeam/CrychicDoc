package org.violetmoon.zetaimplforge.config;

import com.electronwill.nightconfig.core.CommentedConfig;
import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;
import java.lang.reflect.Method;
import java.nio.file.Path;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;
import net.minecraftforge.fml.util.ObfuscationReflectionHelper;

public class TerribleForgeConfigHackery {

    private static final Method SET_CONFIG_DATA = ObfuscationReflectionHelper.findMethod(ModConfig.class, "setConfigData", new Class[] { CommentedConfig.class });

    private static final Method SETUP_CONFIG_FILE = ObfuscationReflectionHelper.findMethod(ConfigFileTypeHandler.class, "setupConfigFile", new Class[] { ModConfig.class, Path.class, ConfigFormat.class });

    public static void registerAndLoadConfigEarlierThanUsual(ForgeConfigSpec spec) {
        ModContainer container = ModLoadingContext.get().getActiveContainer();
        ModConfig modConfig = new ModConfig(Type.COMMON, spec, container);
        container.addConfig(modConfig);
        ConfigFileTypeHandler handler = modConfig.getHandler();
        CommentedFileConfig configData = readConfig(handler, FMLPaths.CONFIGDIR.get(), modConfig);
        SET_CONFIG_DATA.setAccessible(true);
        try {
            SET_CONFIG_DATA.invoke(modConfig, configData);
        } catch (Exception var6) {
        }
        modConfig.save();
    }

    private static CommentedFileConfig readConfig(ConfigFileTypeHandler handler, Path configBasePath, ModConfig c) {
        Path configPath = configBasePath.resolve(c.getFileName());
        CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(configPath).sync().preserveInsertionOrder().autosave().onFileNotFound((newfile, configFormat) -> {
            try {
                return (Boolean) SETUP_CONFIG_FILE.invoke(handler, c, newfile, configFormat);
            } catch (Exception var5) {
                throw new TerribleForgeConfigHackery.ConfigLoadingException(c, var5);
            }
        }).writingMode(WritingMode.REPLACE).build();
        try {
            configData.load();
            return configData;
        } catch (Exception var6) {
            throw new TerribleForgeConfigHackery.ConfigLoadingException(c, var6);
        }
    }

    private static class ConfigLoadingException extends RuntimeException {

        private static final long serialVersionUID = 1554369973578001612L;

        public ConfigLoadingException(ModConfig config, Exception cause) {
            super("Failed loading config file " + config.getFileName() + " of type " + config.getType() + " for modid " + config.getModId(), cause);
        }
    }
}