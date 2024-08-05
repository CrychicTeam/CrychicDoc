package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.EmberActivatorBottomBlockEntity;
import com.rekindled.embers.blockentity.EmberActivatorTopBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmberActivatorBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape BASE_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0));

    protected static final VoxelShape TOP_AABB = Shapes.or(Shapes.joinUnoptimized(Block.box(3.0, 4.0, 3.0, 13.0, 16.0, 13.0), Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0), BooleanOp.ONLY_FIRST), Block.box(2.0, 2.0, 2.0, 14.0, 4.0, 14.0), Block.box(4.0, 0.0, 4.0, 12.0, 2.0, 12.0));

    public EmberActivatorBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.EMBER_ACTIVATOR_BOTTOM_ENTITY.get().create(pPos, pState) : RegistryManager.EMBER_ACTIVATOR_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.EMBER_ACTIVATOR_BOTTOM_ENTITY.get(), EmberActivatorBottomBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.EMBER_ACTIVATOR_BOTTOM_ENTITY.get(), EmberActivatorBottomBlockEntity::serverTick);
        } else {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.EMBER_ACTIVATOR_TOP_ENTITY.get(), EmberActivatorTopBlockEntity::clientTick) : null;
        }
    }
}