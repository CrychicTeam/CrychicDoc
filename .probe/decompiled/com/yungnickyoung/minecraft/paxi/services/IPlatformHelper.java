package com.yungnickyoung.minecraft.paxi.services;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();
}