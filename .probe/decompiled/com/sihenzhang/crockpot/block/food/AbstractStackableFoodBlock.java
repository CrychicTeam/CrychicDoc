package com.sihenzhang.crockpot.block.food;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.IntegerProperty;

public abstract class AbstractStackableFoodBlock extends CrockPotFoodBlock {

    public AbstractStackableFoodBlock() {
        this(BlockBehaviour.Properties.of());
    }

    public AbstractStackableFoodBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(f_54117_, Direction.NORTH)).m_61124_(this.getStacksProperty(), 1));
    }

    public abstract int getMaxStacks();

    public abstract IntegerProperty getStacksProperty();

    @Override
    public boolean canBeReplaced(BlockState pState, BlockPlaceContext pUseContext) {
        return !pUseContext.m_7078_() && pUseContext.m_43722_().getItem() == this.m_5456_() && pState.m_60713_(this) ? (Integer) pState.m_61143_(this.getStacksProperty()) < this.getMaxStacks() : super.m_6864_(pState, pUseContext);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState clickedState = pContext.m_43725_().getBlockState(pContext.getClickedPos());
        return clickedState.m_60713_(this) ? (BlockState) clickedState.m_61122_(this.getStacksProperty()) : super.getStateForPlacement(pContext);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(this.getStacksProperty());
    }
}