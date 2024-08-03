package net.mehvahdjukaar.supplementaries.configs;

import dev.architectury.injectables.annotations.ExpectPlatform;
import dev.architectury.injectables.annotations.ExpectPlatform.Transformed;
import net.mehvahdjukaar.supplementaries.common.network.ModNetwork;
import net.mehvahdjukaar.supplementaries.common.network.ServerBoundRequestConfigReloadPacket;
import net.mehvahdjukaar.supplementaries.configs.forge.ConfigUtilsImpl;
import net.minecraft.client.Minecraft;
import net.minecraft.server.level.ServerPlayer;

public class ConfigUtils {

    @ExpectPlatform
    @Transformed
    public static void openModConfigs() {
        ConfigUtilsImpl.openModConfigs();
    }

    public static void clientRequestServerConfigReload() {
        if (Minecraft.getInstance().getConnection() != null) {
            ModNetwork.CHANNEL.sendToServer(new ServerBoundRequestConfigReloadPacket());
        }
    }

    public static void configScreenReload(ServerPlayer player) {
        CommonConfigs.SPEC.loadFromFile();
        CommonConfigs.SPEC.syncConfigsToPlayer(player);
    }
}