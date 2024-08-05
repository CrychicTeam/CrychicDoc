package com.corosus.modconfig;

import com.corosus.coroutil.util.CULog;
import com.corosus.coroutil.util.MultiLoaderUtil;
import com.corosus.coroutil.util.OldUtil;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class CoroConfigRegistry {

    private static final CoroConfigRegistry instance = new CoroConfigRegistry();

    public List<ModConfigData> configs = new ArrayList();

    public List<ModConfigData> liveEditConfigs = new ArrayList();

    public ConcurrentHashMap<String, ModConfigData> lookupRegistryNameToConfig = new ConcurrentHashMap();

    public ConcurrentHashMap<String, ModConfigData> lookupFilePathToConfig = new ConcurrentHashMap();

    public boolean needsInitialConfigRegistration = true;

    private CoroConfigRegistry() {
    }

    public static synchronized CoroConfigRegistry instance() {
        return instance;
    }

    public void allModsConfigsLoadedAndRegisteredHook() {
        if (this.needsInitialConfigRegistration) {
            this.needsInitialConfigRegistration = false;
            instance().updateAllConfigsFromForge();
        }
    }

    public void onLoadOrReload(String filename) {
        ModConfigData configData = (ModConfigData) this.lookupFilePathToConfig.get(filename);
        if (configData != null) {
            dbg("Coro ConfigMod updating runtime values for file: " + filename);
            configData.updateConfigFieldValues();
            configData.configInstance.hookUpdatedValues();
        } else {
            dbg("ERROR, cannot find ModConfigData reference for filename: " + filename);
        }
    }

    public void updateAllConfigsFromForge() {
        for (ModConfigData configData : this.lookupFilePathToConfig.values()) {
            dbg("Coro ConfigMod updating runtime values for file: " + configData.saveFilePath);
            configData.updateConfigFieldValues();
            configData.configInstance.hookUpdatedValues();
        }
    }

    public void processHashMap(String modid, Map map) {
        for (Entry pairs : map.entrySet()) {
            String name = (String) pairs.getKey();
            Object val = pairs.getValue();
            String comment = this.getComment(modid, name);
            ConfigEntryInfo info = new ConfigEntryInfo(((ModConfigData) this.lookupRegistryNameToConfig.get(modid)).configData.size(), name, val, comment);
            ((ModConfigData) this.lookupRegistryNameToConfig.get(modid)).configData.add(info);
        }
    }

    public static void dbg(Object obj) {
        CULog.dbg(obj + "");
    }

    public synchronized void addConfigFile(String modID, IConfigCategory configCat) {
        this.addConfigFile(modID, configCat.getRegistryName(), configCat, true);
    }

    private void addConfigFile(String modID, String categoryName, IConfigCategory configCat, boolean liveEdit) {
        CULog.dbg("configmod - registering config: " + categoryName);
        if (!this.lookupRegistryNameToConfig.containsKey(configCat.getRegistryName())) {
            ModConfigData configData = MultiLoaderUtil.instance().makeLoaderSpecificConfigData(configCat.getConfigFileName(), categoryName, configCat.getClass(), configCat);
            if (configData == null) {
                CULog.err("ERROR: makeLoaderSpecificConfigData not implemented for active loader");
            } else {
                this.configs.add(configData);
                if (liveEdit) {
                    this.liveEditConfigs.add(configData);
                }
                this.lookupRegistryNameToConfig.put(categoryName, configData);
                this.lookupFilePathToConfig.put(configCat.getConfigFileName() + ".toml", configData);
                configData.initData();
                configData.writeConfigFile(false);
            }
        }
    }

    public Object getField(String configID, String name) {
        try {
            return OldUtil.getPrivateValue(((ModConfigData) this.lookupRegistryNameToConfig.get(configID)).configClass, instance, name);
        } catch (Exception var4) {
            var4.printStackTrace();
            return null;
        }
    }

    public String getComment(String configID, String name) {
        try {
            Field field = ((ModConfigData) this.lookupRegistryNameToConfig.get(configID)).configClass.getDeclaredField(name);
            ConfigComment anno_comment = (ConfigComment) field.getAnnotation(ConfigComment.class);
            return anno_comment == null ? "" : anno_comment.value()[0];
        } catch (NoSuchFieldException var5) {
            var5.printStackTrace();
        } catch (SecurityException var6) {
            var6.printStackTrace();
        }
        return "";
    }

    public boolean updateField(String configID, String name, Object obj) {
        return ((ModConfigData) this.lookupRegistryNameToConfig.get(configID)).setFieldBasedOnType(name, obj);
    }

    public synchronized void forceSaveAllFilesFromRuntimeSettings() {
        CULog.dbg("forceSaveAllFilesFromRuntimeSettings invoked");
        for (ModConfigData data : this.lookupRegistryNameToConfig.values()) {
            data.updateConfigFileWithRuntimeValues();
        }
        for (ModConfigData data : this.lookupRegistryNameToConfig.values()) {
            data.updateConfigFileWithRuntimeValues();
        }
    }

    public void forceLoadRuntimeSettingsFromFile() {
        CULog.dbg("forceLoadRuntimeSettingsFromFile invoked");
        for (ModConfigData data : this.lookupRegistryNameToConfig.values()) {
            data.writeConfigFile(false);
        }
    }
}