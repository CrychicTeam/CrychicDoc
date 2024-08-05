package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.common.capabilities.AutoRegisterCapability;
import org.jetbrains.annotations.NotNull;

@AutoRegisterCapability
public interface IItemHandler {

    int getSlots();

    @NotNull
    ItemStack getStackInSlot(int var1);

    @NotNull
    ItemStack insertItem(int var1, @NotNull ItemStack var2, boolean var3);

    @NotNull
    ItemStack extractItem(int var1, int var2, boolean var3);

    int getSlotLimit(int var1);

    boolean isItemValid(int var1, @NotNull ItemStack var2);
}