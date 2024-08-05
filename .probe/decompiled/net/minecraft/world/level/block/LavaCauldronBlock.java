package net.minecraft.world.level.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.cauldron.CauldronInteraction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LavaCauldronBlock extends AbstractCauldronBlock {

    public LavaCauldronBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, CauldronInteraction.LAVA);
    }

    @Override
    protected double getContentHeight(BlockState blockState0) {
        return 0.9375;
    }

    @Override
    public boolean isFull(BlockState blockState0) {
        return true;
    }

    @Override
    public void entityInside(BlockState blockState0, Level level1, BlockPos blockPos2, Entity entity3) {
        if (this.m_151979_(blockState0, blockPos2, entity3)) {
            entity3.lavaHurt();
        }
    }

    @Override
    public int getAnalogOutputSignal(BlockState blockState0, Level level1, BlockPos blockPos2) {
        return 3;
    }
}