package net.mehvahdjukaar.supplementaries.api;

import net.minecraft.world.item.ItemStack;

public interface IQuiverEntity {

    ItemStack supplementaries$getQuiver();

    default boolean supplementaries$hasQuiver() {
        return !this.supplementaries$getQuiver().isEmpty();
    }

    void supplementaries$setQuiver(ItemStack var1);
}