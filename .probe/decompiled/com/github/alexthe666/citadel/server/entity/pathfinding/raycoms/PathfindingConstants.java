package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import net.minecraft.core.BlockPos;

public class PathfindingConstants {

    public static final int DEBUG_VERBOSITY_NONE = 0;

    public static final int DEBUG_VERBOSITY_FULL = 2;

    public static final Object debugNodeMonitor = new Object();

    public static final BlockPos BLOCKPOS_IDENTITY = new BlockPos(0, 0, 0);

    public static final BlockPos BLOCKPOS_UP = new BlockPos(0, 1, 0);

    public static final BlockPos BLOCKPOS_DOWN = new BlockPos(0, -1, 0);

    public static final BlockPos BLOCKPOS_NORTH = new BlockPos(0, 0, -1);

    public static final BlockPos BLOCKPOS_SOUTH = new BlockPos(0, 0, 1);

    public static final BlockPos BLOCKPOS_EAST = new BlockPos(1, 0, 0);

    public static final BlockPos BLOCKPOS_WEST = new BlockPos(-1, 0, 0);

    public static final double ONE_SIDE = 0.25;

    public static final double OTHER_SIDE = 0.75;

    public static final int SHIFT_X_BY = 20;

    public static final int SHIFT_Y_BY = 12;

    public static final double MAX_JUMP_HEIGHT = 1.3;

    public static final double HALF_A_BLOCK = 0.5;

    public static boolean isDebugMode;

    public static int pathfindingThreads = 1;

    public static int maxPathingNodes = 5000;

    private PathfindingConstants() {
    }
}