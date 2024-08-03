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

public class ContainerAstroBlade extends HeldContainerBase {

    public ContainerAstroBlade(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, new ItemInventoryBase(new ItemStack(ItemInit.ASTRO_BLADE.get(), 2), 2));
    }

    public ContainerAstroBlade(int i, Inventory playerInv, ItemInventoryBase basebag) {
        super(ContainerInit.ASTRO_BLADE.get(), i, playerInv, basebag);
    }

    @Override
    protected void initializeSlots(Inventory playerInv) {
        this.m_38897_(this.slot(this.inventory, 0, 48, 49));
        this.m_38897_(this.slot(this.inventory, 1, 90, 7));
        int slotIndex = 2;
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                slotIndex++;
                this.m_38897_(new Slot(playerInv, ypos + xpos * 9 + 9, 8 + ypos * 18, 127 + xpos * 18));
            }
        }
        for (int var5 = 0; var5 < 9; var5++) {
            if (var5 == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, var5, 8 + var5 * 18, 185));
            slotIndex++;
        }
    }

    @Override
    public BaseSlot slot(IItemHandler inv, int index, int x, int y) {
        return new ItemFilterSlot(inv, index, x, y, ItemFilterGroup.ANY_SPELL).setMaxStackSize(1);
    }

    @Override
    protected int slotsPerRow() {
        return 1;
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