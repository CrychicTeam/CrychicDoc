package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.EmberFunnelBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class EmberFunnelBlock extends EmberReceiverBlock {

    protected static final VoxelShape UP_AABB = Shapes.or(m_49796_(4.0, 0.0, 4.0, 12.0, 4.0, 12.0), m_49796_(2.0, 4.0, 2.0, 14.0, 12.0, 14.0), m_49796_(0.0, 12.0, 0.0, 16.0, 16.0, 16.0));

    protected static final VoxelShape DOWN_AABB = Shapes.or(m_49796_(4.0, 4.0, 4.0, 12.0, 16.0, 12.0), m_49796_(2.0, 4.0, 2.0, 14.0, 12.0, 14.0), m_49796_(0.0, 0.0, 0.0, 16.0, 4.0, 16.0));

    protected static final VoxelShape NORTH_AABB = Shapes.or(m_49796_(4.0, 4.0, 4.0, 12.0, 12.0, 16.0), m_49796_(2.0, 2.0, 4.0, 14.0, 14.0, 12.0), m_49796_(0.0, 0.0, 0.0, 16.0, 16.0, 4.0));

    protected static final VoxelShape SOUTH_AABB = Shapes.or(m_49796_(4.0, 4.0, 0.0, 12.0, 12.0, 4.0), m_49796_(2.0, 2.0, 4.0, 14.0, 14.0, 12.0), m_49796_(0.0, 0.0, 12.0, 16.0, 16.0, 16.0));

    protected static final VoxelShape WEST_AABB = Shapes.or(m_49796_(4.0, 4.0, 4.0, 16.0, 12.0, 12.0), m_49796_(4.0, 2.0, 2.0, 12.0, 14.0, 14.0), m_49796_(0.0, 0.0, 0.0, 4.0, 16.0, 16.0));

    protected static final VoxelShape EAST_AABB = Shapes.or(m_49796_(0.0, 4.0, 4.0, 4.0, 12.0, 12.0), m_49796_(4.0, 2.0, 2.0, 12.0, 14.0, 14.0), m_49796_(12.0, 0.0, 0.0, 16.0, 16.0, 16.0));

    public EmberFunnelBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        switch((Direction) pState.m_61143_(FACING)) {
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
    public boolean canSurvive(BlockState pState, LevelReader pLevel, BlockPos pPos) {
        return true;
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.EMBER_FUNNEL_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : m_152132_(pBlockEntityType, RegistryManager.EMBER_FUNNEL_ENTITY.get(), EmberFunnelBlockEntity::serverTick);
    }
}