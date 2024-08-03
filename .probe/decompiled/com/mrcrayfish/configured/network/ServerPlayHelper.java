package com.mrcrayfish.configured.network;

import com.google.common.base.Preconditions;
import com.mrcrayfish.configured.Config;
import com.mrcrayfish.configured.Constants;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;

public class ServerPlayHelper {

    public static void sendMessageToOperators(Component message, ServerPlayer player) {
        MinecraftServer server = player.m_20194_();
        Preconditions.checkNotNull(server, "The server was null when broadcasting config changes. This should not be possible...");
        for (ServerPlayer serverPlayer : server.getPlayerList().getPlayers()) {
            if (server.getPlayerList().isOp(serverPlayer.m_36316_())) {
                serverPlayer.sendSystemMessage(message);
            }
        }
    }

    public static boolean canEditServerConfigs(ServerPlayer player) {
        MinecraftServer server = player.m_20194_();
        if (server != null && server.isDedicatedServer() && Config.isDeveloperEnabled()) {
            if (Config.getDevelopers().contains(player.m_20149_()) && server.getPlayerList().isOp(player.m_36316_())) {
                return true;
            } else {
                Constants.LOG.warn("{} tried to request or update a server config, however they are not a developer", player.m_7755_().getString());
                player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.unauthorized_request"));
                sendMessageToOperators(Component.translatable("configured.chat.authorized_player").withStyle(ChatFormatting.RED), player);
                return false;
            }
        } else {
            Constants.LOG.warn("{} tried to request or update a server config, however developer mode is not enabled", player.m_7755_().getString());
            player.connection.disconnect(Component.translatable("configured.multiplayer.disconnect.unauthorized_request"));
            sendMessageToOperators(Component.translatable("configured.chat.authorized_player").withStyle(ChatFormatting.RED), player);
            return false;
        }
    }
}