package dev.shadowsoffire.placebo.util;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

@Deprecated
public interface IReplacementBlock {

    void _setDefaultState(BlockState var1);

    void setStateContainer(StateDefinition<Block, BlockState> var1);
}