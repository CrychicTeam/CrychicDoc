package me.jellysquid.mods.lithium.common.ai.pathing;

import net.minecraft.world.level.pathfinder.BlockPathTypes;

public interface BlockStatePathingCache {

    BlockPathTypes getPathNodeType();

    BlockPathTypes getNeighborPathNodeType();
}