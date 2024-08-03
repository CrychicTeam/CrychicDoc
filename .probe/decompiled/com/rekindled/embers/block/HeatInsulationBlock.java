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
import net.minecraft.world.level.block.Mirror;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.Rotation;
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

public class HeatInsulationBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    protected static final VoxelShape UP_AABB = Shapes.or(Block.box(2.0, 13.0, 2.0, 14.0, 15.0, 14.0), Block.box(0.0, 12.0, 0.0, 4.0, 16.0, 4.0), Block.box(12.0, 12.0, 0.0, 16.0, 16.0, 4.0), Block.box(12.0, 12.0, 12.0, 16.0, 16.0, 16.0), Block.box(0.0, 12.0, 12.0, 4.0, 16.0, 16.0), Block.box(1.0, 4.0, 1.0, 15.0, 13.0, 15.0), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0));

    protected static final VoxelShape DOWN_AABB = Shapes.or(Block.box(2.0, 1.0, 2.0, 14.0, 3.0, 14.0), Block.box(0.0, 0.0, 12.0, 4.0, 4.0, 16.0), Block.box(12.0, 0.0, 12.0, 16.0, 4.0, 16.0), Block.box(12.0, 0.0, 0.0, 16.0, 4.0, 4.0), Block.box(0.0, 0.0, 0.0, 4.0, 4.0, 4.0), Block.box(1.0, 3.0, 1.0, 15.0, 12.0, 15.0), Block.box(0.0, 12.0, 0.0, 16.0, 16.0, 16.0));

    protected static final VoxelShape NORTH_AABB = Shapes.or(Block.box(2.0, 2.0, 1.0, 14.0, 14.0, 3.0), Block.box(0.0, 0.0, 0.0, 4.0, 4.0, 4.0), Block.box(12.0, 0.0, 0.0, 16.0, 4.0, 4.0), Block.box(12.0, 12.0, 0.0, 16.0, 16.0, 4.0), Block.box(0.0, 12.0, 0.0, 4.0, 16.0, 4.0), Block.box(1.0, 1.0, 3.0, 15.0, 15.0, 12.0), Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0));

    protected static final VoxelShape SOUTH_AABB = Shapes.or(Block.box(2.0, 2.0, 13.0, 14.0, 14.0, 15.0), Block.box(12.0, 0.0, 12.0, 16.0, 4.0, 16.0), Block.box(0.0, 0.0, 12.0, 4.0, 4.0, 16.0), Block.box(0.0, 12.0, 12.0, 4.0, 16.0, 16.0), Block.box(12.0, 12.0, 12.0, 16.0, 16.0, 16.0), Block.box(1.0, 1.0, 4.0, 15.0, 15.0, 13.0), Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0));

    protected static final VoxelShape WEST_AABB = Shapes.or(Block.box(1.0, 2.0, 2.0, 3.0, 14.0, 14.0), Block.box(0.0, 0.0, 12.0, 4.0, 4.0, 16.0), Block.box(0.0, 0.0, 0.0, 4.0, 4.0, 4.0), Block.box(0.0, 12.0, 0.0, 4.0, 16.0, 4.0), Block.box(0.0, 12.0, 12.0, 4.0, 16.0, 16.0), Block.box(3.0, 1.0, 1.0, 12.0, 15.0, 15.0), Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0));

    protected static final VoxelShape EAST_AABB = Shapes.or(Block.box(13.0, 2.0, 2.0, 15.0, 14.0, 14.0), Block.box(12.0, 0.0, 0.0, 16.0, 4.0, 4.0), Block.box(12.0, 0.0, 12.0, 16.0, 4.0, 16.0), Block.box(12.0, 12.0, 12.0, 16.0, 16.0, 16.0), Block.box(12.0, 12.0, 0.0, 16.0, 16.0, 4.0), Block.box(4.0, 1.0, 1.0, 13.0, 15.0, 15.0), Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0));

    public HeatInsulationBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.FACING, Direction.UP)).m_61124_(BlockStateProperties.WATERLOGGED, false));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((Direction) pState.m_61143_(BlockStateProperties.FACING)) {
            case UP:
                return UP_AABB;
            case DOWN:
                return DOWN_AABB;
            case EAST:
                return EAST_AABB;
            case WEST:
                return WEST_AABB;
            case SOUTH:
                return SOUTH_AABB;
            case NORTH:
            default:
                return NORTH_AABB;
        }
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction[] var2 = pContext.getNearestLookingDirections();
        int var3 = var2.length;
        byte var4 = 0;
        if (var4 < var3) {
            Direction direction = var2[var4];
            BlockState blockstate = (BlockState) this.m_49966_().m_61124_(BlockStateProperties.FACING, direction.getOpposite());
            return (BlockState) blockstate.m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER);
        } else {
            return null;
        }
    }

    @Override
    public BlockState updateShape(BlockState pState, Direction pFacing, BlockState pFacingState, LevelAccessor pLevel, BlockPos pCurrentPos, BlockPos pFacingPos) {
        if ((Boolean) pState.m_61143_(BlockStateProperties.WATERLOGGED)) {
            pLevel.scheduleTick(pCurrentPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        return super.m_7417_(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState) pState.m_61124_(BlockStateProperties.FACING, pRot.rotate((Direction) pState.m_61143_(BlockStateProperties.FACING)));
    }

    @Override
    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.m_60717_(pMirror.getRotation((Direction) pState.m_61143_(BlockStateProperties.FACING)));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(BlockStateProperties.FACING).add(BlockStateProperties.WATERLOGGED);
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.HEAT_INSULATION_ENTITY.get().create(pPos, pState);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}