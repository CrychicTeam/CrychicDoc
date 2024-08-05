package net.blay09.mods.defaultoptions.config;

import net.blay09.mods.balm.api.Balm;

public class DefaultOptionsConfig {

    public static DefaultOptionsConfigData getActive() {
        return Balm.getConfig().getActive(DefaultOptionsConfigData.class);
    }

    public static void initialize() {
        Balm.getConfig().registerConfig(DefaultOptionsConfigData.class, null);
    }
}