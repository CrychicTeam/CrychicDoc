package com.simibubi.create.content.contraptions.bearing;

import com.simibubi.create.content.contraptions.DirectionalExtenderScrollOptionSlot;
import com.simibubi.create.content.contraptions.IControlContraption;
import com.simibubi.create.foundation.blockEntity.behaviour.ValueBoxTransform;
import net.minecraft.core.Direction;

public interface IBearingBlockEntity extends IControlContraption {

    float getInterpolatedAngle(float var1);

    boolean isWoodenTop();

    default ValueBoxTransform getMovementModeSlot() {
        return new DirectionalExtenderScrollOptionSlot((state, d) -> {
            Direction.Axis axis = d.getAxis();
            Direction.Axis bearingAxis = ((Direction) state.m_61143_(BearingBlock.FACING)).getAxis();
            return bearingAxis != axis;
        });
    }

    void setAngle(float var1);
}