package me.jellysquid.mods.lithium.common.hopper;

import net.minecraft.core.Direction;

public interface UpdateReceiver {

    void invalidateCacheOnNeighborUpdate(boolean var1);

    void invalidateCacheOnNeighborUpdate(Direction var1);
}