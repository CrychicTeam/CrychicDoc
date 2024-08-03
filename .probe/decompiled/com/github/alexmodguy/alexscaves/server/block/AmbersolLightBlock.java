package com.github.alexmodguy.alexscaves.server.block;

import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AmbersolLightBlock extends Block {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public AmbersolLightBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(WATERLOGGED, false));
    }

    @Override
    public boolean propagatesSkylightDown(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return true;
    }

    @Override
    public float getShadeBrightness(BlockState state, BlockGetter getter, BlockPos blockPos) {
        return 1.0F;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(WATERLOGGED);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        return Shapes.empty();
    }

    @Override
    public RenderShape getRenderShape(BlockState blockState0) {
        return RenderShape.INVISIBLE;
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    public BlockPos getTopOfColumn(BlockPos current, LevelReader levelReader, Predicate<BlockState> predicate) {
        while (current.m_123342_() < levelReader.m_151558_() && predicate.test(levelReader.m_8055_(current))) {
            current = current.above();
        }
        return current;
    }

    public BlockPos getTopOfColumnLight(BlockPos current, LevelReader levelReader) {
        while (current.m_123342_() < levelReader.m_151558_() && testSkylight(levelReader, levelReader.m_8055_(current), current)) {
            current = current.above();
        }
        return current;
    }

    public static boolean testSkylight(LevelReader levelReader, BlockState blockState, BlockPos current) {
        return blockState.m_60631_(levelReader, current);
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos top = this.getTopOfColumnLight(pos, level);
        return level.m_8055_(top).m_60713_(ACBlockRegistry.AMBERSOL.get());
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        if (levelAccessor.m_8055_(blockPos.below()).m_60734_() != this) {
            BlockPos top = this.getTopOfColumn(blockPos, levelAccessor, state2 -> !state2.m_60713_(ACBlockRegistry.AMBERSOL.get()));
            levelAccessor.scheduleTick(new BlockPos(top), ACBlockRegistry.AMBERSOL.get(), 3);
        }
        return !state.m_60710_(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }
}