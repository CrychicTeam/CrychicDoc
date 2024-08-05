package me.jellysquid.mods.lithium.api.inventory;

import net.minecraft.core.NonNullList;
import net.minecraft.world.Container;
import net.minecraft.world.entity.vehicle.ContainerEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.RandomizableContainerBlockEntity;

public interface LithiumInventory extends Container {

    NonNullList<ItemStack> getInventoryLithium();

    void setInventoryLithium(NonNullList<ItemStack> var1);

    default void generateLootLithium() {
        if (this instanceof RandomizableContainerBlockEntity) {
            ((RandomizableContainerBlockEntity) this).unpackLootTable(null);
        }
        if (this instanceof ContainerEntity) {
            ((ContainerEntity) this).unpackChestVehicleLootTable(null);
        }
    }
}