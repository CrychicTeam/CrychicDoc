package com.github.alexthe666.citadel.server.entity.collision;

import java.util.Objects;
import java.util.Spliterators.AbstractSpliterator;
import java.util.function.BiPredicate;
import java.util.function.Consumer;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CitadelVoxelShapeSpliterator extends AbstractSpliterator<VoxelShape> {

    @Nullable
    private final Entity entity;

    private final AABB aabb;

    private final CollisionContext context;

    private final Cursor3D cubeCoordinateIterator;

    private final BlockPos.MutableBlockPos mutablePos;

    private final VoxelShape shape;

    private final CollisionGetter reader;

    private final BiPredicate<BlockState, BlockPos> statePositionPredicate;

    private boolean needsBorderCheck;

    public CitadelVoxelShapeSpliterator(CollisionGetter reader, @Nullable Entity entity, AABB aabb) {
        this(reader, entity, aabb, (p_241459_0_, p_241459_1_) -> true);
    }

    public CitadelVoxelShapeSpliterator(CollisionGetter reader, @Nullable Entity entity, AABB aabb, BiPredicate<BlockState, BlockPos> statePositionPredicate) {
        super(Long.MAX_VALUE, 1280);
        this.context = entity == null ? CollisionContext.empty() : CollisionContext.of(entity);
        this.mutablePos = new BlockPos.MutableBlockPos();
        this.shape = Shapes.create(aabb);
        this.reader = reader;
        this.needsBorderCheck = entity != null;
        this.entity = entity;
        this.aabb = aabb;
        this.statePositionPredicate = statePositionPredicate;
        int i = Mth.floor(aabb.minX - 1.0E-7) - 1;
        int j = Mth.floor(aabb.maxX + 1.0E-7) + 1;
        int k = Mth.floor(aabb.minY - 1.0E-7) - 1;
        int l = Mth.floor(aabb.maxY + 1.0E-7) + 1;
        int i1 = Mth.floor(aabb.minZ - 1.0E-7) - 1;
        int j1 = Mth.floor(aabb.maxZ + 1.0E-7) + 1;
        this.cubeCoordinateIterator = new Cursor3D(i, k, i1, j, l, j1);
    }

    private static boolean isCloseToBorder(VoxelShape p_241460_0_, AABB p_241460_1_) {
        return Shapes.joinIsNotEmpty(p_241460_0_, Shapes.create(p_241460_1_.inflate(1.0E-7)), BooleanOp.AND);
    }

    private static boolean isOutsideBorder(VoxelShape p_241461_0_, AABB p_241461_1_) {
        return Shapes.joinIsNotEmpty(p_241461_0_, Shapes.create(p_241461_1_.deflate(1.0E-7)), BooleanOp.AND);
    }

    public static boolean isBoxFullyWithinWorldBorder(WorldBorder p_234877_0_, AABB p_234877_1_) {
        double d0 = (double) Mth.floor(p_234877_0_.getMinX());
        double d1 = (double) Mth.floor(p_234877_0_.getMinZ());
        double d2 = (double) Mth.ceil(p_234877_0_.getMaxX());
        double d3 = (double) Mth.ceil(p_234877_0_.getMaxZ());
        return p_234877_1_.minX > d0 && p_234877_1_.minX < d2 && p_234877_1_.minZ > d1 && p_234877_1_.minZ < d3 && p_234877_1_.maxX > d0 && p_234877_1_.maxX < d2 && p_234877_1_.maxZ > d1 && p_234877_1_.maxZ < d3;
    }

    public boolean tryAdvance(Consumer<? super VoxelShape> p_tryAdvance_1_) {
        return this.needsBorderCheck && this.worldBorderCheck(p_tryAdvance_1_) || this.collisionCheck(p_tryAdvance_1_);
    }

    boolean collisionCheck(Consumer<? super VoxelShape> p_234878_1_) {
        while (this.cubeCoordinateIterator.advance()) {
            int i = this.cubeCoordinateIterator.nextX();
            int j = this.cubeCoordinateIterator.nextY();
            int k = this.cubeCoordinateIterator.nextZ();
            int l = this.cubeCoordinateIterator.getNextType();
            if (l != 3) {
                BlockGetter iblockreader = this.getChunk(i, k);
                if (iblockreader != null) {
                    this.mutablePos.set(i, j, k);
                    BlockState blockstate = iblockreader.getBlockState(this.mutablePos);
                    if (this.statePositionPredicate.test(blockstate, this.mutablePos) && (l != 1 || blockstate.m_60779_()) && (l != 2 || blockstate.m_60734_() == Blocks.MOVING_PISTON)) {
                        VoxelShape voxelshape = blockstate.m_60742_(this.reader, this.mutablePos, this.context);
                        if (!(this.entity instanceof ICustomCollisions) || !((ICustomCollisions) this.entity).canPassThrough(this.mutablePos, blockstate, voxelshape)) {
                            if (voxelshape == Shapes.block()) {
                                if (this.aabb.intersects((double) i, (double) j, (double) k, (double) i + 1.0, (double) j + 1.0, (double) k + 1.0)) {
                                    p_234878_1_.accept(voxelshape.move((double) i, (double) j, (double) k));
                                    return true;
                                }
                            } else {
                                VoxelShape voxelshape1 = voxelshape.move((double) i, (double) j, (double) k);
                                if (Shapes.joinIsNotEmpty(voxelshape1, this.shape, BooleanOp.AND)) {
                                    p_234878_1_.accept(voxelshape1);
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    @Nullable
    private BlockGetter getChunk(int p_234876_1_, int p_234876_2_) {
        int i = p_234876_1_ >> 4;
        int j = p_234876_2_ >> 4;
        return this.reader.getChunkForCollisions(i, j);
    }

    boolean worldBorderCheck(Consumer<? super VoxelShape> p_234879_1_) {
        Objects.requireNonNull(this.entity);
        this.needsBorderCheck = false;
        WorldBorder worldborder = this.reader.getWorldBorder();
        AABB axisalignedbb = this.entity.getBoundingBox();
        if (!isBoxFullyWithinWorldBorder(worldborder, axisalignedbb)) {
            VoxelShape voxelshape = worldborder.getCollisionShape();
            if (!isOutsideBorder(voxelshape, axisalignedbb) && isCloseToBorder(voxelshape, axisalignedbb)) {
                p_234879_1_.accept(voxelshape);
                return true;
            }
        }
        return false;
    }
}