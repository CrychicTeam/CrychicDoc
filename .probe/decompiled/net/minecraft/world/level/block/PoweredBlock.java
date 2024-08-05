package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class PoweredBlock extends Block {

    public PoweredBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean isSignalSource(BlockState blockState0) {
        return true;
    }

    @Override
    public int getSignal(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, Direction direction3) {
        return 15;
    }
}