package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.VoxelShape;

public class WeepingVinesBlock extends GrowingPlantHeadBlock {

    protected static final VoxelShape SHAPE = Block.box(4.0, 9.0, 4.0, 12.0, 16.0, 12.0);

    public WeepingVinesBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, Direction.DOWN, SHAPE, false, 0.1);
    }

    @Override
    protected int getBlocksToGrowWhenBonemealed(RandomSource randomSource0) {
        return NetherVines.getBlocksToGrowWhenBonemealed(randomSource0);
    }

    @Override
    protected Block getBodyBlock() {
        return Blocks.WEEPING_VINES_PLANT;
    }

    @Override
    protected boolean canGrowInto(BlockState blockState0) {
        return NetherVines.isValidGrowthState(blockState0);
    }
}