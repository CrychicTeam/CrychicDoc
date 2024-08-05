package net.minecraft.world.level;

import com.google.common.collect.AbstractIterator;
import java.util.function.BiFunction;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Cursor3D;
import net.minecraft.core.SectionPos;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class BlockCollisions<T> extends AbstractIterator<T> {

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

    private final BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> resultProvider;

    public BlockCollisions(CollisionGetter collisionGetter0, @Nullable Entity entity1, AABB aABB2, boolean boolean3, BiFunction<BlockPos.MutableBlockPos, VoxelShape, T> biFunctionBlockPosMutableBlockPosVoxelShapeT4) {
        this.context = entity1 == null ? CollisionContext.empty() : CollisionContext.of(entity1);
        this.pos = new BlockPos.MutableBlockPos();
        this.entityShape = Shapes.create(aABB2);
        this.collisionGetter = collisionGetter0;
        this.box = aABB2;
        this.onlySuffocatingBlocks = boolean3;
        this.resultProvider = biFunctionBlockPosMutableBlockPosVoxelShapeT4;
        int $$5 = Mth.floor(aABB2.minX - 1.0E-7) - 1;
        int $$6 = Mth.floor(aABB2.maxX + 1.0E-7) + 1;
        int $$7 = Mth.floor(aABB2.minY - 1.0E-7) - 1;
        int $$8 = Mth.floor(aABB2.maxY + 1.0E-7) + 1;
        int $$9 = Mth.floor(aABB2.minZ - 1.0E-7) - 1;
        int $$10 = Mth.floor(aABB2.maxZ + 1.0E-7) + 1;
        this.cursor = new Cursor3D($$5, $$7, $$9, $$6, $$8, $$10);
    }

    @Nullable
    private BlockGetter getChunk(int int0, int int1) {
        int $$2 = SectionPos.blockToSectionCoord(int0);
        int $$3 = SectionPos.blockToSectionCoord(int1);
        long $$4 = ChunkPos.asLong($$2, $$3);
        if (this.cachedBlockGetter != null && this.cachedBlockGetterPos == $$4) {
            return this.cachedBlockGetter;
        } else {
            BlockGetter $$5 = this.collisionGetter.getChunkForCollisions($$2, $$3);
            this.cachedBlockGetter = $$5;
            this.cachedBlockGetterPos = $$4;
            return $$5;
        }
    }

    protected T computeNext() {
        while (this.cursor.advance()) {
            int $$0 = this.cursor.nextX();
            int $$1 = this.cursor.nextY();
            int $$2 = this.cursor.nextZ();
            int $$3 = this.cursor.getNextType();
            if ($$3 != 3) {
                BlockGetter $$4 = this.getChunk($$0, $$2);
                if ($$4 != null) {
                    this.pos.set($$0, $$1, $$2);
                    BlockState $$5 = $$4.getBlockState(this.pos);
                    if ((!this.onlySuffocatingBlocks || $$5.m_60828_($$4, this.pos)) && ($$3 != 1 || $$5.m_60779_()) && ($$3 != 2 || $$5.m_60713_(Blocks.MOVING_PISTON))) {
                        VoxelShape $$6 = $$5.m_60742_(this.collisionGetter, this.pos, this.context);
                        if ($$6 == Shapes.block()) {
                            if (this.box.intersects((double) $$0, (double) $$1, (double) $$2, (double) $$0 + 1.0, (double) $$1 + 1.0, (double) $$2 + 1.0)) {
                                return (T) this.resultProvider.apply(this.pos, $$6.move((double) $$0, (double) $$1, (double) $$2));
                            }
                        } else {
                            VoxelShape $$7 = $$6.move((double) $$0, (double) $$1, (double) $$2);
                            if (!$$7.isEmpty() && Shapes.joinIsNotEmpty($$7, this.entityShape, BooleanOp.AND)) {
                                return (T) this.resultProvider.apply(this.pos, $$7);
                            }
                        }
                    }
                }
            }
        }
        return (T) this.endOfData();
    }
}