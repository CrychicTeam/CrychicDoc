package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class SandBlock extends FallingBlock {

    private final int dustColor;

    public SandBlock(int int0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(blockBehaviourProperties1);
        this.dustColor = int0;
    }

    @Override
    public int getDustColor(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return this.dustColor;
    }
}