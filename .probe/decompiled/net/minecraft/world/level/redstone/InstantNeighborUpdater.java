package net.minecraft.world.level.redstone;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class InstantNeighborUpdater implements NeighborUpdater {

    private final Level level;

    public InstantNeighborUpdater(Level level0) {
        this.level = level0;
    }

    @Override
    public void shapeUpdate(Direction direction0, BlockState blockState1, BlockPos blockPos2, BlockPos blockPos3, int int4, int int5) {
        NeighborUpdater.executeShapeUpdate(this.level, direction0, blockState1, blockPos2, blockPos3, int4, int5 - 1);
    }

    @Override
    public void neighborChanged(BlockPos blockPos0, Block block1, BlockPos blockPos2) {
        BlockState $$3 = this.level.getBlockState(blockPos0);
        this.neighborChanged($$3, blockPos0, block1, blockPos2, false);
    }

    @Override
    public void neighborChanged(BlockState blockState0, BlockPos blockPos1, Block block2, BlockPos blockPos3, boolean boolean4) {
        NeighborUpdater.executeUpdate(this.level, blockState0, blockPos1, block2, blockPos3, boolean4);
    }
}