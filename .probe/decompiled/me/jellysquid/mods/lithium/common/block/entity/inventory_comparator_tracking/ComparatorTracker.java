package me.jellysquid.mods.lithium.common.block.entity.inventory_comparator_tracking;

import net.minecraft.core.Direction;

public interface ComparatorTracker {

    void onComparatorAdded(Direction var1, int var2);

    boolean hasAnyComparatorNearby();
}