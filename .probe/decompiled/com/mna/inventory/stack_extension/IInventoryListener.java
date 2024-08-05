package com.mna.inventory.stack_extension;

import com.mna.gui.containers.IExtendedItemHandler;

@FunctionalInterface
public interface IInventoryListener {

    void inventoryChanged(IExtendedItemHandler var1, int var2);
}