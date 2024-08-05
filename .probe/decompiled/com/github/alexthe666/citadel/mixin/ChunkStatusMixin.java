package com.github.alexthe666.citadel.mixin;

import com.github.alexthe666.citadel.server.generation.IMultiNoiseBiomeSourceAccessor;
import com.mojang.datafixers.util.Either;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ThreadedLevelLightEngine;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.ChunkStatus;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplateManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ ChunkStatus.class })
public class ChunkStatusMixin {

    @Inject(at = { @At("HEAD") }, remap = true, cancellable = true, method = { "Lnet/minecraft/world/level/chunk/ChunkStatus;generate(Ljava/util/concurrent/Executor;Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/level/chunk/ChunkGenerator;Lnet/minecraft/world/level/levelgen/structure/templatesystem/StructureTemplateManager;Lnet/minecraft/server/level/ThreadedLevelLightEngine;Ljava/util/function/Function;Ljava/util/List;)Ljava/util/concurrent/CompletableFuture;" })
    private void citadel_fillFromNoise(Executor executor0, ServerLevel serverLevel, ChunkGenerator chunkGenerator, StructureTemplateManager structureTemplateManager1, ThreadedLevelLightEngine threadedLevelLightEngine2, Function<ChunkAccess, CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> functionChunkAccessCompletableFutureEitherChunkAccessChunkHolderChunkLoadingFailure3, List<ChunkAccess> listChunkAccess4, CallbackInfoReturnable<CompletableFuture<Either<ChunkAccess, ChunkHolder.ChunkLoadingFailure>>> cir) {
        if (chunkGenerator.getBiomeSource() instanceof IMultiNoiseBiomeSourceAccessor multiNoiseBiomeSourceAccessor) {
            multiNoiseBiomeSourceAccessor.setLastSampledSeed(serverLevel.getSeed());
            multiNoiseBiomeSourceAccessor.setLastSampledDimension(serverLevel.m_46472_());
        }
    }
}