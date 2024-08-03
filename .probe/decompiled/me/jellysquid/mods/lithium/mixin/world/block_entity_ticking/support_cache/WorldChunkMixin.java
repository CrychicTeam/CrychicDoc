package me.jellysquid.mods.lithium.mixin.world.block_entity_ticking.support_cache;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import net.minecraft.world.level.chunk.LevelChunkSection;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ LevelChunk.class })
public abstract class WorldChunkMixin {

    @Shadow
    public abstract BlockState getBlockState(BlockPos var1);

    @Redirect(method = { "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;" }, at = @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockEntityProvider;createBlockEntity(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)Lnet/minecraft/block/entity/BlockEntity;"))
    private BlockEntity createBlockEntityWithCachedStateFix(EntityBlock blockEntityProvider, BlockPos pos, BlockState state) {
        return blockEntityProvider.newBlockEntity(pos, this.getBlockState(pos));
    }

    @Inject(method = { "setBlockState(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)Lnet/minecraft/block/BlockState;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntity;setCachedState(Lnet/minecraft/block/BlockState;)V", shift = Shift.AFTER) }, locals = LocalCapture.CAPTURE_FAILHARD)
    private void fixCachedState(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir, int i, LevelChunkSection chunkSection, boolean bl, int j, int k, int l, BlockState blockState, Block block, BlockEntity blockEntity) {
        BlockState blockState1 = this.getBlockState(pos);
        if (blockState1 != state) {
            blockEntity.setBlockState(blockState1);
        }
    }
}