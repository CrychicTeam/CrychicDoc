package com.github.alexmodguy.alexscaves.server.block;

import com.github.alexmodguy.alexscaves.server.misc.ACMath;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RotatedPillarBlock;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HollowBoneBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_X = ACMath.buildShape(Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 16.0, 14.0, 2.0), Block.box(0.0, 2.0, 14.0, 16.0, 14.0, 16.0));

    private static final VoxelShape SHAPE_Y = ACMath.buildShape(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 2.0), Block.box(0.0, 0.0, 14.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 2.0, 2.0, 16.0, 14.0), Block.box(14.0, 0.0, 2.0, 16.0, 16.0, 14.0));

    private static final VoxelShape SHAPE_Z = ACMath.buildShape(Block.box(0.0, 14.0, 0.0, 16.0, 16.0, 16.0), Block.box(0.0, 0.0, 0.0, 16.0, 2.0, 16.0), Block.box(0.0, 2.0, 0.0, 2.0, 14.0, 16.0), Block.box(14.0, 2.0, 0.0, 16.0, 14.0, 16.0));

    public HollowBoneBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.SAND).requiresCorrectToolForDrops().strength(2.0F).sound(SoundType.BONE_BLOCK).noOcclusion());
        this.m_49959_((BlockState) ((BlockState) this.m_49966_().m_61124_(WATERLOGGED, false)).m_61124_(f_55923_, Direction.Axis.Y));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter getter, BlockPos pos, CollisionContext context) {
        switch((Direction.Axis) state.m_61143_(f_55923_)) {
            case X:
                return SHAPE_X;
            case Y:
                return SHAPE_Y;
            case Z:
                return SHAPE_Z;
            default:
                return SHAPE_Y;
        }
    }

    @Override
    public BlockState updateShape(BlockState state, Direction direction, BlockState state1, LevelAccessor levelAccessor, BlockPos blockPos, BlockPos blockPos1) {
        if ((Boolean) state.m_61143_(WATERLOGGED)) {
            levelAccessor.scheduleTick(blockPos, Fluids.WATER, Fluids.WATER.m_6718_(levelAccessor));
        }
        return super.m_7417_(state, direction, state1, levelAccessor, blockPos, blockPos1);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        LevelAccessor levelaccessor = context.m_43725_();
        BlockPos blockpos = context.getClickedPos();
        return (BlockState) super.getStateForPlacement(context).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
    }

    @Override
    public FluidState getFluidState(BlockState state) {
        return state.m_61143_(WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(state);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> blockStateBuilder) {
        blockStateBuilder.add(WATERLOGGED, f_55923_);
    }
}