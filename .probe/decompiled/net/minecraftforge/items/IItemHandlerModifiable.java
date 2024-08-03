package net.minecraftforge.items;

import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public interface IItemHandlerModifiable extends IItemHandler {

    void setStackInSlot(int var1, @NotNull ItemStack var2);
}