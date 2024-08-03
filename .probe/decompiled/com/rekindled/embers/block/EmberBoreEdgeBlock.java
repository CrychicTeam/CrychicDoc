package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;

public class EmberBoreEdgeBlock extends MechEdgeBlockBase {

    public EmberBoreEdgeBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z));
    }

    @Override
    public Block getCenterBlock() {
        return RegistryManager.EMBER_BORE.get();
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }
}