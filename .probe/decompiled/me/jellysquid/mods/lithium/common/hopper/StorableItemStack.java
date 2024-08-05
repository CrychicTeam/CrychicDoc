package me.jellysquid.mods.lithium.common.hopper;

public interface StorableItemStack {

    void registerToInventory(LithiumStackList var1, int var2);

    void unregisterFromInventory(LithiumStackList var1);

    void unregisterFromInventory(LithiumStackList var1, int var2);
}