package dev.latvian.mods.kubejs;

import dev.architectury.platform.Platform;
import dev.latvian.mods.kubejs.util.KubeJSPlugins;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.util.Properties;

public class DevProperties {

    private static DevProperties instance;

    private final Properties properties;

    private boolean writeProperties;

    public boolean debugInfo;

    public boolean dataPackOutput = false;

    public boolean logAddedRecipes = false;

    public boolean logRemovedRecipes = false;

    public boolean logModifiedRecipes = false;

    public boolean logSkippedRecipes = false;

    public boolean logSkippedTags = false;

    public boolean logErroringRecipes = true;

    public boolean logInvalidRecipeHandlers = true;

    public boolean logSkippedPlugins = true;

    public boolean logGeneratedData = false;

    public boolean strictTags = false;

    public boolean alwaysCaptureErrors = false;

    public static DevProperties get() {
        if (instance == null) {
            instance = new DevProperties();
        }
        return instance;
    }

    public static void reload() {
        instance = new DevProperties();
    }

    private DevProperties() {
        this.properties = new Properties();
        try {
            Path propertiesFile = KubeJSPaths.getLocalDevProperties();
            this.writeProperties = false;
            if (Files.exists(propertiesFile, new LinkOption[0])) {
                Reader reader = Files.newBufferedReader(propertiesFile);
                try {
                    this.properties.load(reader);
                } catch (Throwable var6) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var5) {
                            var6.addSuppressed(var5);
                        }
                    }
                    throw var6;
                }
                if (reader != null) {
                    reader.close();
                }
            } else {
                this.writeProperties = true;
            }
            this.debugInfo = this.get("debugInfo", Platform.isDevelopmentEnvironment());
            this.dataPackOutput = this.get("dataPackOutput", false);
            this.logAddedRecipes = this.get("logAddedRecipes", false);
            this.logRemovedRecipes = this.get("logRemovedRecipes", false);
            this.logModifiedRecipes = this.get("logModifiedRecipes", false);
            this.logSkippedRecipes = this.get("logSkippedRecipes", false);
            this.logSkippedTags = this.get("logSkippedTags", false);
            this.logErroringRecipes = this.get("logErroringRecipes", true);
            this.logInvalidRecipeHandlers = this.get("logInvalidRecipeHandlers", true);
            this.logSkippedPlugins = this.get("logSkippedPlugins", true);
            this.logGeneratedData = this.get("logGeneratedData", false);
            this.strictTags = this.get("strictTags", false);
            this.alwaysCaptureErrors = this.get("alwaysCaptureErrors", false);
            KubeJSPlugins.forEachPlugin(this, KubeJSPlugin::loadDevProperties);
            if (this.writeProperties) {
                this.save();
            }
        } catch (Exception var7) {
            var7.printStackTrace();
        }
        KubeJS.LOGGER.info("Loaded dev.properties");
    }

    public void remove(String key) {
        String s = this.properties.getProperty(key);
        if (s != null) {
            this.properties.remove(key);
            this.writeProperties = true;
        }
    }

    public String get(String key, String def) {
        String s = this.properties.getProperty(key);
        if (s == null) {
            this.properties.setProperty(key, def);
            this.writeProperties = true;
            return def;
        } else {
            return s;
        }
    }

    public boolean get(String key, boolean def) {
        return this.get(key, def ? "true" : "false").equals("true");
    }

    public void save() {
        try {
            Writer writer = Files.newBufferedWriter(KubeJSPaths.getLocalDevProperties());
            try {
                this.properties.store(writer, "KubeJS Dev Properties");
            } catch (Throwable var5) {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (Throwable var4) {
                        var5.addSuppressed(var4);
                    }
                }
                throw var5;
            }
            if (writer != null) {
                writer.close();
            }
        } catch (Exception var6) {
            var6.printStackTrace();
        }
    }
}