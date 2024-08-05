package com.simibubi.create.foundation.gui.menu;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;
import net.minecraftforge.items.ItemStackHandler;

public abstract class GhostItemMenu<T> extends MenuBase<T> implements IClearableMenu {

    public ItemStackHandler ghostInventory;

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    protected GhostItemMenu(MenuType<?> type, int id, Inventory inv, T contentHolder) {
        super(type, id, inv, contentHolder);
    }

    protected abstract ItemStackHandler createGhostInventory();

    protected abstract boolean allowRepeats();

    @Override
    protected void initAndReadInventory(T contentHolder) {
        this.ghostInventory = this.createGhostInventory();
    }

    @Override
    public void clearContents() {
        for (int i = 0; i < this.ghostInventory.getSlots(); i++) {
            this.ghostInventory.setStackInSlot(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canTakeItemForPickAll(ItemStack stack, Slot slotIn) {
        return slotIn.container == this.playerInventory;
    }

    @Override
    public boolean canDragTo(Slot slotIn) {
        return this.allowRepeats() ? true : slotIn.container == this.playerInventory;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId < 36) {
            super.m_150399_(slotId, dragType, clickTypeIn, player);
        } else if (clickTypeIn != ClickType.THROW) {
            ItemStack held = this.m_142621_();
            int slot = slotId - 36;
            if (clickTypeIn == ClickType.CLONE) {
                if (player.isCreative() && held.isEmpty()) {
                    ItemStack stackInSlot = this.ghostInventory.getStackInSlot(slot).copy();
                    stackInSlot.setCount(stackInSlot.getMaxStackSize());
                    this.m_142503_(stackInSlot);
                }
            } else {
                ItemStack insert;
                if (held.isEmpty()) {
                    insert = ItemStack.EMPTY;
                } else {
                    insert = held.copy();
                    insert.setCount(1);
                }
                this.ghostInventory.setStackInSlot(slot, insert);
                this.m_38853_(slotId).setChanged();
            }
        }
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        if (index < 36) {
            ItemStack stackToInsert = this.playerInventory.getItem(index);
            for (int i = 0; i < this.ghostInventory.getSlots(); i++) {
                ItemStack stack = this.ghostInventory.getStackInSlot(i);
                if (!this.allowRepeats() && ItemHandlerHelper.canItemStacksStack(stack, stackToInsert)) {
                    break;
                }
                if (stack.isEmpty()) {
                    ItemStack copy = stackToInsert.copy();
                    copy.setCount(1);
                    this.ghostInventory.insertItem(i, copy, false);
                    this.m_38853_(i + 36).setChanged();
                    break;
                }
            }
        } else {
            this.ghostInventory.extractItem(index - 36, 1, false);
            this.m_38853_(index).setChanged();
        }
        return ItemStack.EMPTY;
    }
}