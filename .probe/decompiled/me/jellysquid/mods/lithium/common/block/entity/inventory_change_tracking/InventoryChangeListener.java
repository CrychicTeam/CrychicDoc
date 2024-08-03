package me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking;

import net.minecraft.world.Container;

public interface InventoryChangeListener {

    default void handleStackListReplaced(Container inventory) {
        this.handleInventoryRemoved(inventory);
    }

    void handleInventoryContentModified(Container var1);

    void handleInventoryRemoved(Container var1);

    boolean handleComparatorAdded(Container var1);
}