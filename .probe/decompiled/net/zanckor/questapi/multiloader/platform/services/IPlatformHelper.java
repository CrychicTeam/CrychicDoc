package net.zanckor.questapi.multiloader.platform.services;

public interface IPlatformHelper {

    PlatformEnum getPlatform();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return this.isDevelopmentEnvironment() ? "development" : "production";
    }
}