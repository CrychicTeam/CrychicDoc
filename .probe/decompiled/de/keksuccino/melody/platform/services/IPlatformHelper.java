package de.keksuccino.melody.platform.services;

import com.mojang.blaze3d.platform.InputConstants;
import java.util.List;
import net.minecraft.client.KeyMapping;

public interface IPlatformHelper {

    String getPlatformName();

    String getPlatformDisplayName();

    String getLoaderVersion();

    boolean isModLoaded(String var1);

    String getModVersion(String var1);

    List<String> getLoadedModIds();

    boolean isDevelopmentEnvironment();

    boolean isOnClient();

    InputConstants.Key getKeyMappingKey(KeyMapping var1);

    default String getEnvironmentName() {
        return this.isDevelopmentEnvironment() ? "development" : "production";
    }
}