package com.rekindled.embers.block;

import com.rekindled.embers.RegistryManager;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public class MirrorRelayBlock extends EmberReceiverBlock {

    protected static final VoxelShape UP_AABB = Shapes.or(m_49796_(5.0, 0.0, 5.0, 11.0, 1.0, 11.0), m_49796_(6.0, 1.0, 6.0, 10.0, 2.0, 10.0), Shapes.join(m_49796_(4.0, 0.0, 4.0, 12.0, 3.0, 12.0), Shapes.or(m_49796_(4.0, 0.0, 7.0, 12.0, 3.0, 9.0), m_49796_(7.0, 0.0, 4.0, 9.0, 3.0, 12.0)), BooleanOp.ONLY_FIRST));

    protected static final VoxelShape DOWN_AABB = Shapes.or(m_49796_(5.0, 15.0, 5.0, 11.0, 16.0, 11.0), m_49796_(6.0, 14.0, 6.0, 10.0, 15.0, 10.0), Shapes.join(m_49796_(4.0, 13.0, 4.0, 12.0, 16.0, 12.0), Shapes.or(m_49796_(4.0, 13.0, 7.0, 12.0, 16.0, 9.0), m_49796_(7.0, 13.0, 4.0, 9.0, 16.0, 12.0)), BooleanOp.ONLY_FIRST));

    protected static final VoxelShape NORTH_AABB = Shapes.or(m_49796_(5.0, 5.0, 15.0, 11.0, 11.0, 16.0), m_49796_(6.0, 6.0, 14.0, 10.0, 10.0, 15.0), Shapes.join(m_49796_(4.0, 4.0, 13.0, 12.0, 12.0, 16.0), Shapes.or(m_49796_(4.0, 7.0, 13.0, 12.0, 9.0, 16.0), m_49796_(7.0, 4.0, 13.0, 9.0, 12.0, 16.0)), BooleanOp.ONLY_FIRST));

    protected static final VoxelShape SOUTH_AABB = Shapes.or(m_49796_(5.0, 5.0, 0.0, 11.0, 11.0, 1.0), m_49796_(6.0, 6.0, 1.0, 10.0, 10.0, 2.0), Shapes.join(m_49796_(4.0, 4.0, 0.0, 12.0, 12.0, 3.0), Shapes.or(m_49796_(4.0, 7.0, 0.0, 12.0, 9.0, 3.0), m_49796_(7.0, 4.0, 0.0, 9.0, 12.0, 3.0)), BooleanOp.ONLY_FIRST));

    protected static final VoxelShape WEST_AABB = Shapes.or(m_49796_(15.0, 5.0, 5.0, 16.0, 11.0, 11.0), m_49796_(14.0, 6.0, 6.0, 15.0, 10.0, 10.0), Shapes.join(m_49796_(13.0, 4.0, 4.0, 16.0, 12.0, 12.0), Shapes.or(m_49796_(13.0, 4.0, 7.0, 16.0, 12.0, 9.0), m_49796_(13.0, 7.0, 4.0, 16.0, 9.0, 12.0)), BooleanOp.ONLY_FIRST));

    protected static final VoxelShape EAST_AABB = Shapes.or(m_49796_(0.0, 5.0, 5.0, 1.0, 11.0, 11.0), m_49796_(1.0, 6.0, 6.0, 2.0, 10.0, 10.0), Shapes.join(m_49796_(0.0, 4.0, 4.0, 3.0, 12.0, 12.0), Shapes.or(m_49796_(0.0, 4.0, 7.0, 3.0, 12.0, 9.0), m_49796_(0.0, 7.0, 4.0, 3.0, 9.0, 12.0)), BooleanOp.ONLY_FIRST));

    public MirrorRelayBlock(BlockBehaviour.Properties properties) {
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
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return RegistryManager.MIRROR_RELAY_ENTITY.get().create(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return null;
    }
}