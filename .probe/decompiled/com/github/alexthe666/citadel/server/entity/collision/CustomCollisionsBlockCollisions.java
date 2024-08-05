package com.github.alexthe666.citadel.server.entity.collision;

import com.google.common.collect.AbstractIterator;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.CollisionGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.EntityCollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CustomCollisionsBlockCollisions extends AbstractIterator<VoxelShape> {

    private final AABB box;

    private final CollisionContext context;

    private final Cursor3D cursor;

    private final BlockPos.MutableBlockPos pos;

    private final VoxelShape entityShape;

    private final CollisionGetter collisionGetter;

    private final boolean onlySuffocatingBlocks;

    @Nullable
    private BlockGetter cachedBlockGetter;

    private long cachedBlockGetterPos;

    public CustomCollisionsBlockCollisions(CollisionGetter collisionGetter0, @Nullable Entity entity1, AABB aABB2) {
        this(collisionGetter0, entity1, aABB2, false);
    }

    public CustomCollisionsBlockCollisions(CollisionGetter collisionGetter0, @Nullable Entity entity1, AABB aABB2, boolean boolean3) {
        this.context = entity1 == null ? CollisionContext.empty() : CollisionContext.of(entity1);
        this.pos = new BlockPos.MutableBlockPos();
        this.entityShape = Shapes.create(aABB2);
        this.collisionGetter = collisionGetter0;
        this.box = aABB2;
        this.onlySuffocatingBlocks = boolean3;
        int i = Mth.floor(aABB2.minX - 1.0E-7) - 1;
        int j = Mth.floor(aABB2.maxX + 1.0E-7) + 1;
        int k = Mth.floor(aABB2.minY - 1.0E-7) - 1;
        int l = Mth.floor(aABB2.maxY + 1.0E-7) + 1;
        int i1 = Mth.floor(aABB2.minZ - 1.0E-7) - 1;
        int j1 = Mth.floor(aABB2.maxZ + 1.0E-7) + 1;
        this.cursor = new Cursor3D(i, k, i1, j, l, j1);
    }

    @Nullable
    private BlockGetter getChunk(int int0, int int1) {
        int i = SectionPos.blockToSectionCoord(int0);
        int j = SectionPos.blockToSectionCoord(int1);
        long k = ChunkPos.asLong(i, j);
        if (this.cachedBlockGetter != null && this.cachedBlockGetterPos == k) {
            return this.cachedBlockGetter;
        } else {
            BlockGetter blockgetter = this.collisionGetter.getChunkForCollisions(i, j);
            this.cachedBlockGetter = blockgetter;
            this.cachedBlockGetterPos = k;
            return blockgetter;
        }
    }

    protected VoxelShape computeNext() {
        while (this.cursor.advance()) {
            int i = this.cursor.nextX();
            int j = this.cursor.nextY();
            int k = this.cursor.nextZ();
            int l = this.cursor.getNextType();
            if (l != 3) {
                BlockGetter blockgetter = this.getChunk(i, k);
                if (blockgetter != null) {
                    this.pos.set(i, j, k);
                    BlockState blockstate = blockgetter.getBlockState(this.pos);
                    if ((!this.onlySuffocatingBlocks || blockstate.m_60828_(blockgetter, this.pos)) && (l != 1 || blockstate.m_60779_()) && (l != 2 || blockstate.m_60713_(Blocks.MOVING_PISTON))) {
                        VoxelShape voxelshape = blockstate.m_60742_(this.collisionGetter, this.pos, this.context);
                        if (this.context instanceof EntityCollisionContext) {
                            Entity entity = ((EntityCollisionContext) this.context).getEntity();
                            if (entity instanceof ICustomCollisions && ((ICustomCollisions) entity).canPassThrough(this.pos, blockstate, voxelshape)) {
                                continue;
                            }
                        }
                        if (voxelshape == Shapes.block()) {
                            if (this.box.intersects((double) i, (double) j, (double) k, (double) i + 1.0, (double) j + 1.0, (double) k + 1.0)) {
                                return voxelshape.move((double) i, (double) j, (double) k);
                            }
                        } else {
                            VoxelShape voxelshape1 = voxelshape.move((double) i, (double) j, (double) k);
                            if (Shapes.joinIsNotEmpty(voxelshape1, this.entityShape, BooleanOp.AND)) {
                                return voxelshape1;
                            }
                        }
                    }
                }
            }
        }
        return (VoxelShape) this.endOfData();
    }
}