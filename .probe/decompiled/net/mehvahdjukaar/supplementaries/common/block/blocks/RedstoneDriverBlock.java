package net.mehvahdjukaar.supplementaries.common.block.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DiodeBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class RedstoneDriverBlock extends DiodeBlock {

    public RedstoneDriverBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateBuilder) {
        stateBuilder.add(f_54117_, f_52496_);
    }

    @Override
    protected int getDelay(BlockState state) {
        return 0;
    }

    @Override
    protected int getInputSignal(Level world, BlockPos pos, BlockState state) {
        return super.getInputSignal(world, pos, state);
    }
}