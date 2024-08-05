package com.simibubi.create.content.equipment.wrench;

import com.simibubi.create.AllSoundEvents;
import com.simibubi.create.Create;
import com.simibubi.create.content.kinetics.base.DirectionalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.DirectionalKineticBlock;
import com.simibubi.create.content.kinetics.base.GeneratingKineticBlockEntity;
import com.simibubi.create.content.kinetics.base.HorizontalAxisKineticBlock;
import com.simibubi.create.content.kinetics.base.HorizontalKineticBlock;
import com.simibubi.create.content.kinetics.base.KineticBlockEntity;
import com.simibubi.create.content.kinetics.base.RotatedPillarKineticBlock;
import com.simibubi.create.foundation.utility.VoxelShaper;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public interface IWrenchable {

    default InteractionResult onWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockState rotated = this.getRotatedBlockState(state, context.getClickedFace());
        if (!rotated.m_60710_(world, context.getClickedPos())) {
            return InteractionResult.PASS;
        } else {
            KineticBlockEntity.switchToBlockState(world, context.getClickedPos(), this.updateAfterWrenched(rotated, context));
            BlockEntity be = context.getLevel().getBlockEntity(context.getClickedPos());
            if (be instanceof GeneratingKineticBlockEntity) {
                ((GeneratingKineticBlockEntity) be).reActivateSource = true;
            }
            if (world.getBlockState(context.getClickedPos()) != state) {
                this.playRotateSound(world, context.getClickedPos());
            }
            return InteractionResult.SUCCESS;
        }
    }

    default BlockState updateAfterWrenched(BlockState newState, UseOnContext context) {
        return Block.updateFromNeighbourShapes(newState, context.getLevel(), context.getClickedPos());
    }

    default InteractionResult onSneakWrenched(BlockState state, UseOnContext context) {
        Level world = context.getLevel();
        BlockPos pos = context.getClickedPos();
        Player player = context.getPlayer();
        if (world instanceof ServerLevel) {
            if (player != null && !player.isCreative()) {
                Block.getDrops(state, (ServerLevel) world, pos, world.getBlockEntity(pos), player, context.getItemInHand()).forEach(itemStack -> player.getInventory().placeItemBackInInventory(itemStack));
            }
            state.m_222967_((ServerLevel) world, pos, ItemStack.EMPTY, true);
            world.m_46961_(pos, false);
            this.playRemoveSound(world, pos);
        }
        return InteractionResult.SUCCESS;
    }

    default void playRemoveSound(Level world, BlockPos pos) {
        AllSoundEvents.WRENCH_REMOVE.playOnServer(world, pos, 1.0F, Create.RANDOM.nextFloat() * 0.5F + 0.5F);
    }

    default void playRotateSound(Level world, BlockPos pos) {
        AllSoundEvents.WRENCH_ROTATE.playOnServer(world, pos, 1.0F, Create.RANDOM.nextFloat() + 0.5F);
    }

    default BlockState getRotatedBlockState(BlockState originalState, Direction targetedFace) {
        BlockState newState = originalState;
        if (targetedFace.getAxis() == Direction.Axis.Y) {
            if (originalState.m_61138_(HorizontalAxisKineticBlock.HORIZONTAL_AXIS)) {
                return (BlockState) originalState.m_61124_(HorizontalAxisKineticBlock.HORIZONTAL_AXIS, VoxelShaper.axisAsFace((Direction.Axis) originalState.m_61143_(HorizontalAxisKineticBlock.HORIZONTAL_AXIS)).getClockWise(targetedFace.getAxis()).getAxis());
            }
            if (originalState.m_61138_(HorizontalKineticBlock.HORIZONTAL_FACING)) {
                return (BlockState) originalState.m_61124_(HorizontalKineticBlock.HORIZONTAL_FACING, ((Direction) originalState.m_61143_(HorizontalKineticBlock.HORIZONTAL_FACING)).getClockWise(targetedFace.getAxis()));
            }
        }
        if (originalState.m_61138_(RotatedPillarKineticBlock.AXIS)) {
            return (BlockState) originalState.m_61124_(RotatedPillarKineticBlock.AXIS, VoxelShaper.axisAsFace((Direction.Axis) originalState.m_61143_(RotatedPillarKineticBlock.AXIS)).getClockWise(targetedFace.getAxis()).getAxis());
        } else if (!originalState.m_61138_(DirectionalKineticBlock.FACING)) {
            return originalState;
        } else {
            Direction stateFacing = (Direction) originalState.m_61143_(DirectionalKineticBlock.FACING);
            if (stateFacing.getAxis().equals(targetedFace.getAxis())) {
                return originalState.m_61138_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE) ? (BlockState) originalState.m_61122_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE) : originalState;
            } else {
                do {
                    newState = (BlockState) newState.m_61124_(DirectionalKineticBlock.FACING, ((Direction) newState.m_61143_(DirectionalKineticBlock.FACING)).getClockWise(targetedFace.getAxis()));
                    if (targetedFace.getAxis() == Direction.Axis.Y && newState.m_61138_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE)) {
                        newState = (BlockState) newState.m_61122_(DirectionalAxisKineticBlock.AXIS_ALONG_FIRST_COORDINATE);
                    }
                } while (((Direction) newState.m_61143_(DirectionalKineticBlock.FACING)).getAxis().equals(targetedFace.getAxis()));
                return newState;
            }
        }
    }
}