package com.corosus.modconfig;

import com.corosus.coroutil.config.ConfigCoroUtil;
import java.io.File;
import java.nio.file.Path;

public abstract class ConfigMod {

    public static final String MODID = "coroutil";

    private static ConfigMod instance;

    public ConfigMod() {
        instance = this;
        new File("./config/CoroUtil").mkdirs();
        CoroConfigRegistry.instance().addConfigFile("coroutil", new ConfigCoroUtil());
    }

    public static ConfigMod instance() {
        return instance;
    }

    public abstract Path getConfigPath();

    public static void addConfigFile(String modID, IConfigCategory configCat) {
        CoroConfigRegistry.instance().addConfigFile(modID, configCat);
    }

    public static void forceSaveAllFilesFromRuntimeSettings() {
        CoroConfigRegistry.instance().forceSaveAllFilesFromRuntimeSettings();
    }
}