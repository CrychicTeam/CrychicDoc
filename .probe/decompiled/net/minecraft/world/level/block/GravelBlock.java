package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class GravelBlock extends FallingBlock {

    public GravelBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public int getDustColor(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return -8356741;
    }
}