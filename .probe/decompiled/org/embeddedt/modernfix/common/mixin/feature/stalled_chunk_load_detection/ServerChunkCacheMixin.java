package org.embeddedt.modernfix.common.mixin.feature.stalled_chunk_load_detection;

import com.mojang.datafixers.util.Either;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerChunkCache;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.chunk.EmptyLevelChunk;
import org.embeddedt.modernfix.ModernFix;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(value = { ServerChunkCache.class }, priority = 1100)
public abstract class ServerChunkCacheMixin {

    @Shadow
    @Final
    private Thread mainThread;

    @Shadow
    @Final
    public ServerLevel level;

    @Shadow
    @Final
    private ServerChunkCache.MainThreadExecutor mainThreadProcessor;

    private final boolean debugDeadServerAccess = Boolean.getBoolean("modernfix.debugBadChunkloading");

    @Shadow
    protected abstract CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> getChunkFutureMainThread(int var1, int var2, ChunkStatus var3, boolean var4);

    @Inject(method = { "getChunk" }, at = { @At("HEAD") }, cancellable = true)
    private void bailIfServerDead(int chunkX, int chunkZ, ChunkStatus requiredStatus, boolean load, CallbackInfoReturnable<ChunkAccess> cir) {
        if (!this.level.getServer().isRunning() && !this.mainThread.isAlive()) {
            ModernFix.LOGGER.fatal("A mod is accessing chunks from a stopped server (this will also cause memory leaks)");
            if (this.debugDeadServerAccess) {
                new Exception().printStackTrace();
            }
            Holder<Biome> plains = this.level.m_9598_().registryOrThrow(Registries.BIOME).getHolderOrThrow(Biomes.PLAINS);
            cir.setReturnValue(new EmptyLevelChunk(this.level, new ChunkPos(chunkX, chunkZ), plains));
        } else if (Thread.currentThread() != this.mainThread) {
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future = (CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>) CompletableFuture.supplyAsync(() -> this.getChunkFutureMainThread(chunkX, chunkZ, requiredStatus, false), this.mainThreadProcessor).join();
            if (!future.isDone()) {
                Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure> resultingChunk = null;
                try {
                    resultingChunk = (Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>) future.get(500L, TimeUnit.MILLISECONDS);
                } catch (ExecutionException | TimeoutException | InterruptedException var10) {
                }
                if (resultingChunk != null && resultingChunk.left().isPresent()) {
                    cir.setReturnValue((ChunkAccess) resultingChunk.left().get());
                    return;
                }
                if (this.debugDeadServerAccess) {
                    ModernFix.LOGGER.warn("Async loading of a chunk was requested, this might not be desirable", new Exception());
                }
                try {
                    resultingChunk = (Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>) future.get(10L, TimeUnit.SECONDS);
                    if (resultingChunk.left().isPresent()) {
                        cir.setReturnValue((ChunkAccess) resultingChunk.left().get());
                        return;
                    }
                } catch (ExecutionException | TimeoutException | InterruptedException var9) {
                    ModernFix.LOGGER.error("Async chunk load took way too long, this needs to be reported to the appropriate mod.", var9);
                }
            }
        }
    }
}