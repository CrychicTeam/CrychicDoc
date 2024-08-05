package com.mna.tools;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Rotation;

public class RotationUtils {

    public static Rotation rotationFromFacing(Direction facing) {
        switch(facing) {
            case EAST:
                return Rotation.CLOCKWISE_90;
            case SOUTH:
                return Rotation.CLOCKWISE_180;
            case WEST:
                return Rotation.COUNTERCLOCKWISE_90;
            default:
                return Rotation.NONE;
        }
    }
}