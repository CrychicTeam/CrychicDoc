package net.minecraft.world.level.block;

import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class InfestedRotatedPillarBlock extends InfestedBlock {

    public InfestedRotatedPillarBlock(Block block0, BlockBehaviour.Properties blockBehaviourProperties1) {
        super(block0, blockBehaviourProperties1);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(RotatedPillarBlock.AXIS, Direction.Axis.Y));
    }

    @Override
    public BlockState rotate(BlockState blockState0, Rotation rotation1) {
        return RotatedPillarBlock.rotatePillar(blockState0, rotation1);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(RotatedPillarBlock.AXIS);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext0) {
        return (BlockState) this.m_49966_().m_61124_(RotatedPillarBlock.AXIS, blockPlaceContext0.m_43719_().getAxis());
    }
}