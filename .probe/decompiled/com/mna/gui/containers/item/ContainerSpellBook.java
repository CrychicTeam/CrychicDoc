package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.HeldContainerBase;
import com.mna.gui.containers.slots.BaseSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.inventory.InventorySpellBook;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import com.mna.items.sorcery.ItemSpellBook;
import com.mna.network.ClientMessageDispatcher;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;

public class ContainerSpellBook extends HeldContainerBase {

    public ContainerSpellBook(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new InventorySpellBook(new ItemStack(ItemInit.SPELL_BOOK.get())));
    }

    public ContainerSpellBook(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.SPELL_BOOK.get(), i, playerInv, basebag);
    }

    @Override
    protected void initializeSlots(Inventory playerInv) {
        int slotIndex = 0;
        for (int k = 0; k < this.slotsPerRow(); k++) {
            for (int j = 0; j < this.numRows(); j++) {
                if (k % this.slotsPerRow() == 0) {
                    this.m_38897_(this.slot(this.inventory, k + j * this.slotsPerRow(), 18 + k * 18, 7 + j * 18));
                } else {
                    this.m_38897_(this.slot(this.inventory, k + j * this.slotsPerRow(), 104 + k * 29, 7 + j * 18));
                }
                slotIndex++;
            }
        }
        int i = (this.numRows() - 4) * 18;
        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.m_38897_(new Slot(playerInv, j1 + l * 9 + 9, 48 + j1 * 18, 102 + l * 18 + i));
                slotIndex++;
            }
        }
        for (int i1 = 0; i1 < 9; i1++) {
            if (i1 == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, i1, 48 + i1 * 18, 160 + i));
            slotIndex++;
        }
    }

    @Override
    public BaseSlot slot(IItemHandler inv, int index, int x, int y) {
        return new SingleItemSlot(inv, index, x, y, ItemInit.SPELL.get());
    }

    @Override
    protected int slotsPerRow() {
        return 5;
    }

    @Override
    protected int numRows() {
        return 8;
    }

    public void onClose() {
        int curSlot = ItemSpellBook.getActiveSpellSlot(this.inventory.getStack());
        ClientMessageDispatcher.sendSpellBookSlotChange(curSlot, false);
    }
}