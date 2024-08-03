package com.simibubi.create.content.logistics.funnel;

import com.simibubi.create.AllBlocks;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class BrassFunnelBlock extends FunnelBlock {

    public BrassFunnelBlock(BlockBehaviour.Properties p_i48415_1_) {
        super(p_i48415_1_);
    }

    @Override
    public BlockState getEquivalentBeltFunnel(BlockGetter world, BlockPos pos, BlockState state) {
        Direction facing = this.getFacing(state);
        return (BlockState) ((BlockState) AllBlocks.BRASS_BELT_FUNNEL.getDefaultState().m_61124_(BeltFunnelBlock.HORIZONTAL_FACING, facing)).m_61124_(POWERED, (Boolean) state.m_61143_(POWERED));
    }
}