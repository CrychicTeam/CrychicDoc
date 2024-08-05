package com.simibubi.create.content.redstone.contact;

import com.simibubi.create.AllBlocks;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.content.contraptions.elevator.ElevatorContraption;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.NbtUtils;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.ticks.TickPriority;

public class ContactMovementBehaviour implements MovementBehaviour {

    @Override
    public Vec3 getActiveAreaOffset(MovementContext context) {
        return Vec3.atLowerCornerOf(((Direction) context.state.m_61143_(RedstoneContactBlock.f_52588_)).getNormal()).scale(0.65F);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        BlockState block = context.state;
        Level world = context.world;
        if (!world.isClientSide) {
            if (!context.firstMovement) {
                this.deactivateLastVisitedContact(context);
                BlockState visitedState = world.getBlockState(pos);
                if (AllBlocks.REDSTONE_CONTACT.has(visitedState) || AllBlocks.ELEVATOR_CONTACT.has(visitedState)) {
                    Vec3 contact = Vec3.atLowerCornerOf(((Direction) block.m_61143_(RedstoneContactBlock.f_52588_)).getNormal());
                    contact = (Vec3) context.rotation.apply(contact);
                    Direction direction = Direction.getNearest(contact.x, contact.y, contact.z);
                    if (visitedState.m_61143_(RedstoneContactBlock.f_52588_) == direction.getOpposite()) {
                        if (AllBlocks.REDSTONE_CONTACT.has(visitedState)) {
                            world.setBlockAndUpdate(pos, (BlockState) visitedState.m_61124_(RedstoneContactBlock.POWERED, true));
                        }
                        if (AllBlocks.ELEVATOR_CONTACT.has(visitedState) && context.contraption instanceof ElevatorContraption ec) {
                            ec.broadcastFloorData(world, pos);
                        }
                        context.data.put("lastContact", NbtUtils.writeBlockPos(pos));
                    }
                }
            }
        }
    }

    @Override
    public void stopMoving(MovementContext context) {
        this.deactivateLastVisitedContact(context);
    }

    @Override
    public void cancelStall(MovementContext context) {
        MovementBehaviour.super.cancelStall(context);
        this.deactivateLastVisitedContact(context);
    }

    public void deactivateLastVisitedContact(MovementContext context) {
        if (context.data.contains("lastContact")) {
            BlockPos last = NbtUtils.readBlockPos(context.data.getCompound("lastContact"));
            context.data.remove("lastContact");
            BlockState blockState = context.world.getBlockState(last);
            if (AllBlocks.REDSTONE_CONTACT.has(blockState)) {
                context.world.m_186464_(last, (Block) AllBlocks.REDSTONE_CONTACT.get(), 1, TickPriority.NORMAL);
            }
        }
    }
}