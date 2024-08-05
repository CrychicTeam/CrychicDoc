package com.cupboard.config;

import com.cupboard.Cupboard;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardWatchEventKinds;
import java.nio.file.WatchEvent;
import java.nio.file.WatchKey;
import java.nio.file.WatchService;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.Map.Entry;
import net.minecraftforge.fml.loading.FMLPaths;

public class CupboardConfig<C extends ICommonConfig> {

    private static Set<CupboardConfig> allConfigs = new HashSet();

    private static WatchService watchService = null;

    private static Map<String, CupboardConfig> watchedConfigs = new HashMap();

    private static final Map<CupboardConfig, Integer> scheuledReloads = new HashMap();

    private static long lastUpdate = 0L;

    private C commonConfig;

    private final String filename;

    private int loaded = 0;

    private int saveCounter = 0;

    private final Path configPath;

    static final Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();

    public static void initloadAll() {
        for (CupboardConfig config : allConfigs) {
            config.getCommonConfig();
        }
    }

    private static void registerConfig(CupboardConfig config) {
        if (!allConfigs.contains(config)) {
            allConfigs.add(config);
            try {
                config.configPath.getParent().register(getWatchService(), StandardWatchEventKinds.ENTRY_MODIFY);
                watchedConfigs.put(config.filename, config);
            } catch (IOException var2) {
                Cupboard.LOGGER.warn("Failed to register config path to file watcher", var2);
            }
        }
    }

    private static WatchService getWatchService() {
        if (watchService == null) {
            try {
                watchService = FileSystems.getDefault().newWatchService();
            } catch (Throwable var1) {
                Cupboard.LOGGER.warn("Failed to create config file watcher", var1);
            }
        }
        return watchService;
    }

    public static void pollConfigs() {
        if (System.currentTimeMillis() - lastUpdate > 1000L) {
            lastUpdate = System.currentTimeMillis();
            WatchKey watchKey = getWatchService().poll();
            if (watchKey != null) {
                for (WatchEvent<?> event : watchKey.pollEvents()) {
                    if (event.kind() == StandardWatchEventKinds.ENTRY_MODIFY) {
                        CupboardConfig config = (CupboardConfig) watchedConfigs.get(event.context().toString());
                        if (config != null) {
                            if (config.saveCounter > 0) {
                                config.saveCounter = 0;
                            } else {
                                scheuledReloads.put(config, 10);
                            }
                        }
                    }
                }
                watchKey.reset();
            }
            Iterator<Entry<CupboardConfig, Integer>> iterator = scheuledReloads.entrySet().iterator();
            while (iterator.hasNext()) {
                Entry<CupboardConfig, Integer> entry = (Entry<CupboardConfig, Integer>) iterator.next();
                if ((Integer) entry.getValue() > 0) {
                    entry.setValue((Integer) entry.getValue() - 1);
                } else {
                    iterator.remove();
                    ((CupboardConfig) entry.getKey()).load(false);
                }
            }
        }
    }

    public CupboardConfig(String filename, C commonConfig) {
        this.commonConfig = commonConfig;
        this.filename = filename.replace(".json", "") + ".json";
        this.configPath = FMLPaths.CONFIGDIR.get().resolve(this.filename);
        registerConfig(this);
    }

    public void load() {
        this.load(true);
    }

    public void load(boolean manualReload) {
        this.loaded++;
        File config = this.configPath.toFile();
        if (!config.exists()) {
            Cupboard.LOGGER.warn("Config " + this.filename + " not found, recreating default");
            if (this.loaded < 3 && manualReload) {
                this.save();
                this.load();
            }
        } else {
            JsonObject jsonFileData = null;
            try {
                BufferedReader reader = Files.newBufferedReader(this.configPath);
                try {
                    jsonFileData = (JsonObject) gson.fromJson(reader, JsonObject.class);
                    this.commonConfig.deserialize(jsonFileData);
                    Cupboard.LOGGER.info("Loaded config for: " + this.filename);
                } catch (Throwable var9) {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Throwable var8) {
                            var9.addSuppressed(var8);
                        }
                    }
                    throw var9;
                }
                if (reader != null) {
                    reader.close();
                }
            } catch (Exception var11) {
                if (jsonFileData != null) {
                    JsonObject defaultConfig = this.commonConfig.serialize();
                    for (Entry<String, JsonElement> defaultEntry : defaultConfig.asMap().entrySet()) {
                        if (!jsonFileData.has((String) defaultEntry.getKey())) {
                            jsonFileData.add((String) defaultEntry.getKey(), (JsonElement) defaultEntry.getValue());
                        }
                    }
                    try {
                        this.commonConfig.deserialize(jsonFileData);
                        this.save();
                        Cupboard.LOGGER.info("Loaded config for: " + this.filename);
                        return;
                    } catch (Exception var10) {
                    }
                }
                Cupboard.LOGGER.error("Could not read config " + this.filename + " from:" + this.configPath, var11);
                if (this.loaded < 3 && manualReload) {
                    this.save();
                    this.load();
                }
            }
        }
    }

    public void save() {
        this.saveCounter++;
        try {
            BufferedWriter writer = Files.newBufferedWriter(this.configPath);
            try {
                gson.toJson(this.commonConfig.serialize(), JsonObject.class, writer);
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
        } catch (Throwable var6) {
            Cupboard.LOGGER.error("Could not write config " + this.filename + " to:" + this.configPath, var6);
        }
    }

    public C getCommonConfig() {
        if (this.loaded == 0) {
            this.load();
        }
        return this.commonConfig;
    }

    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o != null && this.getClass() == o.getClass()) {
            CupboardConfig<?> config = (CupboardConfig<?>) o;
            return Objects.equals(this.configPath, config.configPath);
        } else {
            return false;
        }
    }

    public int hashCode() {
        return this.configPath.hashCode();
    }
}