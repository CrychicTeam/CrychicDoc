package me.steinborn.krypton.mod.shared.network.util;

import me.steinborn.krypton.mixin.shared.network.util.ServerPlayNetworkHandlerAccessor;
import me.steinborn.krypton.mod.shared.network.ConfigurableAutoFlush;
import net.minecraft.server.level.ServerPlayer;

public class AutoFlushUtil {

    public static void setAutoFlush(ServerPlayer player, boolean val) {
        if (player.getClass() == ServerPlayer.class) {
            ConfigurableAutoFlush configurableAutoFlusher = (ConfigurableAutoFlush) ((ServerPlayNetworkHandlerAccessor) player.connection).getConnection();
            configurableAutoFlusher.setShouldAutoFlush(val);
        }
    }

    private AutoFlushUtil() {
    }
}