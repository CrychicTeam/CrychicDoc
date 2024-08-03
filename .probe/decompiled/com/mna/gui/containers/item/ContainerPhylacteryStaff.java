package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.HeldContainerBase;
import com.mna.gui.containers.slots.BaseSlot;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.filters.ItemFilterGroup;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerPhylacteryStaff extends HeldContainerBase {

    public ContainerPhylacteryStaff(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.STAFF_PHYLACTERY.get()), 24));
    }

    public ContainerPhylacteryStaff(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.PHYLACTERY_STAFF.get(), i, playerInv, basebag);
    }

    @Override
    protected void initializeSlots(Inventory playerInv) {
        int slotIndex = 0;
        for (int i = 0; i < 3; i++) {
            this.m_38897_(this.slot(this.inventory, slotIndex++, 126 + i * 18, 55));
        }
        for (int i = 0; i < 3; i++) {
            this.m_38897_(this.slot(this.inventory, slotIndex++, 197, 81 + i * 18));
        }
        for (int i = 0; i < 3; i++) {
            this.m_38897_(this.slot(this.inventory, slotIndex++, 126 + i * 18, 143));
        }
        for (int i = 2; i >= 0; i--) {
            this.m_38897_(this.slot(this.inventory, slotIndex++, 91, 81 + i * 18));
        }
        for (int j = 0; j < 3; j++) {
            for (int i = 0; i < 4; i++) {
                this.m_38897_(this.slot(this.inventory, slotIndex++, 117 + i * 18, 81 + j * 18));
            }
        }
        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.m_38897_(new Slot(playerInv, j1 + l * 9 + 9, 55 + j1 * 18, 174 + l * 18));
                slotIndex++;
            }
        }
        for (int i1 = 0; i1 < 9; i1++) {
            if (i1 == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, i1, 55 + i1 * 18, 232));
            slotIndex++;
        }
    }

    @Override
    public BaseSlot slot(IItemHandler inv, int index, int x, int y) {
        return new ItemFilterSlot(inv, index, x, y, ItemFilterGroup.ANY_NON_EMPTY_PHYLACTERY);
    }

    @Override
    protected int slotsPerRow() {
        return 9;
    }

    @Override
    protected int numRows() {
        return 2;
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        try {
            if (this.m_38853_(slotId).getItem().hashCode() == this.bagHash) {
                return;
            }
        } catch (Exception var6) {
        }
        super.clicked(slotId, dragType, clickTypeIn, player);
    }

    @Nonnull
    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < this.numRows() * this.slotsPerRow()) {
                if (!this.m_38903_(itemstack1, this.numRows() * this.slotsPerRow(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.numRows() * this.slotsPerRow(), false)) {
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
}