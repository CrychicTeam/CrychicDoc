package com.mna.gui.containers.providers;

import com.mna.gui.containers.item.ContainerRunicMalus;
import com.mna.inventory.ItemInventoryBase;
import javax.annotation.Nullable;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class NamedRunicMalus implements MenuProvider {

    private final ItemStack stack;

    public NamedRunicMalus(ItemStack stack) {
        this.stack = stack;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int i, Inventory inventory, Player player) {
        return new ContainerRunicMalus(i, inventory, new ItemInventoryBase(this.stack, 6));
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}