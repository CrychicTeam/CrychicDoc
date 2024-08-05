package me.jellysquid.mods.lithium.common.util;

import net.minecraft.core.Direction;

public final class DirectionConstants {

    public static final Direction[] ALL = Direction.values();

    public static final Direction[] VERTICAL = new Direction[] { Direction.DOWN, Direction.UP };

    public static final Direction[] HORIZONTAL = new Direction[] { Direction.WEST, Direction.EAST, Direction.NORTH, Direction.SOUTH };

    private DirectionConstants() {
    }
}