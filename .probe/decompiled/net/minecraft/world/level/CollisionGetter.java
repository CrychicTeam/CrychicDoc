package net.minecraft.world.level;

import com.google.common.collect.Iterables;
import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public interface CollisionGetter extends BlockGetter {

    WorldBorder getWorldBorder();

    @Nullable
    BlockGetter getChunkForCollisions(int var1, int var2);

    default boolean isUnobstructed(@Nullable Entity entity0, VoxelShape voxelShape1) {
        return true;
    }

    default boolean isUnobstructed(BlockState blockState0, BlockPos blockPos1, CollisionContext collisionContext2) {
        VoxelShape $$3 = blockState0.m_60742_(this, blockPos1, collisionContext2);
        return $$3.isEmpty() || this.isUnobstructed(null, $$3.move((double) blockPos1.m_123341_(), (double) blockPos1.m_123342_(), (double) blockPos1.m_123343_()));
    }

    default boolean isUnobstructed(Entity entity0) {
        return this.isUnobstructed(entity0, Shapes.create(entity0.getBoundingBox()));
    }

    default boolean noCollision(AABB aABB0) {
        return this.noCollision(null, aABB0);
    }

    default boolean noCollision(Entity entity0) {
        return this.noCollision(entity0, entity0.getBoundingBox());
    }

    default boolean noCollision(@Nullable Entity entity0, AABB aABB1) {
        for (VoxelShape $$2 : this.getBlockCollisions(entity0, aABB1)) {
            if (!$$2.isEmpty()) {
                return false;
            }
        }
        if (!this.getEntityCollisions(entity0, aABB1).isEmpty()) {
            return false;
        } else if (entity0 == null) {
            return true;
        } else {
            VoxelShape $$3 = this.borderCollision(entity0, aABB1);
            return $$3 == null || !Shapes.joinIsNotEmpty($$3, Shapes.create(aABB1), BooleanOp.AND);
        }
    }

    List<VoxelShape> getEntityCollisions(@Nullable Entity var1, AABB var2);

    default Iterable<VoxelShape> getCollisions(@Nullable Entity entity0, AABB aABB1) {
        List<VoxelShape> $$2 = this.getEntityCollisions(entity0, aABB1);
        Iterable<VoxelShape> $$3 = this.getBlockCollisions(entity0, aABB1);
        return $$2.isEmpty() ? $$3 : Iterables.concat($$2, $$3);
    }

    default Iterable<VoxelShape> getBlockCollisions(@Nullable Entity entity0, AABB aABB1) {
        return () -> new BlockCollisions(this, entity0, aABB1, false, (p_286215_, p_286216_) -> p_286216_);
    }

    @Nullable
    private VoxelShape borderCollision(Entity entity0, AABB aABB1) {
        WorldBorder $$2 = this.getWorldBorder();
        return $$2.isInsideCloseToBorder(entity0, aABB1) ? $$2.getCollisionShape() : null;
    }

    default boolean collidesWithSuffocatingBlock(@Nullable Entity entity0, AABB aABB1) {
        BlockCollisions<VoxelShape> $$2 = new BlockCollisions<>(this, entity0, aABB1, true, (p_286211_, p_286212_) -> p_286212_);
        while ($$2.hasNext()) {
            if (!((VoxelShape) $$2.next()).isEmpty()) {
                return true;
            }
        }
        return false;
    }

    default Optional<BlockPos> findSupportingBlock(Entity entity0, AABB aABB1) {
        BlockPos $$2 = null;
        double $$3 = Double.MAX_VALUE;
        BlockCollisions<BlockPos> $$4 = new BlockCollisions<>(this, entity0, aABB1, false, (p_286213_, p_286214_) -> p_286213_);
        while ($$4.hasNext()) {
            BlockPos $$5 = (BlockPos) $$4.next();
            double $$6 = $$5.m_203193_(entity0.position());
            if ($$6 < $$3 || $$6 == $$3 && ($$2 == null || $$2.compareTo($$5) < 0)) {
                $$2 = $$5.immutable();
                $$3 = $$6;
            }
        }
        return Optional.ofNullable($$2);
    }

    default Optional<Vec3> findFreePosition(@Nullable Entity entity0, VoxelShape voxelShape1, Vec3 vec2, double double3, double double4, double double5) {
        if (voxelShape1.isEmpty()) {
            return Optional.empty();
        } else {
            AABB $$6 = voxelShape1.bounds().inflate(double3, double4, double5);
            VoxelShape $$7 = (VoxelShape) StreamSupport.stream(this.getBlockCollisions(entity0, $$6).spliterator(), false).filter(p_186430_ -> this.getWorldBorder() == null || this.getWorldBorder().isWithinBounds(p_186430_.bounds())).flatMap(p_186426_ -> p_186426_.toAabbs().stream()).map(p_186424_ -> p_186424_.inflate(double3 / 2.0, double4 / 2.0, double5 / 2.0)).map(Shapes::m_83064_).reduce(Shapes.empty(), Shapes::m_83110_);
            VoxelShape $$8 = Shapes.join(voxelShape1, $$7, BooleanOp.ONLY_FIRST);
            return $$8.closestPointTo(vec2);
        }
    }
}