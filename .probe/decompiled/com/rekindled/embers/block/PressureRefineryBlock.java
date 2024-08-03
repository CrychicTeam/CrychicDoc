package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.PressureRefineryBottomBlockEntity;
import com.rekindled.embers.blockentity.PressureRefineryTopBlockEntity;
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

public class PressureRefineryBlock extends DoubleTallMachineBlock {

    protected static final VoxelShape TOP_AABB = Shapes.or(Shapes.joinUnoptimized(Block.box(3.0, 4.0, 3.0, 13.0, 16.0, 13.0), Block.box(5.0, 5.0, 5.0, 11.0, 16.0, 11.0), BooleanOp.ONLY_FIRST), Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0));

    public PressureRefineryBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? Shapes.block() : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return (BlockEntity) (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER ? RegistryManager.PRESSURE_REFINERY_BOTTOM_ENTITY.get().create(pPos, pState) : RegistryManager.PRESSURE_REFINERY_TOP_ENTITY.get().create(pPos, pState));
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(BlockStateProperties.DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.PRESSURE_REFINERY_BOTTOM_ENTITY.get(), PressureRefineryBottomBlockEntity::clientTick) : m_152132_(pBlockEntityType, RegistryManager.PRESSURE_REFINERY_BOTTOM_ENTITY.get(), PressureRefineryBottomBlockEntity::serverTick);
        } else {
            return pLevel.isClientSide ? m_152132_(pBlockEntityType, RegistryManager.PRESSURE_REFINERY_TOP_ENTITY.get(), PressureRefineryTopBlockEntity::clientTick) : null;
        }
    }
}