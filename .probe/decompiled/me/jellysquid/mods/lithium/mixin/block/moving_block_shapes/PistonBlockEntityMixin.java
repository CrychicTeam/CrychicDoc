package me.jellysquid.mods.lithium.mixin.block.moving_block_shapes;

import me.jellysquid.mods.lithium.common.shapes.OffsetVoxelShapeCache;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.piston.PistonHeadBlock;
import net.minecraft.world.level.block.piston.PistonMovingBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin({ PistonMovingBlockEntity.class })
public abstract class PistonBlockEntityMixin {

    private static final VoxelShape[] PISTON_BASE_WITH_MOVING_HEAD_SHAPES = precomputePistonBaseWithMovingHeadShapes();

    @Shadow
    private Direction direction;

    @Shadow
    private boolean extending;

    @Shadow
    private boolean isSourcePiston;

    @Shadow
    private BlockState movedState;

    @Inject(method = { "getCollisionShape(Lnet/minecraft/world/BlockView;Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/util/shape/VoxelShape;" }, at = { @At(value = "INVOKE", target = "Lnet/minecraft/util/math/Direction;getOffsetX()I", shift = Shift.BEFORE) }, locals = LocalCapture.CAPTURE_FAILHARD, cancellable = true)
    private void skipVoxelShapeUnion(BlockGetter world, BlockPos pos, CallbackInfoReturnable<VoxelShape> cir, VoxelShape voxelShape, Direction direction, BlockState blockState, float f) {
        if (!this.extending && this.isSourcePiston && this.movedState.m_60734_() instanceof PistonBaseBlock) {
            int index = getIndexForMergedShape(f, this.direction);
            cir.setReturnValue(PISTON_BASE_WITH_MOVING_HEAD_SHAPES[index]);
        } else {
            VoxelShape blockShape = blockState.m_60812_(world, pos);
            VoxelShape offsetAndSimplified = getOffsetAndSimplified(blockShape, Math.abs(f), f < 0.0F ? this.direction.getOpposite() : this.direction);
            cir.setReturnValue(offsetAndSimplified);
        }
    }

    private static VoxelShape getOffsetAndSimplified(VoxelShape blockShape, float offset, Direction direction) {
        VoxelShape offsetSimplifiedShape = ((OffsetVoxelShapeCache) blockShape).getOffsetSimplifiedShape(offset, direction);
        if (offsetSimplifiedShape == null) {
            offsetSimplifiedShape = blockShape.move((double) ((float) direction.getStepX() * offset), (double) ((float) direction.getStepY() * offset), (double) ((float) direction.getStepZ() * offset)).optimize();
            ((OffsetVoxelShapeCache) blockShape).setShape(offset, direction, offsetSimplifiedShape);
        }
        return offsetSimplifiedShape;
    }

    private static VoxelShape[] precomputePistonBaseWithMovingHeadShapes() {
        float[] offsets = new float[] { 0.0F, 0.5F, 1.0F };
        Direction[] directions = Direction.values();
        VoxelShape[] mergedShapes = new VoxelShape[offsets.length * directions.length];
        for (Direction facing : directions) {
            VoxelShape baseShape = ((BlockState) ((BlockState) Blocks.PISTON.defaultBlockState().m_61124_(PistonBaseBlock.EXTENDED, true)).m_61124_(PistonBaseBlock.f_52588_, facing)).m_60812_(null, null);
            for (float offset : offsets) {
                boolean isShort = offset < 0.25F;
                VoxelShape headShape = ((BlockState) ((BlockState) Blocks.PISTON_HEAD.defaultBlockState().m_61124_(PistonHeadBlock.f_52588_, facing)).m_61124_(PistonHeadBlock.SHORT, isShort)).m_60812_(null, null);
                VoxelShape offsetHead = headShape.move((double) ((float) facing.getStepX() * offset), (double) ((float) facing.getStepY() * offset), (double) ((float) facing.getStepZ() * offset));
                mergedShapes[getIndexForMergedShape(offset, facing)] = Shapes.or(baseShape, offsetHead);
            }
        }
        return mergedShapes;
    }

    private static int getIndexForMergedShape(float offset, Direction direction) {
        return offset != 0.0F && offset != 0.5F && offset != 1.0F ? -1 : (int) (2.0F * offset) + 3 * direction.get3DDataValue();
    }
}