package net.minecraft.server;

import com.mojang.logging.LogUtils;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import net.minecraft.SharedConstants;
import org.slf4j.Logger;

public class Eula {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final Path file;

    private final boolean agreed;

    public Eula(Path path0) {
        this.file = path0;
        this.agreed = SharedConstants.IS_RUNNING_IN_IDE || this.readFile();
    }

    private boolean readFile() {
        try {
            InputStream $$0 = Files.newInputStream(this.file);
            boolean var3;
            try {
                Properties $$1 = new Properties();
                $$1.load($$0);
                var3 = Boolean.parseBoolean($$1.getProperty("eula", "false"));
            } catch (Throwable var5) {
                if ($$0 != null) {
                    try {
                        $$0.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if ($$0 != null) {
                $$0.close();
            }
            return var3;
        } catch (Exception var6) {
            LOGGER.warn("Failed to load {}", this.file);
            this.saveDefaults();
            return false;
        }
    }

    public boolean hasAgreedToEULA() {
        return this.agreed;
    }

    private void saveDefaults() {
        if (!SharedConstants.IS_RUNNING_IN_IDE) {
            try {
                OutputStream $$0 = Files.newOutputStream(this.file);
                try {
                    Properties $$1 = new Properties();
                    $$1.setProperty("eula", "false");
                    $$1.store($$0, "By changing the setting below to TRUE you are indicating your agreement to our EULA (https://aka.ms/MinecraftEULA).");
                } catch (Throwable var5) {
                    if ($$0 != null) {
                        try {
                            $$0.close();
                        } catch (Throwable var4) {
                            var5.addSuppressed(var4);
                        }
                    }
                    throw var5;
                }
                if ($$0 != null) {
                    $$0.close();
                }
            } catch (Exception var6) {
                LOGGER.warn("Failed to save {}", this.file, var6);
            }
        }
    }
}