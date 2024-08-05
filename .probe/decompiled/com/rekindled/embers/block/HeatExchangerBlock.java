package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class HeatExchangerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape RADIATOR_NORTH_AABB = Shapes.or(Block.box(4.0, 0.0, 2.0, 12.0, 13.0, 4.0), Block.box(4.0, 0.0, 10.0, 12.0, 13.0, 12.0), Block.box(9.0, 9.0, 0.0, 11.0, 11.0, 2.0), Block.box(9.0, 2.0, 0.0, 11.0, 4.0, 2.0), Block.box(5.0, 9.0, 0.0, 7.0, 11.0, 2.0), Block.box(5.0, 2.0, 0.0, 7.0, 4.0, 2.0), Block.box(5.0, 10.0, 12.0, 11.0, 12.0, 13.0), Block.box(5.0, 7.0, 12.0, 11.0, 9.0, 13.0), Block.box(5.0, 4.0, 12.0, 11.0, 6.0, 13.0), Block.box(5.0, 1.0, 12.0, 11.0, 3.0, 13.0), Block.box(5.0, 1.0, 4.0, 11.0, 12.0, 10.0));

    public static final VoxelShape RADIATOR_SOUTH_AABB = Shapes.or(Block.box(4.0, 0.0, 12.0, 12.0, 13.0, 14.0), Block.box(4.0, 0.0, 4.0, 12.0, 13.0, 6.0), Block.box(5.0, 9.0, 14.0, 7.0, 11.0, 16.0), Block.box(5.0, 2.0, 14.0, 7.0, 4.0, 16.0), Block.box(9.0, 9.0, 14.0, 11.0, 11.0, 16.0), Block.box(9.0, 2.0, 14.0, 11.0, 4.0, 16.0), Block.box(5.0, 10.0, 3.0, 11.0, 12.0, 4.0), Block.box(5.0, 7.0, 3.0, 11.0, 9.0, 4.0), Block.box(5.0, 4.0, 3.0, 11.0, 6.0, 4.0), Block.box(5.0, 1.0, 3.0, 11.0, 3.0, 4.0), Block.box(5.0, 1.0, 6.0, 11.0, 12.0, 12.0));

    public static final VoxelShape RADIATOR_WEST_AABB = Shapes.or(Block.box(2.0, 0.0, 4.0, 4.0, 13.0, 12.0), Block.box(10.0, 0.0, 4.0, 12.0, 13.0, 12.0), Block.box(0.0, 9.0, 5.0, 2.0, 11.0, 7.0), Block.box(0.0, 2.0, 5.0, 2.0, 4.0, 7.0), Block.box(0.0, 9.0, 9.0, 2.0, 11.0, 11.0), Block.box(0.0, 2.0, 9.0, 2.0, 4.0, 11.0), Block.box(12.0, 10.0, 5.0, 13.0, 12.0, 11.0), Block.box(12.0, 7.0, 5.0, 13.0, 9.0, 11.0), Block.box(12.0, 4.0, 5.0, 13.0, 6.0, 11.0), Block.box(12.0, 1.0, 5.0, 13.0, 3.0, 11.0), Block.box(4.0, 1.0, 5.0, 10.0, 12.0, 11.0));

    public static final VoxelShape RADIATOR_EAST_AABB = Shapes.or(Block.box(12.0, 0.0, 4.0, 14.0, 13.0, 12.0), Block.box(4.0, 0.0, 4.0, 6.0, 13.0, 12.0), Block.box(14.0, 9.0, 9.0, 16.0, 11.0, 11.0), Block.box(14.0, 2.0, 9.0, 16.0, 4.0, 11.0), Block.box(14.0, 9.0, 5.0, 16.0, 11.0, 7.0), Block.box(14.0, 2.0, 5.0, 16.0, 4.0, 7.0), Block.box(3.0, 10.0, 5.0, 4.0, 12.0, 11.0), Block.box(3.0, 7.0, 5.0, 4.0, 9.0, 11.0), Block.box(3.0, 4.0, 5.0, 4.0, 6.0, 11.0), Block.box(3.0, 1.0, 5.0, 4.0, 3.0, 11.0), Block.box(6.0, 1.0, 5.0, 12.0, 12.0, 11.0));

    public HeatExchangerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        switch((Direction) state.m_61143_(BlockStateProperties.HORIZONTAL_FACING)) {
            case EAST:
                return RADIATOR_EAST_AABB;
            case WEST:
                return RADIATOR_WEST_AABB;
            case SOUTH:
                return RADIATOR_SOUTH_AABB;
            case NORTH:
            default:
                return RADIATOR_NORTH_AABB;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.HEAT_EXCHANGER_ENTITY.get().create(pPos, pState);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction;
        if (pContext.m_43719_().getAxis() != Direction.Axis.Y) {
            direction = pContext.m_43719_().getOpposite();
        } else {
            direction = pContext.m_8125_();
        }
        return (BlockState) ((BlockState) super.m_5573_(pContext).m_61124_(BlockStateProperties.HORIZONTAL_FACING, direction)).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.WATERLOGGED, BlockStateProperties.HORIZONTAL_FACING);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}