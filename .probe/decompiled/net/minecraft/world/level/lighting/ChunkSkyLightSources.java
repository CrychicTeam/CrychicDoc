package net.minecraft.world.level.lighting;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.SectionPos;
import net.minecraft.util.BitStorage;
import net.minecraft.util.Mth;
import net.minecraft.util.SimpleBitStorage;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelHeightAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraft.world.level.chunk.LevelChunkSection;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ChunkSkyLightSources {

    private static final int SIZE = 16;

    public static final int NEGATIVE_INFINITY = Integer.MIN_VALUE;

    private final int minY;

    private final BitStorage heightmap;

    private final BlockPos.MutableBlockPos mutablePos1 = new BlockPos.MutableBlockPos();

    private final BlockPos.MutableBlockPos mutablePos2 = new BlockPos.MutableBlockPos();

    public ChunkSkyLightSources(LevelHeightAccessor levelHeightAccessor0) {
        this.minY = levelHeightAccessor0.getMinBuildHeight() - 1;
        int $$1 = levelHeightAccessor0.getMaxBuildHeight();
        int $$2 = Mth.ceillog2($$1 - this.minY + 1);
        this.heightmap = new SimpleBitStorage($$2, 256);
    }

    public void fillFrom(ChunkAccess chunkAccess0) {
        int $$1 = chunkAccess0.getHighestFilledSectionIndex();
        if ($$1 == -1) {
            this.fill(this.minY);
        } else {
            for (int $$2 = 0; $$2 < 16; $$2++) {
                for (int $$3 = 0; $$3 < 16; $$3++) {
                    int $$4 = Math.max(this.findLowestSourceY(chunkAccess0, $$1, $$3, $$2), this.minY);
                    this.set(index($$3, $$2), $$4);
                }
            }
        }
    }

    private int findLowestSourceY(ChunkAccess chunkAccess0, int int1, int int2, int int3) {
        int $$4 = SectionPos.sectionToBlockCoord(chunkAccess0.m_151568_(int1) + 1);
        BlockPos.MutableBlockPos $$5 = this.mutablePos1.set(int2, $$4, int3);
        BlockPos.MutableBlockPos $$6 = this.mutablePos2.setWithOffset($$5, Direction.DOWN);
        BlockState $$7 = Blocks.AIR.defaultBlockState();
        for (int $$8 = int1; $$8 >= 0; $$8--) {
            LevelChunkSection $$9 = chunkAccess0.getSection($$8);
            if ($$9.hasOnlyAir()) {
                $$7 = Blocks.AIR.defaultBlockState();
                int $$10 = chunkAccess0.m_151568_($$8);
                $$5.setY(SectionPos.sectionToBlockCoord($$10));
                $$6.setY($$5.m_123342_() - 1);
            } else {
                for (int $$11 = 15; $$11 >= 0; $$11--) {
                    BlockState $$12 = $$9.getBlockState(int2, $$11, int3);
                    if (isEdgeOccluded(chunkAccess0, $$5, $$7, $$6, $$12)) {
                        return $$5.m_123342_();
                    }
                    $$7 = $$12;
                    $$5.set($$6);
                    $$6.move(Direction.DOWN);
                }
            }
        }
        return this.minY;
    }

    public boolean update(BlockGetter blockGetter0, int int1, int int2, int int3) {
        int $$4 = int2 + 1;
        int $$5 = index(int1, int3);
        int $$6 = this.get($$5);
        if ($$4 < $$6) {
            return false;
        } else {
            BlockPos $$7 = this.mutablePos1.set(int1, int2 + 1, int3);
            BlockState $$8 = blockGetter0.getBlockState($$7);
            BlockPos $$9 = this.mutablePos2.set(int1, int2, int3);
            BlockState $$10 = blockGetter0.getBlockState($$9);
            if (this.updateEdge(blockGetter0, $$5, $$6, $$7, $$8, $$9, $$10)) {
                return true;
            } else {
                BlockPos $$11 = this.mutablePos1.set(int1, int2 - 1, int3);
                BlockState $$12 = blockGetter0.getBlockState($$11);
                return this.updateEdge(blockGetter0, $$5, $$6, $$9, $$10, $$11, $$12);
            }
        }
    }

    private boolean updateEdge(BlockGetter blockGetter0, int int1, int int2, BlockPos blockPos3, BlockState blockState4, BlockPos blockPos5, BlockState blockState6) {
        int $$7 = blockPos3.m_123342_();
        if (isEdgeOccluded(blockGetter0, blockPos3, blockState4, blockPos5, blockState6)) {
            if ($$7 > int2) {
                this.set(int1, $$7);
                return true;
            }
        } else if ($$7 == int2) {
            this.set(int1, this.findLowestSourceBelow(blockGetter0, blockPos5, blockState6));
            return true;
        }
        return false;
    }

    private int findLowestSourceBelow(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2) {
        BlockPos.MutableBlockPos $$3 = this.mutablePos1.set(blockPos1);
        BlockPos.MutableBlockPos $$4 = this.mutablePos2.setWithOffset(blockPos1, Direction.DOWN);
        BlockState $$5 = blockState2;
        while ($$4.m_123342_() >= this.minY) {
            BlockState $$6 = blockGetter0.getBlockState($$4);
            if (isEdgeOccluded(blockGetter0, $$3, $$5, $$4, $$6)) {
                return $$3.m_123342_();
            }
            $$5 = $$6;
            $$3.set($$4);
            $$4.move(Direction.DOWN);
        }
        return this.minY;
    }

    private static boolean isEdgeOccluded(BlockGetter blockGetter0, BlockPos blockPos1, BlockState blockState2, BlockPos blockPos3, BlockState blockState4) {
        if (blockState4.m_60739_(blockGetter0, blockPos3) != 0) {
            return true;
        } else {
            VoxelShape $$5 = LightEngine.getOcclusionShape(blockGetter0, blockPos1, blockState2, Direction.DOWN);
            VoxelShape $$6 = LightEngine.getOcclusionShape(blockGetter0, blockPos3, blockState4, Direction.UP);
            return Shapes.faceShapeOccludes($$5, $$6);
        }
    }

    public int getLowestSourceY(int int0, int int1) {
        int $$2 = this.get(index(int0, int1));
        return this.extendSourcesBelowWorld($$2);
    }

    public int getHighestLowestSourceY() {
        int $$0 = Integer.MIN_VALUE;
        for (int $$1 = 0; $$1 < this.heightmap.getSize(); $$1++) {
            int $$2 = this.heightmap.get($$1);
            if ($$2 > $$0) {
                $$0 = $$2;
            }
        }
        return this.extendSourcesBelowWorld($$0 + this.minY);
    }

    private void fill(int int0) {
        int $$1 = int0 - this.minY;
        for (int $$2 = 0; $$2 < this.heightmap.getSize(); $$2++) {
            this.heightmap.set($$2, $$1);
        }
    }

    private void set(int int0, int int1) {
        this.heightmap.set(int0, int1 - this.minY);
    }

    private int get(int int0) {
        return this.heightmap.get(int0) + this.minY;
    }

    private int extendSourcesBelowWorld(int int0) {
        return int0 == this.minY ? Integer.MIN_VALUE : int0;
    }

    private static int index(int int0, int int1) {
        return int0 + int1 * 16;
    }
}