package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.EmberBoreBlockEntity;
import com.rekindled.embers.util.Misc;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;

public class EmberBoreBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {

    public EmberBoreBlock(BlockBehaviour.Properties pProperties) {
        super(pProperties);
        this.m_49959_((BlockState) ((BlockState) ((BlockState) this.f_49792_.any()).m_61124_(BlockStateProperties.WATERLOGGED, false)).m_61124_(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z));
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState state, Level level, BlockPos pos, BlockState newState, boolean isMoving) {
        if (!state.m_60713_(newState.m_60734_())) {
            for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
                BlockPos cornerPos = pos.subtract(edge.centerPos);
                if (level.getBlockState(cornerPos).m_60734_() instanceof MechEdgeBlockBase) {
                    level.m_46961_(cornerPos, false);
                }
            }
            BlockEntity blockEntity = level.getBlockEntity(pos);
            if (blockEntity != null) {
                IItemHandler handler = (IItemHandler) blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER, null).orElse(null);
                if (handler != null) {
                    Misc.spawnInventoryInWorld(level, (double) pos.m_123341_() + 0.5, (double) pos.m_123342_() + 0.5, (double) pos.m_123343_() + 0.5, handler);
                    level.updateNeighbourForOutputSignal(pos, this);
                }
            }
            super.m_6810_(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        boolean clear = true;
        for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
            if (!pContext.m_43725_().getBlockState(pContext.getClickedPos().subtract(edge.centerPos)).m_60629_(pContext)) {
                clear = false;
            }
        }
        return clear ? (BlockState) ((BlockState) super.m_5573_(pContext).m_61124_(BlockStateProperties.HORIZONTAL_AXIS, pContext.m_8125_().getAxis())).m_61124_(BlockStateProperties.WATERLOGGED, pContext.m_43725_().getFluidState(pContext.getClickedPos()).getType() == Fluids.WATER) : null;
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        Direction.Axis axis = (Direction.Axis) state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS);
        for (MechEdgeBlockBase.MechEdge edge : MechEdgeBlockBase.MechEdge.values()) {
            BlockState edgeState = (BlockState) RegistryManager.EMBER_BORE_EDGE.get().defaultBlockState().m_61124_(BlockStateProperties.WATERLOGGED, level.getFluidState(pos.subtract(edge.centerPos)).getType() == Fluids.WATER);
            level.setBlock(pos.subtract(edge.centerPos), (BlockState) ((BlockState) edgeState.m_61124_(MechEdgeBlockBase.EDGE, edge)).m_61124_(BlockStateProperties.HORIZONTAL_AXIS, axis), 3);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.EMBER_BORE_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.EMBER_BORE_ENTITY.get(), EmberBoreBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.EMBER_BORE_ENTITY.get(), EmberBoreBlockEntity::serverTick);
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
        pBuilder.add(BlockStateProperties.WATERLOGGED).add(BlockStateProperties.HORIZONTAL_AXIS);
    }

    @Override
    public FluidState getFluidState(BlockState pState) {
        return pState.m_61143_(BlockStateProperties.WATERLOGGED) ? Fluids.WATER.getSource(false) : super.m_5888_(pState);
    }
}