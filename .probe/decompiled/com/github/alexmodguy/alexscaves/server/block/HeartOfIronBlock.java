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

public class HeartOfIronBlock extends RotatedPillarBlock implements SimpleWaterloggedBlock {

    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;

    private static final VoxelShape SHAPE_X = ACMath.buildShape(Block.box(5.0, 0.0, 0.0, 11.0, 4.0, 16.0), Block.box(5.0, 12.0, 0.0, 11.0, 16.0, 16.0), Block.box(5.0, 4.0, 0.0, 11.0, 12.0, 4.0), Block.box(5.0, 4.0, 12.0, 11.0, 12.0, 16.0));

    private static final VoxelShape SHAPE_Y = ACMath.buildShape(Block.box(0.0, 5.0, 0.0, 16.0, 11.0, 4.0), Block.box(0.0, 5.0, 12.0, 16.0, 11.0, 16.0), Block.box(0.0, 5.0, 4.0, 4.0, 11.0, 12.0), Block.box(12.0, 5.0, 4.0, 16.0, 11.0, 12.0));

    private static final VoxelShape SHAPE_Z = ACMath.buildShape(Block.box(0.0, 0.0, 5.0, 16.0, 4.0, 11.0), Block.box(0.0, 12.0, 5.0, 16.0, 16.0, 11.0), Block.box(0.0, 4.0, 5.0, 4.0, 12.0, 11.0), Block.box(12.0, 4.0, 5.0, 16.0, 12.0, 11.0));

    public HeartOfIronBlock() {
        super(BlockBehaviour.Properties.of().mapColor(MapColor.RAW_IRON).requiresCorrectToolForDrops().strength(4.0F).sound(SoundType.METAL).noOcclusion());
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
        return (BlockState) ((BlockState) this.m_49966_().m_61124_(f_55923_, context.getNearestLookingDirection().getAxis())).m_61124_(WATERLOGGED, levelaccessor.m_6425_(blockpos).getType() == Fluids.WATER);
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