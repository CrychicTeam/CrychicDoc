package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerMarkBook;
import com.mna.inventory.ItemInventoryBase;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NamedMarkBook implements MenuProvider {

    private final ItemStack stack;

    public NamedMarkBook(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerMarkBook(i, inventory, new ItemInventoryBase(this.stack, 16));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}