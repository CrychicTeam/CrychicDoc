package net.minecraft.client.gui.screens.inventory;

import net.minecraft.client.Minecraft;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerListener;
import net.minecraft.world.item.ItemStack;

public class CreativeInventoryListener implements ContainerListener {

    private final Minecraft minecraft;

    public CreativeInventoryListener(Minecraft minecraft0) {
        this.minecraft = minecraft0;
    }

    @Override
    public void slotChanged(AbstractContainerMenu abstractContainerMenu0, int int1, ItemStack itemStack2) {
        this.minecraft.gameMode.handleCreativeModeItemAdd(itemStack2, int1);
    }

    @Override
    public void dataChanged(AbstractContainerMenu abstractContainerMenu0, int int1, int int2) {
    }
}