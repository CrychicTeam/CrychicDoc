package me.jellysquid.mods.lithium.mixin.world.chunk_access;

import com.mojang.datafixers.util.Either;
import java.util.Arrays;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import me.jellysquid.mods.lithium.common.world.chunk.ChunkHolderExtended;
import net.minecraft.Util;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.DistanceManager;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.TicketType;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ServerChunkCache.class })
public abstract class ServerChunkManagerMixin {

    @Shadow
    @Final
    private ServerChunkCache.MainThreadExecutor mainThreadProcessor;

    @Shadow
    @Final
    private DistanceManager distanceManager;

    @Shadow
    @Final
    public ChunkMap chunkMap;

    @Shadow
    @Final
    Thread mainThread;

    private long time;

    private final long[] cacheKeys = new long[4];

    private final ChunkAccess[] cacheChunks = new ChunkAccess[4];

    @Shadow
    protected abstract ChunkHolder getVisibleChunkIfPresent(long var1);

    @Shadow
    protected abstract boolean chunkAbsent(ChunkHolder var1, int var2);

    @Shadow
    abstract boolean runDistanceManagerUpdates();

    @Inject(method = { "tick()Z" }, at = { @At("HEAD") })
    private void preTick(CallbackInfoReturnable<Boolean> cir) {
        this.time++;
    }

    @Overwrite
    public ChunkAccess getChunk(int x, int z, ChunkStatus status, boolean create) {
        if (Thread.currentThread() != this.mainThread) {
            return this.getChunkOffThread(x, z, status, create);
        } else {
            long[] cacheKeys = this.cacheKeys;
            long key = createCacheKey(x, z, status);
            for (int i = 0; i < 4; i++) {
                if (key == cacheKeys[i]) {
                    ChunkAccess chunk = this.cacheChunks[i];
                    if (chunk != null || !create) {
                        return chunk;
                    }
                }
            }
            ChunkAccess chunk = this.getChunkBlocking(x, z, status, create);
            if (chunk != null) {
                this.addToCache(key, chunk);
            } else if (create) {
                throw new IllegalStateException("Chunk not there when requested");
            }
            return chunk;
        }
    }

    private ChunkAccess getChunkOffThread(int x, int z, ChunkStatus status, boolean create) {
        return (ChunkAccess) CompletableFuture.supplyAsync(() -> this.getChunk(x, z, status, create), this.mainThreadProcessor).join();
    }

    private ChunkAccess getChunkBlocking(int x, int z, ChunkStatus status, boolean create) {
        long key = ChunkPos.asLong(x, z);
        int level = ChunkLevel.byStatus(status);
        ChunkHolder holder = this.getVisibleChunkIfPresent(key);
        if (holder != null && ((ChunkHolderExtended) holder).getCurrentlyLoading() != null) {
            return ((ChunkHolderExtended) holder).getCurrentlyLoading();
        } else {
            if (this.chunkAbsent(holder, level)) {
                if (!create) {
                    return null;
                }
                this.createChunkLoadTicket(x, z, level);
                this.runDistanceManagerUpdates();
                holder = this.getVisibleChunkIfPresent(key);
                if (this.chunkAbsent(holder, level)) {
                    throw (IllegalStateException) Util.pauseInIde(new IllegalStateException("No chunk holder after ticket has been added"));
                }
            } else if (create && ((ChunkHolderExtended) holder).updateLastAccessTime(this.time)) {
                this.createChunkLoadTicket(x, z, level);
            }
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> loadFuture = null;
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> statusFuture = ((ChunkHolderExtended) holder).getFutureByStatus(status.getIndex());
            if (statusFuture != null) {
                Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> immediate = (Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>) statusFuture.getNow(null);
                if (immediate != null) {
                    Optional<ChunkAccess> chunk = immediate.left();
                    if (chunk.isPresent()) {
                        return (ChunkAccess) chunk.get();
                    }
                } else {
                    loadFuture = statusFuture;
                }
            }
            if (loadFuture == null) {
                if (ChunkLevel.generationStatus(holder.getTicketLevel()).isOrAfter(status)) {
                    CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> mergedFuture = this.chunkMap.schedule(holder, status);
                    holder.updateChunkToSave(mergedFuture, "schedule chunk status");
                    ((ChunkHolderExtended) holder).setFutureForStatus(status.getIndex(), mergedFuture);
                    loadFuture = mergedFuture;
                } else {
                    if (statusFuture == null) {
                        return null;
                    }
                    loadFuture = statusFuture;
                }
            }
            if (!loadFuture.isDone()) {
                this.mainThreadProcessor.m_18701_(loadFuture::isDone);
            }
            return (ChunkAccess) ((Either) loadFuture.join()).left().orElse(null);
        }
    }

    private void createChunkLoadTicket(int x, int z, int level) {
        ChunkPos chunkPos = new ChunkPos(x, z);
        this.distanceManager.addTicket(TicketType.UNKNOWN, chunkPos, level, chunkPos);
    }

    private static long createCacheKey(int chunkX, int chunkZ, ChunkStatus status) {
        return (long) chunkX & 268435455L | ((long) chunkZ & 268435455L) << 28 | (long) status.getIndex() << 56;
    }

    private void addToCache(long key, ChunkAccess chunk) {
        for (int i = 3; i > 0; i--) {
            this.cacheKeys[i] = this.cacheKeys[i - 1];
            this.cacheChunks[i] = this.cacheChunks[i - 1];
        }
        this.cacheKeys[0] = key;
        this.cacheChunks[0] = chunk;
    }

    @Inject(method = { "initChunkCaches()V" }, at = { @At("HEAD") })
    private void onCachesCleared(CallbackInfo ci) {
        Arrays.fill(this.cacheKeys, Long.MAX_VALUE);
        Arrays.fill(this.cacheChunks, null);
    }
}