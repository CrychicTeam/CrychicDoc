package me.jellysquid.mods.lithium.mixin.alloc.chunk_random;

import me.jellysquid.mods.lithium.common.world.ChunkRandomSource;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin({ ServerLevel.class })
public abstract class ServerWorldMixin {

    private final BlockPos.MutableBlockPos randomPosInChunkCachedPos = new BlockPos.MutableBlockPos();

    @Redirect(method = { "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/server/world/ServerWorld;getRandomPosInChunk(IIII)Lnet/minecraft/util/math/BlockPos;"))
    private BlockPos redirectTickGetRandomPosInChunk(ServerLevel serverWorld, int x, int y, int z, int mask) {
        ((ChunkRandomSource) serverWorld).getRandomPosInChunk(x, y, z, mask, this.randomPosInChunkCachedPos);
        return this.randomPosInChunkCachedPos;
    }

    @Redirect(method = { "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;randomTick(Lnet/minecraft/server/world/ServerWorld;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V"))
    private void redirectBlockStateTick(BlockState blockState, ServerLevel world, BlockPos pos, RandomSource rand) {
        blockState.m_222972_(world, pos.immutable(), rand);
    }

    @Redirect(method = { "tickChunk(Lnet/minecraft/world/chunk/WorldChunk;I)V" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/fluid/FluidState;onRandomTick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/random/Random;)V"))
    private void redirectFluidStateTick(FluidState fluidState, Level world, BlockPos pos, RandomSource rand) {
        fluidState.randomTick(world, pos.immutable(), rand);
    }
}