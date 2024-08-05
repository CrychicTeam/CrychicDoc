package com.simibubi.create.content.contraptions.actors.seat;

import com.simibubi.create.content.contraptions.AbstractContraptionEntity;
import com.simibubi.create.content.contraptions.behaviour.MovementBehaviour;
import com.simibubi.create.content.contraptions.behaviour.MovementContext;
import com.simibubi.create.foundation.utility.VecHelper;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.block.SlabBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.SlabType;
import net.minecraft.world.phys.Vec3;

public class SeatMovementBehaviour implements MovementBehaviour {

    @Override
    public void startMoving(MovementContext context) {
        MovementBehaviour.super.startMoving(context);
        int indexOf = context.contraption.getSeats().indexOf(context.localPos);
        context.data.putInt("SeatIndex", indexOf);
    }

    @Override
    public void visitNewPosition(MovementContext context, BlockPos pos) {
        MovementBehaviour.super.visitNewPosition(context, pos);
        AbstractContraptionEntity contraptionEntity = context.contraption.entity;
        if (contraptionEntity != null) {
            int index = context.data.getInt("SeatIndex");
            if (index != -1) {
                Map<UUID, Integer> seatMapping = context.contraption.getSeatMapping();
                BlockState blockState = context.world.getBlockState(pos);
                boolean slab = blockState.m_60734_() instanceof SlabBlock && blockState.m_61143_(SlabBlock.TYPE) == SlabType.BOTTOM;
                boolean solid = blockState.m_60815_() || slab;
                if (seatMapping.containsValue(index)) {
                    if (solid) {
                        Entity toDismount = null;
                        for (Entry<UUID, Integer> entry : seatMapping.entrySet()) {
                            if ((Integer) entry.getValue() == index) {
                                for (Entity entity : contraptionEntity.m_20197_()) {
                                    if (((UUID) entry.getKey()).equals(entity.getUUID())) {
                                        toDismount = entity;
                                    }
                                }
                            }
                        }
                        if (toDismount != null) {
                            toDismount.stopRiding();
                            Vec3 position = VecHelper.getCenterOf(pos).add(0.0, slab ? 0.5 : 1.0, 0.0);
                            toDismount.teleportTo(position.x, position.y, position.z);
                            toDismount.getPersistentData().remove("ContraptionDismountLocation");
                        }
                    }
                }
            }
        }
    }
}