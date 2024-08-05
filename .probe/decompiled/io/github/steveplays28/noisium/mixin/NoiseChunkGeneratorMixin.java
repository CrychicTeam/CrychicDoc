package io.github.steveplays28.noisium.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Supplier;
import net.minecraft.Util;
import net.minecraft.world.level.StructureManager;
import net.minecraft.world.level.biome.BiomeSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.ChunkGenerator;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.level.levelgen.NoiseBasedChunkGenerator;
import net.minecraft.world.level.levelgen.RandomState;
import net.minecraft.world.level.levelgen.blending.Blender;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ NoiseBasedChunkGenerator.class })
public abstract class NoiseChunkGeneratorMixin extends ChunkGenerator {

    @Shadow
    protected abstract ChunkAccess doFill(Blender var1, StructureManager var2, RandomState var3, ChunkAccess var4, int var5, int var6);

    public NoiseChunkGeneratorMixin(BiomeSource biomeSource) {
        super(biomeSource);
    }

    @Redirect(method = { "populateNoise(Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/chunk/Chunk;II)Lnet/minecraft/world/chunk/Chunk;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/world/chunk/ChunkSection;setBlockState(IIILnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;"))
    private BlockState noisium$populateNoiseWrapSetBlockStateOperation(LevelChunkSection chunkSection, int chunkSectionBlockPosX, int chunkSectionBlockPosY, int chunkSectionBlockPosZ, BlockState blockState, boolean lock) {
        chunkSection.nonEmptyBlockCount++;
        if (!blockState.m_60819_().isEmpty()) {
            chunkSection.tickingFluidCount++;
        }
        if (blockState.m_60823_()) {
            chunkSection.tickingBlockCount++;
        }
        int blockStateId = chunkSection.states.data.palette.idFor(blockState);
        chunkSection.states.data.storage().set(chunkSection.states.strategy.getIndex(chunkSectionBlockPosX, chunkSectionBlockPosY, chunkSectionBlockPosZ), blockStateId);
        return blockState;
    }

    @Inject(method = { "populateNoise(Ljava/util/concurrent/Executor;Lnet/minecraft/world/gen/chunk/Blender;Lnet/minecraft/world/gen/noise/NoiseConfig;Lnet/minecraft/world/gen/StructureAccessor;Lnet/minecraft/world/chunk/Chunk;)Ljava/util/concurrent/CompletableFuture;" }, at = { @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/world/chunk/Chunk;getSectionIndex(I)I", ordinal = 1) }, cancellable = true)
    private void noisium$populateNoiseInject(Executor executor, Blender blender, RandomState noiseConfig, StructureManager structureAccessor, ChunkAccess chunk, CallbackInfoReturnable<CompletableFuture<ChunkAccess>> cir, @Local(ordinal = 1) int minimumYFloorDiv, @Local(ordinal = 2) int generationShapeHeightFloorDiv, @Local(ordinal = 3) int startingChunkSectionIndex, @Local(ordinal = 4) int minimumYChunkSectionIndex) {
        LevelChunkSection[] chunkSections = chunk.getSections();
        for (int chunkSectionIndex = startingChunkSectionIndex; chunkSectionIndex >= minimumYChunkSectionIndex; chunkSectionIndex--) {
            chunkSections[chunkSectionIndex].acquire();
        }
        cir.setReturnValue(CompletableFuture.supplyAsync(Util.wrapThreadWithTaskName("wgen_fill_noise", (Supplier) (() -> this.doFill(blender, structureAccessor, noiseConfig, chunk, minimumYFloorDiv, generationShapeHeightFloorDiv))), Util.backgroundExecutor()).whenCompleteAsync((chunk2, throwable) -> {
            for (int chunkSectionIndexx = startingChunkSectionIndex; chunkSectionIndexx >= minimumYChunkSectionIndex; chunkSectionIndexx--) {
                chunkSections[chunkSectionIndexx].release();
            }
        }, executor));
    }
}