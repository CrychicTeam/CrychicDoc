package com.mna.gui.containers.entity;

import com.mna.api.capabilities.IPlayerMagic;
import com.mna.capabilities.playerdata.magic.PlayerMagicProvider;
import com.mna.gui.containers.ContainerInit;
import javax.annotation.Nonnull;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerRift extends AbstractContainerMenu {

    private SimpleContainer inventory;

    public ContainerRift(int i, Inventory playerInventory, FriendlyByteBuf buffer) {
        this(i, playerInventory, ((IPlayerMagic) playerInventory.player.getCapability(PlayerMagicProvider.MAGIC).orElse(null)).getRiftInventory());
    }

    public ContainerRift(int i, Inventory playerInv, SimpleContainer riftInv) {
        super(ContainerInit.RIFT.get(), i);
        this.inventory = riftInv;
        this.initializeSlots(playerInv);
    }

    protected void initializeSlots(Inventory playerInv) {
        for (int k = 0; k < this.slotsPerRow(); k++) {
            for (int j = 0; j < this.numRows(); j++) {
                this.m_38897_(new Slot(this.inventory, k + j * this.slotsPerRow(), 8 + k * 18, 13 + j * 18));
            }
        }
        int i = (this.numRows() - 4) * 18;
        for (int l = 0; l < 3; l++) {
            for (int j1 = 0; j1 < 9; j1++) {
                this.m_38897_(new Slot(playerInv, j1 + l * 9 + 9, 8 + j1 * 18, 94 + l * 18 + i));
            }
        }
        for (int i1 = 0; i1 < 9; i1++) {
            this.m_38897_(new Slot(playerInv, i1, 8 + i1 * 18, 152 + i));
        }
    }

    protected int slotsPerRow() {
        return 9;
    }

    protected int numRows() {
        return 6;
    }

    @Override
    public boolean stillValid(@Nonnull Player player) {
        return true;
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