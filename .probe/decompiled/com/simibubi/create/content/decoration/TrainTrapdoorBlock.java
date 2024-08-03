package com.simibubi.create.content.decoration;

import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import com.simibubi.create.content.equipment.wrench.IWrenchable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.TrapDoorBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockSetType;
import net.minecraft.world.level.block.state.properties.Half;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;

public class TrainTrapdoorBlock extends TrapDoorBlock implements IWrenchable {

    public TrainTrapdoorBlock(BlockBehaviour.Properties blockBehaviourProperties0) {
        super(blockBehaviourProperties0, (BlockSetType) SlidingDoorBlock.TRAIN_SET_TYPE.get());
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        pState = (BlockState) pState.m_61122_(f_57514_);
        pLevel.setBlock(pPos, pState, 2);
        if ((Boolean) pState.m_61143_(f_57517_)) {
            pLevel.m_186469_(pPos, Fluids.WATER, Fluids.WATER.m_6718_(pLevel));
        }
        this.m_57527_(pPlayer, pLevel, pPos, (Boolean) pState.m_61143_(f_57514_));
        return InteractionResult.sidedSuccess(pLevel.isClientSide);
    }

    @Override
    public boolean skipRendering(BlockState state, BlockState other, Direction pDirection) {
        return state.m_60713_(this) == other.m_60713_(this) && isConnected(state, other, pDirection);
    }

    public static boolean isConnected(BlockState state, BlockState other, Direction pDirection) {
        state = (BlockState) ((BlockState) state.m_61124_(f_57517_, false)).m_61124_(f_57516_, false);
        other = (BlockState) ((BlockState) other.m_61124_(f_57517_, false)).m_61124_(f_57516_, false);
        boolean open = (Boolean) state.m_61143_(f_57514_);
        Half half = (Half) state.m_61143_(f_57515_);
        Direction facing = (Direction) state.m_61143_(f_54117_);
        if (open != (Boolean) other.m_61143_(f_57514_)) {
            return false;
        } else if (!open && half == other.m_61143_(f_57515_)) {
            return pDirection.getAxis() != Direction.Axis.Y;
        } else if (!open && half != other.m_61143_(f_57515_) && pDirection.getAxis() == Direction.Axis.Y) {
            return true;
        } else if (open && facing.getOpposite() == other.m_61143_(f_54117_) && pDirection.getAxis() == facing.getAxis()) {
            return true;
        } else {
            return (open ? (BlockState) state.m_61124_(f_57515_, Half.TOP) : state) != (open ? (BlockState) other.m_61124_(f_57515_, Half.TOP) : other) ? false : pDirection.getAxis() != facing.getAxis();
        }
    }
}