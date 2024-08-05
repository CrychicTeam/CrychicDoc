package net.minecraft.world.level.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public class TrappedChestBlockEntity extends ChestBlockEntity {

    public TrappedChestBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        super(BlockEntityType.TRAPPED_CHEST, blockPos0, blockState1);
    }

    @Override
    protected void signalOpenCount(Level level0, BlockPos blockPos1, BlockState blockState2, int int3, int int4) {
        super.signalOpenCount(level0, blockPos1, blockState2, int3, int4);
        if (int3 != int4) {
            Block $$5 = blockState2.m_60734_();
            level0.updateNeighborsAt(blockPos1, $$5);
            level0.updateNeighborsAt(blockPos1.below(), $$5);
        }
    }
}