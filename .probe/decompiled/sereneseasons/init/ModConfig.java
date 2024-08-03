package sereneseasons.init;

import java.io.IOException;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig.Type;
import net.minecraftforge.fml.loading.FMLPaths;
import sereneseasons.config.FertilityConfig;
import sereneseasons.config.SeasonsConfig;
import sereneseasons.config.ServerConfig;
import sereneseasons.core.SereneSeasons;

public class ModConfig {

    public static void init() {
        Path configPath = FMLPaths.CONFIGDIR.get();
        Path modConfigPath = Paths.get(configPath.toAbsolutePath().toString(), "sereneseasons");
        try {
            Files.createDirectory(modConfigPath);
        } catch (FileAlreadyExistsException var3) {
        } catch (IOException var4) {
            SereneSeasons.LOGGER.error("Failed to create sereneseasons config directory", var4);
        }
        ModLoadingContext.get().registerConfig(Type.COMMON, FertilityConfig.SPEC, "sereneseasons/fertility.toml");
        ModLoadingContext.get().registerConfig(Type.COMMON, SeasonsConfig.SPEC, "sereneseasons/seasons.toml");
        ModLoadingContext.get().registerConfig(Type.SERVER, ServerConfig.SPEC, "sereneseasons-server.toml");
    }
}