package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class TintedGlassBlock extends AbstractGlassBlock {

    public TintedGlassBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public boolean propagatesSkylightDown(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    @Override
    public int getLightBlock(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return blockGetter1.getMaxLightLevel();
    }
}