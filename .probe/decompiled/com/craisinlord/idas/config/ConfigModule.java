package com.craisinlord.idas.config;

public class ConfigModule {

    public ConfigModule.General general = new ConfigModule.General();

    public static class General {

        public boolean disableIaFStructures = true;

        public boolean applyMiningFatigue = true;
    }
}