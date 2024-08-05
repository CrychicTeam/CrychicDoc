package org.embeddedt.embeddium.config;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import net.minecraftforge.fml.loading.FMLPaths;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ConfigMigrator {

    public static Logger LOGGER = LogManager.getLogger("Embeddium");

    public static Path handleConfigMigration(String fileName) {
        Path mainPath = FMLPaths.CONFIGDIR.get().resolve(fileName);
        try {
            if (Files.notExists(mainPath, new LinkOption[0])) {
                String legacyName = fileName.replace("embeddium", "rubidium");
                Path legacyPath = FMLPaths.CONFIGDIR.get().resolve(legacyName);
                if (Files.exists(legacyPath, new LinkOption[0])) {
                    Files.move(legacyPath, mainPath);
                    LOGGER.warn("Migrated {} config file to {}", legacyName, fileName);
                }
            }
        } catch (RuntimeException | IOException var4) {
            LOGGER.error("Exception encountered while attempting config migration", var4);
        }
        return mainPath;
    }
}