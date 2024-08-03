package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.AutomaticHammerBlockEntity;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.SimpleWaterloggedBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class AutomaticHammerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public static final VoxelShape HAMMER_NORTH_AABB = Shapes.or(Block.box(0.0, 0.0, 12.0, 16.0, 16.0, 16.0), Block.box(6.0, 6.0, 2.0, 10.0, 10.0, 6.0), Block.box(4.0, 4.0, 10.0, 12.0, 12.0, 12.0), Block.box(5.0, 5.0, 6.0, 11.0, 11.0, 10.0), Block.box(2.0, 9.0, 8.0, 7.0, 14.0, 12.0), Block.box(9.0, 9.0, 8.0, 14.0, 14.0, 12.0), Block.box(2.0, 2.0, 8.0, 7.0, 7.0, 12.0), Block.box(9.0, 2.0, 8.0, 14.0, 7.0, 12.0));

    public static final VoxelShape HAMMER_EAST_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 4.0, 16.0, 16.0), Block.box(10.0, 6.0, 6.0, 14.0, 10.0, 10.0), Block.box(4.0, 4.0, 4.0, 6.0, 12.0, 12.0), Block.box(6.0, 5.0, 5.0, 10.0, 11.0, 11.0), Block.box(4.0, 9.0, 2.0, 8.0, 14.0, 7.0), Block.box(4.0, 9.0, 9.0, 8.0, 14.0, 14.0), Block.box(4.0, 2.0, 2.0, 8.0, 7.0, 7.0), Block.box(4.0, 2.0, 9.0, 8.0, 7.0, 14.0));

    public static final VoxelShape HAMMER_SOUTH_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 16.0, 4.0), Block.box(6.0, 6.0, 10.0, 10.0, 10.0, 14.0), Block.box(4.0, 4.0, 4.0, 12.0, 12.0, 6.0), Block.box(5.0, 5.0, 6.0, 11.0, 11.0, 10.0), Block.box(9.0, 9.0, 4.0, 14.0, 14.0, 8.0), Block.box(2.0, 9.0, 4.0, 7.0, 14.0, 8.0), Block.box(9.0, 2.0, 4.0, 14.0, 7.0, 8.0), Block.box(2.0, 2.0, 4.0, 7.0, 7.0, 8.0));

    public static final VoxelShape HAMMER_WEST_AABB = Shapes.or(Block.box(12.0, 0.0, 0.0, 16.0, 16.0, 16.0), Block.box(2.0, 6.0, 6.0, 6.0, 10.0, 10.0), Block.box(10.0, 4.0, 4.0, 12.0, 12.0, 12.0), Block.box(6.0, 5.0, 5.0, 10.0, 11.0, 11.0), Block.box(8.0, 9.0, 9.0, 12.0, 14.0, 14.0), Block.box(8.0, 9.0, 2.0, 12.0, 14.0, 7.0), Block.box(8.0, 2.0, 9.0, 12.0, 7.0, 14.0), Block.box(8.0, 2.0, 2.0, 12.0, 7.0, 7.0));

    public AutomaticHammerBlock(BlockBehaviour.Properties properties) {
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
                return HAMMER_EAST_AABB;
            case WEST:
                return HAMMER_WEST_AABB;
            case SOUTH:
                return HAMMER_SOUTH_AABB;
            case NORTH:
            default:
                return HAMMER_NORTH_AABB;
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.AUTOMATIC_HAMMER_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.AUTOMATIC_HAMMER_ENTITY.get(), AutomaticHammerBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.AUTOMATIC_HAMMER_ENTITY.get(), AutomaticHammerBlockEntity::serverTick);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        Direction direction;
        if (pContext.m_43719_().getAxis() != Direction.Axis.Y) {
            direction = pContext.m_43719_();
        } else {
            direction = pContext.m_8125_().getOpposite();
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