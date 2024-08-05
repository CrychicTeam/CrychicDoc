package com.github.alexmodguy.alexscaves.server.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;

public class DoublePlantWithRotationBlock extends DoublePlantBlock implements BonemealableBlock {

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public DoublePlantWithRotationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(FACING, Direction.NORTH)).m_61124_(f_52858_, DoubleBlockHalf.LOWER));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader levelReader, BlockPos pos) {
        if (state.m_61143_(f_52858_) != DoubleBlockHalf.UPPER) {
            return super.canSurvive(state, levelReader, pos);
        } else {
            BlockState blockstate = levelReader.m_8055_(pos.below());
            return state.m_60734_() != this ? super.canSurvive(state, levelReader, pos) : blockstate.m_60713_(this) && blockstate.m_61143_(f_52858_) == DoubleBlockHalf.LOWER && blockstate.m_61143_(FACING) == state.m_61143_(FACING);
        }
    }

    @Override
    public BlockState rotate(BlockState state, Rotation rotation) {
        return (BlockState) state.m_61124_(FACING, rotation.rotate((Direction) state.m_61143_(FACING)));
    }

    @Override
    public BlockState mirror(BlockState state, Mirror mirror) {
        return state.m_60717_(mirror.getRotation((Direction) state.m_61143_(FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(FACING, f_52858_);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        BlockState prev = super.getStateForPlacement(context);
        return prev == null ? null : (BlockState) prev.m_61124_(FACING, context.m_8125_().getOpposite());
    }

    @Override
    public void setPlacedBy(Level level, BlockPos pos, BlockState state, LivingEntity livingEntity, ItemStack stack) {
        BlockPos blockpos = pos.above();
        level.setBlock(blockpos, m_182453_(level, blockpos, (BlockState) ((BlockState) this.m_49966_().m_61124_(f_52858_, DoubleBlockHalf.UPPER)).m_61124_(FACING, (Direction) state.m_61143_(FACING))), 3);
    }

    @Override
    public boolean isValidBonemealTarget(LevelReader level, BlockPos blockPos, BlockState blockState, boolean idk) {
        return true;
    }

    @Override
    public boolean isBonemealSuccess(Level level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        return true;
    }

    @Override
    public void performBonemeal(ServerLevel level, RandomSource randomSource, BlockPos blockPos, BlockState state) {
        m_49840_(level, blockPos, new ItemStack(this));
    }
}