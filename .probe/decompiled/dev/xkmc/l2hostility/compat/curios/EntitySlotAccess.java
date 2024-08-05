package dev.xkmc.l2hostility.compat.curios;

import java.util.function.Function;
import net.minecraft.world.item.ItemStack;

public interface EntitySlotAccess {

    ItemStack get();

    void set(ItemStack var1);

    default void modify(Function<ItemStack, ItemStack> func) {
        this.set((ItemStack) func.apply(this.get()));
    }

    String getID();
}