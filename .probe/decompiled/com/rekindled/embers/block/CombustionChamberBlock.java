package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.CombustionChamberBlockEntity;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CombustionChamberBlock extends ChamberBlockBase {

    protected static final VoxelShape BASE_AABB = Shapes.or(Block.box(0.0, 0.0, 0.0, 16.0, 4.0, 16.0), Block.box(1.0, 4.0, 1.0, 15.0, 16.0, 15.0));

    public CombustionChamberBlock(BlockBehaviour.Properties properties, SoundType topSound) {
        super(properties, topSound);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return state.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM ? BASE_AABB : TOP_AABB;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return pState.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM ? RegistryManager.COMBUSTION_CHAMBER_ENTITY.get().create(pPos, pState) : null;
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if (pState.m_61143_(CONNECTION) == ChamberBlockBase.ChamberConnection.BOTTOM) {
            return pLevel.isClientSide ? null : m_152132_(pBlockEntityType, RegistryManager.COMBUSTION_CHAMBER_ENTITY.get(), CombustionChamberBlockEntity::serverTick);
        } else {
            return null;
        }
    }
}