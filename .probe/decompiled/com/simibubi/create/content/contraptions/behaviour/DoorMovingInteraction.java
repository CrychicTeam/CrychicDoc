package com.simibubi.create.content.contraptions.behaviour;

import com.simibubi.create.content.contraptions.Contraption;
import com.simibubi.create.content.decoration.slidingDoor.SlidingDoorBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.DoorBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DoorHingeSide;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class DoorMovingInteraction extends SimpleBlockMovingInteraction {

    @Override
    protected BlockState handle(Player player, Contraption contraption, BlockPos pos, BlockState currentState) {
        if (!(currentState.m_60734_() instanceof DoorBlock)) {
            return currentState;
        } else {
            boolean trainDoor = currentState.m_60734_() instanceof SlidingDoorBlock;
            SoundEvent sound = currentState.m_61143_(DoorBlock.OPEN) ? (trainDoor ? null : SoundEvents.WOODEN_DOOR_CLOSE) : (trainDoor ? SoundEvents.IRON_DOOR_OPEN : SoundEvents.WOODEN_DOOR_OPEN);
            BlockPos otherPos = currentState.m_61143_(DoorBlock.HALF) == DoubleBlockHalf.LOWER ? pos.above() : pos.below();
            StructureTemplate.StructureBlockInfo info = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(otherPos);
            if (info.state().m_61138_(DoorBlock.OPEN)) {
                BlockState newState = (BlockState) info.state().m_61122_(DoorBlock.OPEN);
                this.setContraptionBlockData(contraption.entity, otherPos, new StructureTemplate.StructureBlockInfo(info.pos(), newState, info.nbt()));
            }
            currentState = (BlockState) currentState.m_61122_(DoorBlock.OPEN);
            if (player != null) {
                if (trainDoor) {
                    DoorHingeSide hinge = (DoorHingeSide) currentState.m_61143_(SlidingDoorBlock.f_52728_);
                    Direction facing = (Direction) currentState.m_61143_(SlidingDoorBlock.f_52726_);
                    BlockPos doublePos = pos.relative(hinge == DoorHingeSide.LEFT ? facing.getClockWise() : facing.getCounterClockWise());
                    StructureTemplate.StructureBlockInfo doubleInfo = (StructureTemplate.StructureBlockInfo) contraption.getBlocks().get(doublePos);
                    if (doubleInfo != null && SlidingDoorBlock.isDoubleDoor(currentState, hinge, facing, doubleInfo.state())) {
                        this.handlePlayerInteraction(null, InteractionHand.MAIN_HAND, doublePos, contraption.entity);
                    }
                }
                float pitch = player.m_9236_().random.nextFloat() * 0.1F + 0.9F;
                if (sound != null) {
                    this.playSound(player, sound, pitch);
                }
            }
            return currentState;
        }
    }

    @Override
    protected boolean updateColliders() {
        return true;
    }
}