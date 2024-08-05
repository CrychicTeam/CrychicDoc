package net.mehvahdjukaar.moonlight.core.mixins;

import net.mehvahdjukaar.moonlight.api.block.IBlockHolder;
import net.mehvahdjukaar.moonlight.api.block.IPistonMotionReact;
import net.mehvahdjukaar.moonlight.core.misc.IExtendedPistonTile;
import net.mehvahdjukaar.moonlight.core.network.ClientBoundOnPistonMovedBlockPacket;
import net.mehvahdjukaar.moonlight.core.network.ModMessages;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.Shapes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin({ PistonMovingBlockEntity.class })
public abstract class PistonBlockEntityMixin extends BlockEntity implements IBlockHolder, IExtendedPistonTile {

    @Shadow
    private Direction direction;

    @Shadow
    private float progress;

    @Shadow
    private float progressO;

    @Shadow
    private BlockState movedState;

    @Shadow
    private boolean extending;

    protected PistonBlockEntityMixin(BlockEntityType<?> pType, BlockPos pWorldPosition, BlockState pBlockState) {
        super(pType, pWorldPosition, pBlockState);
    }

    @Override
    public BlockState getHeldBlock() {
        return this.movedState;
    }

    @Override
    public boolean setHeldBlock(BlockState state) {
        this.movedState = state;
        return true;
    }

    @Shadow
    protected abstract float getExtendedProgress(float var1);

    @Inject(method = { "tick" }, at = { @At("TAIL") })
    private static void whileMoving(Level pLevel, BlockPos pPos, BlockState pState, PistonMovingBlockEntity tile, CallbackInfo info) {
        if (tile instanceof IExtendedPistonTile t) {
            t.tickMovedBlock(pLevel, pPos);
        }
    }

    @Override
    public void tickMovedBlock(Level level, BlockPos pos) {
        if (this.progressO < 1.0F && this.movedState.m_60734_() instanceof IPistonMotionReact mr && mr.ticksWhileMoved()) {
            AABB aabb = this.moveByPositionAndProgress(pos, Shapes.block().bounds());
            mr.moveTick(level, pos, this.movedState, aabb, (PistonMovingBlockEntity) this);
        }
    }

    @Unique
    private AABB moveByPositionAndProgress(BlockPos pos, AABB aabb) {
        double d0 = (double) this.getExtendedProgress(this.progress);
        return aabb.move((double) pos.m_123341_() + d0 * (double) this.direction.getStepX(), (double) pos.m_123342_() + d0 * (double) this.direction.getStepY(), (double) pos.m_123343_() + d0 * (double) this.direction.getStepZ());
    }

    @Inject(method = { "finalTick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;neighborChanged(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;)V", shift = Shift.AFTER) })
    public void onFinishedShortPulse(CallbackInfo ci) {
        if (!this.f_58857_.isClientSide && this.movedState.m_60734_() instanceof IPistonMotionReact pr) {
            ModMessages.CHANNEL.sendToAllClientPlayersInDefaultRange(this.f_58857_, this.f_58858_, new ClientBoundOnPistonMovedBlockPacket(this.f_58858_, this.movedState, this.direction, this.extending));
            pr.onMoved(this.f_58857_, this.f_58858_, this.movedState, this.direction, this.extending);
        }
    }

    @Inject(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/block/Block;updateOrDestroy(Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/block/state/BlockState;Lnet/minecraft/world/level/LevelAccessor;Lnet/minecraft/core/BlockPos;I)V", shift = Shift.AFTER) }, require = 1)
    private static void onFinishedMoving(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity blockEntity, CallbackInfo ci) {
        BlockState movedState = blockEntity.getMovedState();
        if (!level.isClientSide && movedState.m_60734_() instanceof IPistonMotionReact pr) {
            ModMessages.CHANNEL.sendToAllClientPlayersInDefaultRange(level, pos, new ClientBoundOnPistonMovedBlockPacket(pos, movedState, blockEntity.getDirection(), blockEntity.isExtending()));
            pr.onMoved(level, blockEntity.m_58899_(), movedState, blockEntity.getDirection(), blockEntity.isExtending());
        }
    }

    @Inject(method = { "tick" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/world/level/Level;neighborChanged(Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/Block;Lnet/minecraft/core/BlockPos;)V", shift = Shift.AFTER) }, require = 1)
    private static void onFinishedMoving2(Level level, BlockPos pos, BlockState state, PistonMovingBlockEntity blockEntity, CallbackInfo ci) {
        BlockState movedState = blockEntity.getMovedState();
        if (!level.isClientSide && movedState.m_60734_() instanceof IPistonMotionReact pr) {
            ModMessages.CHANNEL.sendToAllClientPlayersInDefaultRange(level, pos, new ClientBoundOnPistonMovedBlockPacket(pos, movedState, blockEntity.getDirection(), blockEntity.isExtending()));
            pr.onMoved(level, blockEntity.m_58899_(), movedState, blockEntity.getDirection(), blockEntity.isExtending());
        }
    }
}