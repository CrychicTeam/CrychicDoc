package com.chunksending;

import net.minecraft.network.protocol.Packet;
import net.minecraft.world.level.ChunkPos;

public interface IChunksendingPlayer {

    boolean attachToPending(ChunkPos var1, Packet<?> var2);
}