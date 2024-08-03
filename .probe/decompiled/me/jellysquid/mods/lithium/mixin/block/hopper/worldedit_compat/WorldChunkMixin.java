package me.jellysquid.mods.lithium.mixin.block.hopper.worldedit_compat;

import me.jellysquid.mods.lithium.common.compat.worldedit.WorldEditCompat;
import me.jellysquid.mods.lithium.common.hopper.UpdateReceiver;
import me.jellysquid.mods.lithium.common.util.DirectionConstants;
import me.jellysquid.mods.lithium.common.world.WorldHelper;
import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.LevelChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ LevelChunk.class })
public abstract class WorldChunkMixin {

    @Shadow
    public abstract Level getLevel();

    @Inject(method = { "setBlockState" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/BlockState;onBlockAdded(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V", shift = Shift.BEFORE) })
    private void updateHoppersIfWorldEditPresent(BlockPos pos, BlockState state, boolean moved, CallbackInfoReturnable<BlockState> cir) {
        if (WorldEditCompat.WORLD_EDIT_PRESENT && (state.m_60734_() instanceof WorldlyContainerHolder || state.m_155947_())) {
            updateHopperCachesOnNewInventoryAdded((LevelChunk) this, pos, this.getLevel());
        }
    }

    private static void updateHopperCachesOnNewInventoryAdded(LevelChunk worldChunk, BlockPos pos, Level world) {
        BlockPos.MutableBlockPos neighborPos = new BlockPos.MutableBlockPos();
        for (Direction offsetDirection : DirectionConstants.ALL) {
            neighborPos.setWithOffset(pos, offsetDirection);
            if ((WorldHelper.arePosWithinSameChunk(pos, neighborPos) ? worldChunk.getBlockEntity(neighborPos, LevelChunk.EntityCreationType.CHECK) : ((BlockEntityGetter) world).getLoadedExistingBlockEntity(neighborPos)) instanceof UpdateReceiver updateReceiver) {
                updateReceiver.invalidateCacheOnNeighborUpdate(offsetDirection.getOpposite());
            }
        }
    }
}