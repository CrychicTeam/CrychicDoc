package net.minecraft.world.entity.vehicle;

import java.util.function.Function;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class DismountHelper {

    public static int[][] offsetsForDirection(Direction direction0) {
        Direction $$1 = direction0.getClockWise();
        Direction $$2 = $$1.getOpposite();
        Direction $$3 = direction0.getOpposite();
        return new int[][] { { $$1.getStepX(), $$1.getStepZ() }, { $$2.getStepX(), $$2.getStepZ() }, { $$3.getStepX() + $$1.getStepX(), $$3.getStepZ() + $$1.getStepZ() }, { $$3.getStepX() + $$2.getStepX(), $$3.getStepZ() + $$2.getStepZ() }, { direction0.getStepX() + $$1.getStepX(), direction0.getStepZ() + $$1.getStepZ() }, { direction0.getStepX() + $$2.getStepX(), direction0.getStepZ() + $$2.getStepZ() }, { $$3.getStepX(), $$3.getStepZ() }, { direction0.getStepX(), direction0.getStepZ() } };
    }

    public static boolean isBlockFloorValid(double double0) {
        return !Double.isInfinite(double0) && double0 < 1.0;
    }

    public static boolean canDismountTo(CollisionGetter collisionGetter0, LivingEntity livingEntity1, AABB aABB2) {
        for (VoxelShape $$4 : collisionGetter0.getBlockCollisions(livingEntity1, aABB2)) {
            if (!$$4.isEmpty()) {
                return false;
            }
        }
        return collisionGetter0.getWorldBorder().isWithinBounds(aABB2);
    }

    public static boolean canDismountTo(CollisionGetter collisionGetter0, Vec3 vec1, LivingEntity livingEntity2, Pose pose3) {
        return canDismountTo(collisionGetter0, livingEntity2, livingEntity2.getLocalBoundsForPose(pose3).move(vec1));
    }

    public static VoxelShape nonClimbableShape(BlockGetter blockGetter0, BlockPos blockPos1) {
        BlockState $$2 = blockGetter0.getBlockState(blockPos1);
        return !$$2.m_204336_(BlockTags.CLIMBABLE) && (!($$2.m_60734_() instanceof TrapDoorBlock) || !$$2.m_61143_(TrapDoorBlock.OPEN)) ? $$2.m_60812_(blockGetter0, blockPos1) : Shapes.empty();
    }

    public static double findCeilingFrom(BlockPos blockPos0, int int1, Function<BlockPos, VoxelShape> functionBlockPosVoxelShape2) {
        BlockPos.MutableBlockPos $$3 = blockPos0.mutable();
        int $$4 = 0;
        while ($$4 < int1) {
            VoxelShape $$5 = (VoxelShape) functionBlockPosVoxelShape2.apply($$3);
            if (!$$5.isEmpty()) {
                return (double) (blockPos0.m_123342_() + $$4) + $$5.min(Direction.Axis.Y);
            }
            $$4++;
            $$3.move(Direction.UP);
        }
        return Double.POSITIVE_INFINITY;
    }

    @Nullable
    public static Vec3 findSafeDismountLocation(EntityType<?> entityType0, CollisionGetter collisionGetter1, BlockPos blockPos2, boolean boolean3) {
        if (boolean3 && entityType0.isBlockDangerous(collisionGetter1.m_8055_(blockPos2))) {
            return null;
        } else {
            double $$4 = collisionGetter1.m_45564_(nonClimbableShape(collisionGetter1, blockPos2), () -> nonClimbableShape(collisionGetter1, blockPos2.below()));
            if (!isBlockFloorValid($$4)) {
                return null;
            } else if (boolean3 && $$4 <= 0.0 && entityType0.isBlockDangerous(collisionGetter1.m_8055_(blockPos2.below()))) {
                return null;
            } else {
                Vec3 $$5 = Vec3.upFromBottomCenterOf(blockPos2, $$4);
                AABB $$6 = entityType0.getDimensions().makeBoundingBox($$5);
                for (VoxelShape $$8 : collisionGetter1.getBlockCollisions(null, $$6)) {
                    if (!$$8.isEmpty()) {
                        return null;
                    }
                }
                if (entityType0 != EntityType.PLAYER || !collisionGetter1.m_8055_(blockPos2).m_204336_(BlockTags.INVALID_SPAWN_INSIDE) && !collisionGetter1.m_8055_(blockPos2.above()).m_204336_(BlockTags.INVALID_SPAWN_INSIDE)) {
                    return !collisionGetter1.getWorldBorder().isWithinBounds($$6) ? null : $$5;
                } else {
                    return null;
                }
            }
        }
    }
}