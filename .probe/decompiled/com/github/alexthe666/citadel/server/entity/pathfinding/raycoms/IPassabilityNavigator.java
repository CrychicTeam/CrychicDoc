package com.github.alexthe666.citadel.server.entity.pathfinding.raycoms;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

public interface IPassabilityNavigator {

    int maxSearchNodes();

    boolean isBlockExplicitlyPassable(BlockState var1, BlockPos var2, BlockPos var3);

    boolean isBlockExplicitlyNotPassable(BlockState var1, BlockPos var2, BlockPos var3);
}