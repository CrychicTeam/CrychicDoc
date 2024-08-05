package mezz.jei.common.platform;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;
import net.minecraft.client.gui.screens.Screen;

public interface IPlatformConfigHelper {

    Path getModConfigDir();

    Optional<Screen> getConfigScreen();

    default Path createJeiConfigDir() {
        Path configDir = this.getModConfigDir().resolve("jei");
        try {
            Files.createDirectories(configDir);
            return configDir;
        } catch (IOException var3) {
            throw new RuntimeException("Unable to create JEI config directory: " + configDir, var3);
        }
    }
}