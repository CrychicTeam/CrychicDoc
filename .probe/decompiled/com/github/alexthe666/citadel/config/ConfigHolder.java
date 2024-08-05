package com.github.alexthe666.citadel.config;

import net.minecraftforge.common.ForgeConfigSpec;
import org.apache.commons.lang3.tuple.Pair;

public class ConfigHolder {

    public static final ForgeConfigSpec SERVER_SPEC;

    public static final ServerConfig SERVER;

    static {
        Pair<ServerConfig, ForgeConfigSpec> specPair = new ForgeConfigSpec.Builder().configure(ServerConfig::new);
        SERVER = (ServerConfig) specPair.getLeft();
        SERVER_SPEC = (ForgeConfigSpec) specPair.getRight();
    }
}