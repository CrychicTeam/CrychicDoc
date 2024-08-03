package com.github.alexthe666.alexsmobs.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.AbstractGlassBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.MapColor;

public class BlockRainbowGlass extends AbstractGlassBlock {

    public static final BooleanProperty UP = BooleanProperty.create("up");

    public static final BooleanProperty DOWN = BooleanProperty.create("down");

    public static final BooleanProperty EAST = BooleanProperty.create("east");

    public static final BooleanProperty WEST = BooleanProperty.create("west");

    public static final BooleanProperty NORTH = BooleanProperty.create("north");

    public static final BooleanProperty SOUTH = BooleanProperty.create("south");

    protected BlockRainbowGlass() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.COLOR_PURPLE).friction(0.97F).strength(0.2F).lightLevel(i -> 11).sound(SoundType.GLASS).noOcclusion().isValidSpawn(BlockRainbowGlass::noOption).isRedstoneConductor(BlockRainbowGlass::noOption).isSuffocating(BlockRainbowGlass::noOption).isViewBlocking(BlockRainbowGlass::noOption).emissiveRendering(BlockRainbowGlass::yes));
        this.m_49959_((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(UP, false)).m_61124_(DOWN, false)).m_61124_(EAST, false)).m_61124_(WEST, false)).m_61124_(NORTH, false)).m_61124_(SOUTH, false));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> stateDefinitionBuilderBlockBlockState0) {
        stateDefinitionBuilderBlockBlockState0.add(UP, DOWN, NORTH, SOUTH, EAST, WEST);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelReader levelreader = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        BlockPos n = blockpos.north();
        BlockPos e = blockpos.east();
        BlockPos s = blockpos.south();
        BlockPos w = blockpos.west();
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState northState = levelreader.m_8055_(n);
        BlockState eastState = levelreader.m_8055_(e);
        BlockState southState = levelreader.m_8055_(s);
        BlockState westState = levelreader.m_8055_(w);
        BlockState upState = levelreader.m_8055_(u);
        BlockState downState = levelreader.m_8055_(d);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) this.m_49966_().m_61124_(NORTH, northState.m_60713_(this))).m_61124_(NORTH, northState.m_60713_(this))).m_61124_(EAST, eastState.m_60713_(this))).m_61124_(SOUTH, southState.m_60713_(this))).m_61124_(WEST, westState.m_60713_(this))).m_61124_(UP, upState.m_60713_(this))).m_61124_(DOWN, downState.m_60713_(this));
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state2, LevelAccessor levelreader, BlockPos blockpos, BlockPos pos2) {
        BlockPos n = blockpos.north();
        BlockPos e = blockpos.east();
        BlockPos s = blockpos.south();
        BlockPos w = blockpos.west();
        BlockPos u = blockpos.above();
        BlockPos d = blockpos.below();
        BlockState northState = levelreader.m_8055_(n);
        BlockState eastState = levelreader.m_8055_(e);
        BlockState southState = levelreader.m_8055_(s);
        BlockState westState = levelreader.m_8055_(w);
        BlockState upState = levelreader.m_8055_(u);
        BlockState downState = levelreader.m_8055_(d);
        return (BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) ((BlockState) state.m_61124_(NORTH, northState.m_60713_(this))).m_61124_(NORTH, northState.m_60713_(this))).m_61124_(EAST, eastState.m_60713_(this))).m_61124_(SOUTH, southState.m_60713_(this))).m_61124_(WEST, westState.m_60713_(this))).m_61124_(UP, upState.m_60713_(this))).m_61124_(DOWN, downState.m_60713_(this));
    }

    private static Boolean noOption(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    private static Boolean yes(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2) {
        return false;
    }

    private static Boolean noOption(BlockState blockState0, BlockGetter blockGetter1, BlockPos blockPos2, EntityType<?> entityType3) {
        return false;
    }
}