package com.chunksending.mixin;

import com.chunksending.ChunkSending;
import com.chunksending.IChunksendingPlayer;
import com.mojang.authlib.GameProfile;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ ServerPlayer.class })
public abstract class ServerPlayerChunkSending extends Player implements IChunksendingPlayer {

    @Shadow
    public ServerGamePacketListenerImpl connection;

    @Shadow
    private boolean disconnected;

    @Unique
    private Map<ChunkPos, List<Packet<?>>> chunksToSend = new HashMap();

    public ServerPlayerChunkSending(Level level0, BlockPos blockPos1, float float2, GameProfile gameProfile3) {
        super(level0, blockPos1, float2, gameProfile3);
    }

    @Inject(method = { "trackChunk" }, at = { @At("HEAD") }, cancellable = true)
    private void chunksending$trackChunk(ChunkPos pos, Packet<?> chunkPacket, CallbackInfo ci) {
        ci.cancel();
        List<Packet<?>> packetList = (List<Packet<?>>) this.chunksToSend.get(pos);
        if (packetList == null) {
            packetList = new ArrayList();
            this.chunksToSend.put(pos, packetList);
        }
        packetList.add(chunkPacket);
    }

    @Inject(method = { "tick" }, at = { @At("RETURN") })
    private void chunksending$update(CallbackInfo ci) {
        if (!this.chunksToSend.isEmpty()) {
            if (this.disconnected) {
                this.chunksToSend.clear();
            } else {
                List<Entry<ChunkPos, List<Packet<?>>>> packets = new ArrayList(this.chunksToSend.entrySet());
                packets.sort(Comparator.comparingDouble(e -> ((ChunkPos) e.getKey()).getMiddleBlockPosition(this.m_146904_()).m_123331_(this.m_20183_())));
                int amount = ChunkSending.config.getCommonConfig().maxChunksPerTick + packets.size() / 10;
                for (int i = 0; i < packets.size() && i < amount; i++) {
                    Entry<ChunkPos, List<Packet<?>>> entry = (Entry<ChunkPos, List<Packet<?>>>) packets.get(i);
                    for (Packet packet : (List) entry.getValue()) {
                        this.connection.send(packet);
                    }
                    this.chunksToSend.remove(entry.getKey());
                }
                if (ChunkSending.config.getCommonConfig().debugLogging) {
                    ChunkSending.LOGGER.info("Sent: " + amount + " packets to " + this.m_5446_().getString() + ", in queue:" + this.chunksToSend.size());
                }
            }
        }
    }

    @Override
    public boolean attachToPending(ChunkPos pos, Packet<?> packet) {
        List<Packet<?>> packetList = (List<Packet<?>>) this.chunksToSend.get(pos);
        if (packetList == null) {
            return false;
        } else {
            packetList.add(packet);
            return true;
        }
    }
}