package dev.architectury.networking.simple;

import net.minecraft.network.protocol.Packet;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.chunk.LevelChunk;

public abstract class BaseS2CMessage extends Message {

    private void sendTo(ServerPlayer player, Packet<?> packet) {
        if (player == null) {
            throw new NullPointerException("Unable to send packet '" + this.getType().getId() + "' to a 'null' player!");
        } else {
            player.connection.send(packet);
        }
    }

    public final void sendTo(ServerPlayer player) {
        this.sendTo(player, this.toPacket());
    }

    public final void sendTo(Iterable<ServerPlayer> players) {
        Packet<?> packet = this.toPacket();
        for (ServerPlayer player : players) {
            this.sendTo(player, packet);
        }
    }

    public final void sendToAll(MinecraftServer server) {
        this.sendTo(server.getPlayerList().getPlayers());
    }

    public final void sendToLevel(ServerLevel level) {
        this.sendTo(level.players());
    }

    public final void sendToChunkListeners(LevelChunk chunk) {
        Packet<?> packet = this.toPacket();
        ((ServerChunkCache) chunk.getLevel().m_7726_()).chunkMap.getPlayers(chunk.m_7697_(), false).forEach(e -> this.sendTo(e, packet));
    }
}