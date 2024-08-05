package com.github.alexthe666.iceandfire.inventory;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ContainerPodium extends AbstractContainerMenu {

    public final Container podium;

    public ContainerPodium(int i, Inventory playerInventory) {
        this(i, new SimpleContainer(1), playerInventory, new SimpleContainerData(0));
    }

    public ContainerPodium(int id, Container furnaceInventory, Inventory playerInventory, ContainerData vars) {
        super(IafContainerRegistry.PODIUM_CONTAINER.get(), id);
        this.podium = furnaceInventory;
        furnaceInventory.startOpen(playerInventory.player);
        byte b0 = 51;
        this.m_38897_(new Slot(furnaceInventory, 0, 80, 20));
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 9; j++) {
                this.m_38897_(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, i * 18 + b0));
            }
        }
        for (int var8 = 0; var8 < 9; var8++) {
            this.m_38897_(new Slot(playerInventory, var8, 8 + var8 * 18, 58 + b0));
        }
    }

    @Override
    public boolean stillValid(@NotNull Player playerIn) {
        return this.podium.stillValid(playerIn);
    }

    @NotNull
    @Override
    public ItemStack quickMoveStack(@NotNull Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.podium.getContainerSize()) {
                if (!this.m_38903_(itemstack1, this.podium.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.podium.getContainerSize(), false)) {
                return ItemStack.EMPTY;
            }
            if (itemstack1.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }
        return itemstack;
    }

    @Override
    public void removed(@NotNull Player playerIn) {
        super.removed(playerIn);
        this.podium.stopOpen(playerIn);
    }
}