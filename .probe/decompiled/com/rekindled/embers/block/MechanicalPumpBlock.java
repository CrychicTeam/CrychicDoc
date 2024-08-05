package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.MechanicalPumpBottomBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MechanicalPumpBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape BOTTOM_Z_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0), Block.box(1.0, 9.0, 0.0, 15.0, 11.0, 16.0), Block.box(2.0, 11.0, 0.0, 14.0, 16.0, 16.0));

    protected static final VoxelShape BOTTOM_X_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 9.0, 16.0), Block.box(0.0, 9.0, 1.0, 16.0, 11.0, 15.0), Block.box(0.0, 11.0, 2.0, 16.0, 16.0, 14.0));

    protected static final VoxelShape TOP_AABB = Block.box(1.0, 0.0, 1.0, 15.0, 12.0, 15.0);

    public MechanicalPumpBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
        this.m_49959_((BlockState) this.m_49966_().m_61124_(BlockStateProperties.HORIZONTAL_AXIS, Direction.Axis.Z));
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? (state.m_61143_(BlockStateProperties.HORIZONTAL_AXIS) == Direction.Axis.Z ? BOTTOM_Z_AABB : BOTTOM_X_AABB) : TOP_AABB;
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        BlockState state = super.getStateForPlacement(pContext);
        return state == null ? null : (BlockState) state.m_61124_(BlockStateProperties.HORIZONTAL_AXIS, pContext.m_8125_().getAxis());
    }

    @Override
    public void onPlace(BlockState state, Level level, BlockPos pos, BlockState oldState, boolean isMoving) {
        if (state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            BlockState topState = (BlockState) state.m_61124_(BlockStateProperties.WATERLOGGED, level.getFluidState(pos.above()).getType() == Fluids.WATER);
            level.setBlock(pos.above(), (BlockState) topState.m_61124_(BlockStateProperties.DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), 3);
        }
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.MECHANICAL_PUMP_BOTTOM_ENTITY.get().create(pPos, pState) : RegistryManager.MECHANICAL_PUMP_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.MECHANICAL_PUMP_BOTTOM_ENTITY.get(), MechanicalPumpBottomBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.MECHANICAL_PUMP_BOTTOM_ENTITY.get(), MechanicalPumpBottomBlockEntity::serverTick);
        } else {
            return null;
        }
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        super.createBlockStateDefinition(pBuilder);
        pBuilder.add(BlockStateProperties.HORIZONTAL_AXIS);
    }
}