package com.mrcrayfish.configured.platform.services;

import com.mrcrayfish.configured.api.Environment;
import java.nio.file.Path;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;

public interface IPlatformHelper {

    String getPlatformName();

    boolean isModLoaded(String var1);

    boolean isDevelopmentEnvironment();

    default String getEnvironmentName() {
        return this.isDevelopmentEnvironment() ? "development" : "production";
    }

    Environment getEnvironment();

    Path getGamePath();

    Path getConfigPath();

    String getDefaultConfigPath();

    boolean isConnectionActive(ClientPacketListener var1);

    void sendSessionData(ServerPlayer var1);

    void sendFrameworkConfigToServer(ResourceLocation var1, byte[] var2);

    void sendFrameworkConfigRequest(ResourceLocation var1);

    void sendFrameworkConfigResponse(ServerPlayer var1, byte[] var2);
}