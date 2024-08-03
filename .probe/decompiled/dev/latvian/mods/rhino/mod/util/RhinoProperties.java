package dev.latvian.mods.rhino.mod.util;

import com.mojang.logging.LogUtils;
import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import dev.latvian.mods.rhino.mod.util.forge.RhinoPropertiesImpl;
import java.io.InputStream;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Properties;

public enum RhinoProperties {

    INSTANCE;

    private final Properties properties = new Properties();

    private boolean writeProperties;

    @ExpectPlatform
    @Transformed
    public static Path getGameDir() {
        return RhinoPropertiesImpl.getGameDir();
    }

    @ExpectPlatform
    @Transformed
    public static boolean isDev() {
        return RhinoPropertiesImpl.isDev();
    }

    @ExpectPlatform
    @Transformed
    public static InputStream openResource(String path) throws Exception {
        return RhinoPropertiesImpl.openResource(path);
    }

    private RhinoProperties() {
        try {
            Path propertiesFile = getGameDir().resolve("rhino.local.properties");
            this.writeProperties = false;
            if (Files.exists(propertiesFile, new LinkOption[0])) {
                Reader reader = Files.newBufferedReader(propertiesFile);
                try {
                    this.properties.load(reader);
                } catch (Throwable var10) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var8) {
                            var10.addSuppressed(var8);
                        }
                    }
                    throw var10;
                }
                if (reader != null) {
                    reader.close();
                }
            } else {
                this.writeProperties = true;
            }
            if (this.writeProperties) {
                Writer writer = Files.newBufferedWriter(propertiesFile);
                try {
                    this.properties.store(writer, "Local properties for Rhino, please do not push this to version control if you don't know what you're doing!");
                } catch (Throwable var9) {
                    if (writer != null) {
                        try {
                            writer.close();
                        } catch (Throwable var7) {
                            var9.addSuppressed(var7);
                        }
                    }
                    throw var9;
                }
                if (writer != null) {
                    writer.close();
                }
            }
        } catch (Exception var11) {
            var11.printStackTrace();
        }
        LogUtils.getLogger().info("Rhino properties loaded.");
    }

    private void remove(String key) {
        String s = this.properties.getProperty(key);
        if (s != null) {
            this.properties.remove(key);
            this.writeProperties = true;
        }
    }

    private String get(String key, String def) {
        String s = this.properties.getProperty(key);
        if (s == null) {
            this.properties.setProperty(key, def);
            this.writeProperties = true;
            return def;
        } else {
            return s;
        }
    }

    private boolean get(String key, boolean def) {
        return this.get(key, def ? "true" : "false").equals("true");
    }
}