package com.github.alexmodguy.alexscaves.server.block;

import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.StairBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.block.state.properties.StairsShape;

public class AbyssmarineStairBlock extends StairBlock implements ActivatedByAltar {

    public AbyssmarineStairBlock(BlockState state, BlockBehaviour.Properties properties) {
        super(state, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DISTANCE, 15)).m_61124_(f_56841_, Direction.NORTH)).m_61124_(f_56842_, Half.BOTTOM)).m_61124_(f_56843_, StairsShape.STRAIGHT)).m_61124_(f_56844_, false)).m_61124_(ACTIVE, false));
    }

    public AbyssmarineStairBlock(Supplier<BlockState> state, BlockBehaviour.Properties properties) {
        super(state, properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(DISTANCE, 15)).m_61124_(f_56841_, Direction.NORTH)).m_61124_(f_56842_, Half.BOTTOM)).m_61124_(f_56843_, StairsShape.STRAIGHT)).m_61124_(f_56844_, false)).m_61124_(ACTIVE, false));
    }

    @Override
    public void tick(BlockState state, ServerLevel serverLevel, BlockPos pos, RandomSource randomSource) {
        super.tick(state, serverLevel, pos, randomSource);
        serverLevel.m_7731_(pos, this.updateDistance(state, serverLevel, pos), 3);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        BlockState newState = super.updateShape(state, direction, state1, levelAccessor, blockPos, blockPos1);
        int i = ActivatedByAltar.getDistanceAt(state1) + 1;
        if (i != 1 || (Integer) newState.m_61143_(DISTANCE) != i) {
            levelAccessor.scheduleTick(blockPos, this, 2);
        }
        return newState;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        return this.updateDistance(super.getStateForPlacement(context), context.m_43725_(), context.getClickedPos());
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(DISTANCE, ACTIVE, f_56841_, f_56842_, f_56843_, f_56844_);
    }
}