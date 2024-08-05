package me.jellysquid.mods.lithium.api.inventory;

public interface LithiumCooldownReceivingInventory {

    default void setTransferCooldown(long currentTime) {
    }

    default boolean canReceiveTransferCooldown() {
        return false;
    }
}