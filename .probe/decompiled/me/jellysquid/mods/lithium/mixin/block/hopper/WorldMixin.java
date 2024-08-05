package me.jellysquid.mods.lithium.mixin.block.hopper;

import java.util.Map;
import me.jellysquid.mods.lithium.common.hopper.UpdateReceiver;
import me.jellysquid.mods.lithium.common.util.DirectionConstants;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ Level.class })
public class WorldMixin {

    @Inject(method = { "markAndNotifyBlock" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/World;onBlockChanged(Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/BlockState;)V") })
    private void updateHopperOnUpdateSuppression(BlockPos pos, LevelChunk worldChunk, BlockState blockState, BlockState blockState2, int flags, int k, CallbackInfo ci) {
        if ((flags & 1) == 0) {
            Map<BlockPos, BlockEntity> blockEntities = WorldHelper.areNeighborsWithinSameChunk(pos) ? worldChunk.getBlockEntities() : null;
            if (blockState != blockState2 && (blockEntities == null || !blockEntities.isEmpty())) {
                for (Direction direction : DirectionConstants.ALL) {
                    BlockPos offsetPos = pos.relative(direction);
                    if ((blockEntities != null ? (BlockEntity) blockEntities.get(offsetPos) : ((BlockEntityGetter) this).getLoadedExistingBlockEntity(offsetPos)) instanceof UpdateReceiver updateReceiver) {
                        updateReceiver.invalidateCacheOnNeighborUpdate(direction == Direction.DOWN);
                    }
                }
            }
        }
    }
}