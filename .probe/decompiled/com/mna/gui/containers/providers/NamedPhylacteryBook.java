package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerPhylacteryStaff;
import com.mna.inventory.ItemInventoryBase;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NamedPhylacteryBook implements MenuProvider {

    private final ItemStack stack;

    public NamedPhylacteryBook(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerPhylacteryStaff(i, inventory, new ItemInventoryBase(this.stack, 24));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}