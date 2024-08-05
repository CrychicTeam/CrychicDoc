package com.yungnickyoung.minecraft.yungsapi.services;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();

    default boolean isForge() {
        return false;
    }

    default boolean isFabric() {
        return false;
    }
}