package dev.shadowsoffire.placebo.network;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

public class PacketDistro {

    public static void sendToTracking(SimpleChannel channel, Object packet, ServerLevel world, BlockPos pos) {
        world.getChunkSource().chunkMap.getPlayers(new ChunkPos(pos), false).forEach(p -> channel.sendTo(packet, p.connection.connection, NetworkDirection.PLAY_TO_CLIENT));
    }

    public static void sendTo(SimpleChannel channel, Object packet, Player player) {
        channel.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), packet);
    }

    public static void sendToAll(SimpleChannel channel, Object packet) {
        channel.send(PacketDistributor.ALL.noArg(), packet);
    }
}