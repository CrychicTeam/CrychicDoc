package com.craisinlord.integrated_villages.config;

public class ConfigModule {

    public ConfigModule.General general = new ConfigModule.General();

    public static class General {

        public boolean disableVanillaVillages = true;

        public boolean activateCreateContraptions = true;
    }
}