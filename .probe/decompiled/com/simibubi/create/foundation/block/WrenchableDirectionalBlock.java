package com.simibubi.create.foundation.block;

import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.DirectionalBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class WrenchableDirectionalBlock extends DirectionalBlock implements IWrenchable {

    public WrenchableDirectionalBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(f_52588_);
        super.m_7926_(builder);
    }

    @Override
    public BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        Direction facing = (Direction) originalState.m_61143_(f_52588_);
        if (facing.getAxis() == targetedFace.getAxis()) {
            return originalState;
        } else {
            Direction newFacing = facing.getClockWise(targetedFace.getAxis());
            return (BlockState) originalState.m_61124_(f_52588_, newFacing);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return (BlockState) this.m_49966_().m_61124_(f_52588_, context.getNearestLookingDirection());
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rot) {
        return (BlockState) state.m_61124_(f_52588_, rot.rotate((Direction) state.m_61143_(f_52588_)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.m_60717_(mirrorIn.getRotation((Direction) state.m_61143_(f_52588_)));
    }
}