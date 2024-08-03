package me.jellysquid.mods.lithium.mixin.block.hopper;

import me.jellysquid.mods.lithium.common.hopper.UpdateReceiver;
import me.jellysquid.mods.lithium.common.world.blockentity.BlockEntityGetter;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.WorldlyContainerHolder;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HopperBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Intrinsic;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin({ HopperBlock.class })
public abstract class HopperBlockMixin extends BaseEntityBlock {

    protected HopperBlockMixin(BlockBehaviour.Properties settings) {
        super(settings);
    }

    @Intrinsic
    @Override
    public BlockState updateShape(BlockState myBlockState, Direction direction, BlockState newState, LevelAccessor world, BlockPos myPos, BlockPos posFrom) {
        return super.m_7417_(myBlockState, direction, newState, world, myPos, posFrom);
    }

    @Inject(method = { "getStateForNeighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/Direction;Lnet/minecraft/block/BlockState;Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/block/BlockState;" }, at = { @At("HEAD") })
    private void notifyOnNeighborUpdate(BlockState myBlockState, Direction direction, BlockState newState, LevelAccessor world, BlockPos myPos, BlockPos posFrom, CallbackInfoReturnable<BlockState> ci) {
        if (!world.m_5776_() && newState.m_60734_() instanceof WorldlyContainerHolder) {
            this.updateHopper(world, myBlockState, myPos, posFrom);
        }
    }

    @Inject(method = { "neighborUpdate(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/Block;Lnet/minecraft/util/math/BlockPos;Z)V" }, at = { @At("HEAD") })
    private void updateBlockEntity(BlockState myBlockState, Level world, BlockPos myPos, Block block, BlockPos posFrom, boolean moved, CallbackInfo ci) {
        if (!world.isClientSide()) {
            this.updateHopper(world, myBlockState, myPos, posFrom);
        }
    }

    private void updateHopper(LevelAccessor world, BlockState myBlockState, BlockPos myPos, BlockPos posFrom) {
        Direction facing = (Direction) myBlockState.m_61143_(HopperBlock.FACING);
        boolean above = posFrom.m_123342_() == myPos.m_123342_() + 1;
        if ((above || posFrom.m_123341_() == myPos.m_123341_() + facing.getStepX() && posFrom.m_123342_() == myPos.m_123342_() + facing.getStepY() && posFrom.m_123343_() == myPos.m_123343_() + facing.getStepZ()) && ((BlockEntityGetter) world).getLoadedExistingBlockEntity(myPos) instanceof UpdateReceiver updateReceiver) {
            updateReceiver.invalidateCacheOnNeighborUpdate(above);
        }
    }

    @Inject(method = { "onBlockAdded(Lnet/minecraft/block/BlockState;Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Z)V" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/block/HopperBlock;updateEnabled(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;I)V", shift = Shift.AFTER) })
    private void workAroundVanillaUpdateSuppression(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean moved, CallbackInfo ci) {
        if (world.getBlockState(pos) != state) {
            for (Direction direction : f_60441_) {
                if (((BlockEntityGetter) world).getLoadedExistingBlockEntity(pos.relative(direction)) instanceof UpdateReceiver updateReceiver) {
                    updateReceiver.invalidateCacheOnNeighborUpdate(direction == Direction.DOWN);
                }
            }
        }
    }
}