package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.blockentity.BeamCannonBlockEntity;
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

public class BeamCannonBlock extends EmberEmitterBlock {

    protected static final VoxelShape UP_AABB = Shapes.box(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);

    protected static final VoxelShape DOWN_AABB = Shapes.box(0.25, 0.0, 0.25, 0.75, 1.0, 0.75);

    protected static final VoxelShape NORTH_AABB = Shapes.box(0.25, 0.25, 0.0, 0.75, 0.75, 1.0);

    protected static final VoxelShape SOUTH_AABB = Shapes.box(0.25, 0.25, 0.0, 0.75, 0.75, 1.0);

    protected static final VoxelShape WEST_AABB = Shapes.box(0.0, 0.25, 0.25, 1.0, 0.75, 0.75);

    protected static final VoxelShape EAST_AABB = Shapes.box(0.0, 0.25, 0.25, 1.0, 0.75, 0.75);

    protected static final VoxelShape SUPPORT_UP = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0));

    protected static final VoxelShape SUPPORT_DOWN = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0));

    protected static final VoxelShape SUPPORT_NORTH = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0));

    protected static final VoxelShape SUPPORT_SOUTH = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1));

    protected static final VoxelShape SUPPORT_WEST = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.9, 0.0, 0.0, 1.0, 1.0, 1.0));

    protected static final VoxelShape SUPPORT_EAST = Shapes.or(Shapes.box(0.0, 0.0, 0.0, 1.0, 1.0, 0.1), Shapes.box(0.0, 0.0, 0.9, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 1.0, 0.1, 1.0), Shapes.box(0.0, 0.9, 0.0, 1.0, 1.0, 1.0), Shapes.box(0.0, 0.0, 0.0, 0.1, 1.0, 1.0));

    public BeamCannonBlock(BlockBehaviour.Properties properties) {
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
        return RegistryManager.BEAM_CANNON_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide ? null : m_152132_(pBlockEntityType, RegistryManager.BEAM_CANNON_ENTITY.get(), BeamCannonBlockEntity::serverTick);
    }

    @Override
    public VoxelShape getBlockSupportShape(BlockState pState, BlockGetter pLevel, BlockPos pPos) {
        switch((Direction) pState.m_61143_(FACING)) {
            case UP:
                return SUPPORT_UP;
            case DOWN:
                return SUPPORT_DOWN;
            case EAST:
                return SUPPORT_EAST;
            case WEST:
                return SUPPORT_WEST;
            case SOUTH:
                return SUPPORT_SOUTH;
            case NORTH:
            default:
                return SUPPORT_NORTH;
        }
    }
}