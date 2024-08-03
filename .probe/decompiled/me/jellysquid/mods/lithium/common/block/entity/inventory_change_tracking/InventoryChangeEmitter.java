package me.jellysquid.mods.lithium.common.block.entity.inventory_change_tracking;

import me.jellysquid.mods.lithium.common.hopper.LithiumStackList;

public interface InventoryChangeEmitter {

    void emitStackListReplaced();

    void emitRemoved();

    void emitContentModified();

    void emitFirstComparatorAdded();

    void forwardContentChangeOnce(InventoryChangeListener var1, LithiumStackList var2, InventoryChangeTracker var3);

    void forwardMajorInventoryChanges(InventoryChangeListener var1);

    void stopForwardingMajorInventoryChanges(InventoryChangeListener var1);

    default void emitCallbackReplaced() {
        this.emitRemoved();
    }
}