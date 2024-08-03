package com.corosus.coroutil.util;

import com.corosus.modconfig.IConfigCategory;
import com.corosus.modconfig.ModConfigData;

public class MultiLoaderUtil {

    private static final MultiLoaderUtil instance = new MultiLoaderUtil();

    private boolean checkForge = true;

    private boolean isForge = false;

    private boolean checkFabric = true;

    private boolean isFabric = false;

    private MultiLoaderUtil() {
    }

    public static synchronized MultiLoaderUtil instance() {
        return instance;
    }

    public synchronized boolean isForge() {
        if (this.checkForge) {
            try {
                this.checkForge = false;
                this.isForge = Class.forName("com.corosus.coroutil.loader.forge.ConfigModForge") != null;
                if (this.isForge) {
                    CULog.log("forge loader environment detected");
                }
            } catch (ClassNotFoundException var2) {
            }
        }
        if (this.isForge && this.isFabric()) {
            CULog.err("ERROR: DETECTED FABRIC AND FORGE BOTH PRESENT, THIS MIGHT BREAK THIS LOGIC, should only happen when using build_dev.gradle");
        }
        return this.isForge;
    }

    public synchronized boolean isFabric() {
        if (this.checkFabric) {
            try {
                this.checkFabric = false;
                this.isFabric = Class.forName("com.corosus.coroutil.loader.fabric.ConfigModFabric") != null;
                if (this.isFabric) {
                    CULog.log("fabric loader environment detected");
                }
            } catch (ClassNotFoundException var2) {
            }
        }
        return this.isFabric;
    }

    public synchronized ModConfigData makeLoaderSpecificConfigData(String savePath, String parStr, Class parClass, IConfigCategory parConfig) {
        if (this.isFabric()) {
            return this.constructLoaderSpecificConfigData("com.corosus.coroutil.loader.fabric.ModConfigDataFabric", savePath, parStr, parClass, parConfig);
        } else {
            return this.isForge() ? this.constructLoaderSpecificConfigData("com.corosus.coroutil.loader.forge.ModConfigDataForge", savePath, parStr, parClass, parConfig) : null;
        }
    }

    private ModConfigData constructLoaderSpecificConfigData(String clazz, String savePath, String parStr, Class parClass, IConfigCategory parConfig) {
        try {
            Class classToLoad = Class.forName(clazz);
            Class[] cArg = new Class[] { String.class, String.class, Class.class, IConfigCategory.class };
            return (ModConfigData) classToLoad.getDeclaredConstructor(cArg).newInstance(savePath, parStr, parClass, parConfig);
        } catch (Exception var8) {
            var8.printStackTrace();
            return null;
        }
    }
}