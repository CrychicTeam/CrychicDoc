package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class ContainerGuideBook extends AbstractContainerMenu {

    public ContainerGuideBook(int id, Inventory playerInventory, FriendlyByteBuf buffer) {
        super(ContainerInit.GUIDE_BOOK.get(), id);
    }

    public ContainerGuideBook(int i, Inventory playerInv, SimpleContainer itemInv) {
        super(ContainerInit.GUIDE_BOOK.get(), i);
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return true;
    }

    @Override
    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
        return ItemStack.EMPTY;
    }
}