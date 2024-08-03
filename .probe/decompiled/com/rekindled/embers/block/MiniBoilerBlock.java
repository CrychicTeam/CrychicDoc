package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.api.block.IPipeConnection;
import com.rekindled.embers.blockentity.MiniBoilerBlockEntity;
import com.rekindled.embers.blockentity.PipeBlockEntityBase;
import com.rekindled.embers.datagen.EmbersBlockTags;
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

public class MiniBoilerBlock extends BaseEntityBlock implements SimpleWaterloggedBlock, IPipeConnection {

    protected static final VoxelShape BOILER_AABB = Shapes.or(Block.box(3.0, 0.0, 3.0, 13.0, 16.0, 13.0), Block.box(2.0, 2.0, 2.0, 14.0, 14.0, 14.0));

    public MiniBoilerBlock(BlockBehaviour.Properties properties) {
        super(properties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return BOILER_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.MINI_BOILER_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.MINI_BOILER_ENTITY.get(), MiniBoilerBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.MINI_BOILER_ENTITY.get(), MiniBoilerBlockEntity::serverTick);
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
        if (pFacing.getAxis() != Direction.Axis.Y && pLevel.m_7702_(pCurrentPos) instanceof PipeBlockEntityBase pipe) {
            BlockEntity facingBE = pLevel.m_7702_(pFacingPos);
            if (pFacingState.m_204336_(EmbersBlockTags.FLUID_PIPE_CONNECTION)) {
                if (facingBE instanceof PipeBlockEntityBase && ((PipeBlockEntityBase) facingBE).getConnection(pFacing.getOpposite()) == PipeBlockEntityBase.PipeConnection.DISABLED) {
                    pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.NONE);
                } else {
                    pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.PIPE);
                }
            } else {
                pipe.setConnection(pFacing, PipeBlockEntityBase.PipeConnection.NONE);
            }
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

    @Override
    public PipeBlockEntityBase.PipeConnection getPipeConnection(BlockState state, Direction direction) {
        return direction.getAxis() == Direction.Axis.Y ? PipeBlockEntityBase.PipeConnection.END : PipeBlockEntityBase.PipeConnection.PIPE;
    }
}