package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.wizard_lab.RunescribingTableTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.items.ItemInit;
import com.mna.items.filters.RuneHammerFilter;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerRunescribingTable extends AbstractContainerMenu {

    private final Container inventory;

    private final RunescribingTableTile table;

    static int rowLength = 9;

    static int rowCount = 3;

    public ContainerRunescribingTable(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, ((RunescribingTableTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos())).readFrom(packetBuffer));
    }

    public ContainerRunescribingTable(int windowId, Inventory playerInventory, RunescribingTableTile inventory) {
        super(ContainerInit.RUNESCRIBING_TABLE.get(), windowId);
        this.inventory = inventory;
        this.table = inventory;
        inventory.m_5856_(playerInventory.player);
        InvWrapper iWrapper = new InvWrapper(inventory);
        this.m_38897_(new ItemFilterSlot(iWrapper, 0, 8, 8, new RuneHammerFilter()).setMaxStackSize(1));
        this.m_38897_(new SingleItemSlot(iWrapper, 1, 8, 32, ItemInit.RUNESMITH_CHISEL.get()).setMaxStackSize(1));
        this.m_38897_(new SingleItemSlot(iWrapper, 2, 174, 130, ItemInit.RUNE_PATTERN.get()).setMaxStackSize(1));
        this.m_38897_(new SingleItemSlot(iWrapper, 3, 251, 8, ItemInit.RECIPE_SCRAP_RUNESCRIBING.get()).setMaxStackSize(1));
        this.m_38897_(new SingleItemSlot(iWrapper, 4, 8, 56, Items.CLAY_BALL));
        int slotRowStartX = 215;
        int slotColStep = 18;
        int slotRowY = 138;
        for (int pSlotRow = 0; pSlotRow < 5; pSlotRow++) {
            int slotRowX = 215;
            for (int pSlotCol = 0; pSlotCol < 5; pSlotCol++) {
                int idx = pSlotRow * 5 + pSlotCol;
                this.m_38897_(new SingleItemSlot(iWrapper, 5 + idx, slotRowX, slotRowY, ItemInit.RECIPE_SCRAP_RUNESCRIBING.get()));
                slotRowX += 18;
            }
            int var14 = 215;
            slotRowY += 18;
        }
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, 19 + ypos * 18, 156 + xpos * 18));
            }
        }
        for (int var13 = 0; var13 < 9; var13++) {
            this.m_38897_(new Slot(playerInventory, var13, 19 + var13 * 18, 214));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.inventory.stillValid(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < 30) {
                if (index == 3) {
                    if (!this.m_38903_(itemstack1, 5, 30, false) && !this.m_38903_(itemstack1, 30, this.f_38839_.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (index >= 5 && index <= 29) {
                    if (this.m_38903_(itemstack1, 3, 4, true)) {
                        return ItemStack.EMPTY;
                    }
                    if (!this.m_38903_(itemstack1, 30, this.f_38839_.size(), true)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.m_38903_(itemstack1, 30, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, 30, false)) {
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
    public void removed(Player playerIn) {
        super.removed(playerIn);
        this.inventory.stopOpen(playerIn);
    }

    public void writeMutex(long mutex_h, long mutex_v, Player player, int playerTier, boolean isRemove) {
        this.table.writeMutexChanges(mutex_h, mutex_v, player, playerTier, isRemove, player.m_9236_().getRandom());
    }

    public boolean hasRequiredItems() {
        return this.table.hasRequiredItems(true);
    }

    public boolean hasPattern() {
        return this.table.hasRequiredItems(false);
    }

    public long getHMutex() {
        return this.table.getHMutex();
    }

    public long getVMutex() {
        return this.table.getVMutex();
    }

    public boolean canUndo(Player player) {
        return this.table.canUndo(player);
    }
}