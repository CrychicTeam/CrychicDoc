package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class HalfTransparentBlock extends Block {

    protected HalfTransparentBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean skipRendering(BlockState blockState0, BlockState blockState1, Direction direction2) {
        return blockState1.m_60713_(this) ? true : super.m_6104_(blockState0, blockState1, direction2);
    }
}