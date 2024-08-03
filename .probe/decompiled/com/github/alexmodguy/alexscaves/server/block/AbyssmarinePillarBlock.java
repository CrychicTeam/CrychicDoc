package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;

public class AbyssmarinePillarBlock extends RotatedPillarBlock implements ActivatedByAltar {

    public AbyssmarinePillarBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DISTANCE, 15)).m_61124_(f_55923_, Direction.Axis.Y)).m_61124_(ACTIVE, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        serverLevel.m_7731_(pos, this.updateDistance(state, serverLevel, pos), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        int i = ActivatedByAltar.getDistanceAt(state1) + 1;
        if (i != 1 || (Integer) state.m_61143_(DISTANCE) != i) {
            levelAccessor.scheduleTick(blockPos, this, 2);
        }
        return state;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.updateDistance(super.getStateForPlacement(context), context.m_43725_(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, ACTIVE, f_55923_);
    }
}