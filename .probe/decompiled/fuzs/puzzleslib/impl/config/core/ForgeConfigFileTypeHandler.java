package fuzs.puzzleslib.impl.config.core;

import com.electronwill.nightconfig.core.ConfigFormat;
import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.file.FileConfig;
import com.electronwill.nightconfig.core.file.FileWatcher;
import com.electronwill.nightconfig.core.io.ParsingException;
import com.electronwill.nightconfig.core.io.WritingMode;
import com.mojang.logging.LogUtils;
import fuzs.puzzleslib.api.core.v1.ModLoaderEnvironment;
import fuzs.puzzleslib.impl.PuzzlesLib;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.function.Function;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.IConfigEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLConfig;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.slf4j.MarkerFactory;

public class ForgeConfigFileTypeHandler extends ConfigFileTypeHandler {

    static final Marker CONFIG = MarkerFactory.getMarker("CONFIG");

    private static final Logger LOGGER = LogUtils.getLogger();

    static final ConfigFileTypeHandler TOML = new ForgeConfigFileTypeHandler();

    private static final Path DEFAULT_CONFIGS_PATH = ModLoaderEnvironment.INSTANCE.getGameDirectory().resolve(FMLConfig.defaultConfigPath());

    private static void tryLoadConfigFile(FileConfig configData) {
        try {
            configData.load();
        } catch (ParsingException var4) {
            try {
                Files.delete(configData.getNioPath());
                configData.load();
                PuzzlesLib.LOGGER.warn("Configuration file {} could not be parsed. Correcting", configData.getNioPath());
            } catch (Throwable var3) {
                var4.addSuppressed(var3);
                throw var4;
            }
        }
    }

    private static Path getConfigPath(Path configBasePath, String fileName) {
        Path configPath = configBasePath.resolve(fileName);
        if (Files.notExists(configPath, new LinkOption[0])) {
            configPath = ModLoaderEnvironment.INSTANCE.getConfigDirectory().resolve(fileName);
        }
        return configPath;
    }

    public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
        return c -> {
            Path configPath = getConfigPath(configBasePath, c.getFileName());
            CommentedFileConfig configData = (CommentedFileConfig) CommentedFileConfig.builder(configPath).sync().preserveInsertionOrder().autosave().onFileNotFound((newfile, configFormat) -> this.setupConfigFile(c, newfile, configFormat)).writingMode(WritingMode.REPLACE).build();
            LOGGER.debug(CONFIG, "Built TOML config for {}", configPath);
            try {
                tryLoadConfigFile(configData);
            } catch (ParsingException var7) {
                throw new ForgeConfigFileTypeHandler.ConfigLoadingException(c, var7);
            }
            LOGGER.debug(CONFIG, "Loaded TOML config file {}", configPath);
            try {
                FileWatcher.defaultInstance().addWatch(configPath, new ForgeConfigFileTypeHandler.ConfigWatcher(c, configData, Thread.currentThread().getContextClassLoader()));
                LOGGER.debug(CONFIG, "Watching TOML config file {} for changes", configPath);
                return configData;
            } catch (IOException var6) {
                throw new RuntimeException("Couldn't watch config file", var6);
            }
        };
    }

    public void unload(Path configBasePath, ModConfig config) {
        Path configPath = getConfigPath(configBasePath, config.getFileName());
        try {
            FileWatcher.defaultInstance().removeWatch(configPath);
        } catch (RuntimeException var5) {
            LOGGER.error("Failed to remove config {} from tracker!", configPath, var5);
        }
    }

    private boolean setupConfigFile(ModConfig modConfig, Path file, ConfigFormat<?> conf) throws IOException {
        Files.createDirectories(file.getParent());
        Path p = DEFAULT_CONFIGS_PATH.resolve(modConfig.getFileName());
        if (Files.exists(p, new LinkOption[0])) {
            LOGGER.info(CONFIG, "Loading default config file from path {}", p);
            Files.copy(p, file);
        } else {
            Files.createFile(file);
            conf.initEmptyFile(file);
        }
        return true;
    }

    private static class ConfigLoadingException extends RuntimeException {

        public ConfigLoadingException(ModConfig config, Exception cause) {
            super("Failed loading config file " + config.getFileName() + " of type " + config.getType() + " for modid " + config.getModId(), cause);
        }
    }

    private static class ConfigWatcher implements Runnable {

        private final ModConfig modConfig;

        private final CommentedFileConfig commentedFileConfig;

        private final ClassLoader realClassLoader;

        private final ModContainer modContainer;

        ConfigWatcher(ModConfig modConfig, CommentedFileConfig commentedFileConfig, ClassLoader classLoader) {
            this.modConfig = modConfig;
            this.commentedFileConfig = commentedFileConfig;
            this.realClassLoader = classLoader;
            this.modContainer = (ModContainer) ModList.get().getModContainerById(modConfig.getModId()).orElseThrow();
        }

        public void run() {
            Thread.currentThread().setContextClassLoader(this.realClassLoader);
            if (!this.modConfig.getSpec().isCorrecting()) {
                try {
                    ForgeConfigFileTypeHandler.tryLoadConfigFile(this.commentedFileConfig);
                    if (!this.modConfig.getSpec().isCorrect(this.commentedFileConfig)) {
                        ForgeConfigFileTypeHandler.LOGGER.warn(ForgeConfigFileTypeHandler.CONFIG, "Configuration file {} is not correct. Correcting", this.commentedFileConfig.getFile().getAbsolutePath());
                        ConfigFileTypeHandler.backUpConfig(this.commentedFileConfig);
                        this.modConfig.getSpec().correct(this.commentedFileConfig);
                        this.commentedFileConfig.save();
                    }
                } catch (ParsingException var2) {
                    throw new ForgeConfigFileTypeHandler.ConfigLoadingException(this.modConfig, var2);
                }
                ForgeConfigFileTypeHandler.LOGGER.debug(ForgeConfigFileTypeHandler.CONFIG, "Config file {} changed, sending notifies", this.modConfig.getFileName());
                this.modConfig.getSpec().afterReload();
                this.modContainer.dispatchConfigEvent(IConfigEvent.reloading(this.modConfig));
            }
        }
    }
}