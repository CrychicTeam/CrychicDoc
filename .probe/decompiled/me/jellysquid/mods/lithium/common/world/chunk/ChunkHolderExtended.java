package me.jellysquid.mods.lithium.common.world.chunk;

import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;

public interface ChunkHolderExtended {

    CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getFutureByStatus(int var1);

    void setFutureForStatus(int var1, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> var2);

    boolean updateLastAccessTime(long var1);

    LevelChunk getCurrentlyLoading();
}