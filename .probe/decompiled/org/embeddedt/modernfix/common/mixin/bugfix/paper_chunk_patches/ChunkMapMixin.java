package org.embeddedt.modernfix.common.mixin.bugfix.paper_chunk_patches;

import com.mojang.datafixers.util.Either;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.server.level.TicketType;
import net.minecraft.server.level.progress.ChunkProgressListener;
import net.minecraft.util.thread.BlockableEventLoop;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkMap.class })
public abstract class ChunkMapMixin {

    @Shadow
    @Final
    private BlockableEventLoop<Runnable> mainThreadExecutor;

    @Shadow
    @Final
    private ChunkMap.DistanceManager distanceManager;

    @Shadow
    @Final
    private ServerLevel level;

    @Shadow
    @Final
    private ThreadedLevelLightEngine lightEngine;

    @Shadow
    @Final
    private ChunkProgressListener progressListener;

    @Shadow
    @Final
    private StructureTemplateManager structureTemplateManager;

    @Shadow
    protected abstract CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> protoChunkToFullChunk(ChunkHolder var1);

    @Shadow
    protected abstract CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> scheduleChunkGeneration(ChunkHolder var1, ChunkStatus var2);

    @ModifyArg(method = { "prepareAccessibleChunk" }, at = @At(value = "INVOKE", target = "Ljava/util/concurrent/CompletableFuture;thenApplyAsync(Ljava/util/function/Function;Ljava/util/concurrent/Executor;)Ljava/util/concurrent/CompletableFuture;"), index = 1)
    private Executor useMainThreadExecutor(Executor executor) {
        return this.mainThreadExecutor;
    }

    @Inject(method = { "schedule" }, at = { @At("HEAD") }, cancellable = true)
    private void useLegacySchedulingLogic(ChunkHolder holder, ChunkStatus requiredStatus, CallbackInfoReturnable<CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> cir) {
        if (requiredStatus != ChunkStatus.EMPTY && !requiredStatus.hasLoadDependencies()) {
            ChunkPos chunkpos = holder.getPos();
            CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> future = holder.getOrScheduleFuture(requiredStatus.getParent(), (ChunkMap) this);
            cir.setReturnValue(future.thenComposeAsync(either -> {
                Optional<ChunkAccess> optional = either.left();
                if (requiredStatus == ChunkStatus.LIGHT) {
                    this.distanceManager.m_140792_(TicketType.LIGHT, chunkpos, 33 + ChunkStatus.getDistance(ChunkStatus.LIGHT), chunkpos);
                }
                if (optional.isPresent() && ((ChunkAccess) optional.get()).getStatus().isOrAfter(requiredStatus)) {
                    CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>> completablefuture = requiredStatus.load(this.level, this.structureTemplateManager, this.lightEngine, arg2 -> this.protoChunkToFullChunk(holder), (ChunkAccess) optional.get());
                    this.progressListener.onStatusChange(chunkpos, requiredStatus);
                    return completablefuture;
                } else {
                    return this.scheduleChunkGeneration(holder, requiredStatus);
                }
            }, this.mainThreadExecutor).thenComposeAsync(CompletableFuture::completedFuture, this.mainThreadExecutor));
        }
    }
}