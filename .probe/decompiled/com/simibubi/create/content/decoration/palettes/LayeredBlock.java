package com.simibubi.create.content.decoration.palettes;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;

public class LayeredBlock extends RotatedPillarBlock {

    public LayeredBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState stateForPlacement = super.getStateForPlacement(pContext);
        BlockState placedOn = pContext.m_43725_().getBlockState(pContext.getClickedPos().relative(pContext.m_43719_().getOpposite()));
        if (placedOn.m_60734_() == this && (pContext.m_43723_() == null || !pContext.m_43723_().m_6144_())) {
            stateForPlacement = (BlockState) stateForPlacement.m_61124_(f_55923_, (Direction.Axis) placedOn.m_61143_(f_55923_));
        }
        return stateForPlacement;
    }
}