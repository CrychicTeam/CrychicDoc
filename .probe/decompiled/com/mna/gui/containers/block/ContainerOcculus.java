package com.mna.gui.containers.block;

import com.mna.gui.containers.ContainerInit;
import javax.annotation.Nullable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.ItemStack;

public class ContainerOcculus extends AbstractContainerMenu {

    private ContainerOcculus(@Nullable MenuType<?> type, int id, Inventory playerInv, ItemStack heldItem) {
        super(type, id);
    }

    public ContainerOcculus(int i, Inventory playerInv, FriendlyByteBuf buffer) {
        this(ContainerInit.OCCULUS.get(), i, playerInv, playerInv.getItem(playerInv.selected));
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