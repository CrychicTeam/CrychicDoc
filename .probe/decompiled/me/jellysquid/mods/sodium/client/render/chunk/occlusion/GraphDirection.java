package me.jellysquid.mods.sodium.client.render.chunk.occlusion;

import net.minecraft.core.Direction;

public class GraphDirection {

    public static final int DOWN = 0;

    public static final int UP = 1;

    public static final int NORTH = 2;

    public static final int SOUTH = 3;

    public static final int WEST = 4;

    public static final int EAST = 5;

    public static final int COUNT = 6;

    private static final Direction[] ENUMS = new Direction[6];

    private static final int[] OPPOSITE = new int[6];

    private static final int[] X = new int[6];

    private static final int[] Y = new int[6];

    private static final int[] Z = new int[6];

    public static int opposite(int direction) {
        return OPPOSITE[direction];
    }

    public static int x(int direction) {
        return X[direction];
    }

    public static int y(int direction) {
        return Y[direction];
    }

    public static int z(int direction) {
        return Z[direction];
    }

    public static Direction toEnum(int direction) {
        return ENUMS[direction];
    }

    static {
        OPPOSITE[0] = 1;
        OPPOSITE[1] = 0;
        OPPOSITE[2] = 3;
        OPPOSITE[3] = 2;
        OPPOSITE[4] = 5;
        OPPOSITE[5] = 4;
        X[4] = -1;
        X[5] = 1;
        Y[0] = -1;
        Y[1] = 1;
        Z[2] = -1;
        Z[3] = 1;
        ENUMS[0] = Direction.DOWN;
        ENUMS[1] = Direction.UP;
        ENUMS[2] = Direction.NORTH;
        ENUMS[3] = Direction.SOUTH;
        ENUMS[4] = Direction.WEST;
        ENUMS[5] = Direction.EAST;
    }
}