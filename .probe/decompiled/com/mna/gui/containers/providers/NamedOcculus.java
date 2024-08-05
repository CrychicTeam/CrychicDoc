package com.mna.gui.containers.providers;

import com.mna.gui.containers.block.ContainerOcculus;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;

public class NamedOcculus implements MenuProvider {

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player playerEntity) {
        return new ContainerOcculus(id, playerInventory, null);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("");
    }
}