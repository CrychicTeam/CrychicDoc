package net.minecraft.world.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WallHangingSignBlock;
import net.minecraft.world.level.block.state.BlockState;

public class HangingSignItem extends SignItem {

    public HangingSignItem(Block block0, Block block1, Item.Properties itemProperties2) {
        super(itemProperties2, block0, block1, Direction.UP);
    }

    @Override
    protected boolean canPlace(LevelReader levelReader0, BlockState blockState1, BlockPos blockPos2) {
        if (blockState1.m_60734_() instanceof WallHangingSignBlock $$3 && !$$3.canPlace(blockState1, levelReader0, blockPos2)) {
            return false;
        }
        return super.m_246210_(levelReader0, blockState1, blockPos2);
    }
}