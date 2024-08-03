package com.simibubi.create.content.equipment.bell;

import com.simibubi.create.AllShapes;
import com.simibubi.create.foundation.block.IBE;
import javax.annotation.Nullable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.stats.Stats;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BellBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BellAttachType;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

public abstract class AbstractBellBlock<BE extends AbstractBellBlockEntity> extends BellBlock implements IBE<BE> {

    public AbstractBellBlock(BlockBehaviour.Properties properties) {
        super(properties);
    }

    @Override
    public VoxelShape getShape(BlockState state, BlockGetter reader, BlockPos pos, CollisionContext selection) {
        Direction facing = (Direction) state.m_61143_(f_49679_);
        switch((BellAttachType) state.m_61143_(f_49680_)) {
            case CEILING:
                return AllShapes.BELL_CEILING.get(facing);
            case DOUBLE_WALL:
                return AllShapes.BELL_DOUBLE_WALL.get(facing);
            case FLOOR:
                return AllShapes.BELL_FLOOR.get(facing);
            case SINGLE_WALL:
                return AllShapes.BELL_WALL.get(facing);
            default:
                return Shapes.block();
        }
    }

    @Override
    public void neighborChanged(BlockState pState, Level pLevel, BlockPos pPos, Block pBlock, BlockPos pFromPos, boolean pIsMoving) {
        if (!pLevel.isClientSide) {
            boolean shouldPower = pLevel.m_276867_(pPos);
            if (shouldPower != (Boolean) pState.m_61143_(f_49681_)) {
                pLevel.setBlock(pPos, (BlockState) pState.m_61124_(f_49681_, shouldPower), 3);
                if (shouldPower) {
                    Direction facing = (Direction) pState.m_61143_(f_49679_);
                    BellAttachType type = (BellAttachType) pState.m_61143_(f_49680_);
                    this.ring(pLevel, pPos, type != BellAttachType.CEILING && type != BellAttachType.FLOOR ? facing.getClockWise() : facing, null);
                }
            }
        }
    }

    @Override
    public boolean onHit(Level world, BlockState state, BlockHitResult hit, @Nullable Player player, boolean flag) {
        BlockPos pos = hit.getBlockPos();
        Direction direction = hit.getDirection();
        if (direction == null) {
            direction = (Direction) world.getBlockState(pos).m_61143_(f_49679_);
        }
        return !this.canRingFrom(state, direction, hit.m_82450_().y - (double) pos.m_123342_()) ? false : this.ring(world, pos, direction, player);
    }

    protected boolean ring(Level world, BlockPos pos, Direction direction, Player player) {
        BE be = this.getBlockEntity(world, pos);
        if (world.isClientSide) {
            return true;
        } else if (be != null && be.ring(world, pos, direction)) {
            this.playSound(world, pos);
            if (player != null) {
                player.awardStat(Stats.BELL_RING);
            }
            return true;
        } else {
            return false;
        }
    }

    public boolean canRingFrom(BlockState state, Direction hitDir, double heightChange) {
        if (hitDir.getAxis() == Direction.Axis.Y) {
            return false;
        } else if (heightChange > 0.8124) {
            return false;
        } else {
            Direction direction = (Direction) state.m_61143_(f_49679_);
            BellAttachType bellAttachment = (BellAttachType) state.m_61143_(f_49680_);
            switch(bellAttachment) {
                case CEILING:
                case FLOOR:
                    return direction.getAxis() == hitDir.getAxis();
                case DOUBLE_WALL:
                case SINGLE_WALL:
                    return direction.getAxis() != hitDir.getAxis();
                default:
                    return false;
            }
        }
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos blockPos0, BlockState blockState1) {
        return IBE.super.newBlockEntity(blockPos0, blockState1);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level level0, BlockState blockState1, BlockEntityType<T> blockEntityTypeT2) {
        return IBE.super.getTicker(level0, blockState1, blockEntityTypeT2);
    }

    public abstract void playSound(Level var1, BlockPos var2);
}