package me.jellysquid.mods.lithium.mixin.world.chunk_access;

import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReferenceArray;
import me.jellysquid.mods.lithium.common.world.chunk.ChunkHolderExtended;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin({ ChunkHolder.class })
public class ChunkHolderMixin implements ChunkHolderExtended {

    @Shadow
    @Final
    private AtomicReferenceArray<CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> futures;

    @Shadow
    LevelChunk currentlyLoading;

    private long lastRequestTime;

    @Override
    public CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getFutureByStatus(int index) {
        return (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>) this.futures.get(index);
    }

    @Override
    public void setFutureForStatus(int index, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future) {
        this.futures.set(index, future);
    }

    @Override
    public boolean updateLastAccessTime(long time) {
        long prev = this.lastRequestTime;
        this.lastRequestTime = time;
        return prev != time;
    }

    @Override
    public LevelChunk getCurrentlyLoading() {
        return this.currentlyLoading;
    }
}