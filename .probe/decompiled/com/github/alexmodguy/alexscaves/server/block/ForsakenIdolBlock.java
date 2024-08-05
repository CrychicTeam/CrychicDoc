package com.github.alexmodguy.alexscaves.server.block;

import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.Rotation;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SupportType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class ForsakenIdolBlock extends Block implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    private static final VoxelShape SHAPE_NS = Block.box(3.0, 0.0, 2.0, 13.0, 16.0, 14.0);

    private static final VoxelShape SHAPE_EW = Block.box(2.0, 0.0, 3.0, 14.0, 16.0, 13.0);

    public ForsakenIdolBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(2.0F).sound(ACSoundTypes.BEHOLDER).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(FACING, Direction.NORTH));
    }

    @Override
    public boolean canSurvive(BlockState state, LevelReader level, BlockPos pos) {
        BlockPos blockpos = pos.below();
        BlockState state1 = level.m_8055_(blockpos);
        return state1.m_60659_(level, blockpos, Direction.UP, SupportType.CENTER);
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return direction == ((Direction) state.m_61143_(FACING)).getOpposite() && !state.m_60710_(levelAccessor, blockPos) ? Blocks.AIR.defaultBlockState() : super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return ((Direction) state.m_61143_(FACING)).getAxis() == Direction.Axis.X ? SHAPE_EW : SHAPE_NS;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER)).m_61124_(FACING, context.m_8125_().getOpposite());
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
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, FACING);
    }

    public PushReaction getPistonPushReaction(BlockState blockState) {
        return PushReaction.DESTROY;
    }
}