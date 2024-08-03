package com.mna.gui.containers.block;

import com.mna.blocks.tileentities.SeerStoneTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.ItemFilterSlot;
import com.mna.gui.containers.slots.SingleItemSlot;
import com.mna.items.ItemInit;
import com.mna.items.filters.SeerStoneItems;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerSeerStone extends AbstractContainerMenu {

    private final SeerStoneTile seer;

    public static int SIZE = 36;

    public ContainerSeerStone(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, (SeerStoneTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public ContainerSeerStone(int id, Inventory playerInventory, SeerStoneTile seerStoneTile) {
        super(ContainerInit.SEER_STONE.get(), id);
        this.seer = seerStoneTile;
        InvWrapper wrapper = new InvWrapper(this.seer);
        int slotIndex = 0;
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 2; row++) {
                this.m_38897_(new ItemFilterSlot(wrapper, slotIndex++, 37 + col * 18, 19 + row * 18, new SeerStoneItems()));
            }
        }
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 2; row++) {
                this.m_38897_(new ItemFilterSlot(wrapper, slotIndex++, 37 + col * 18, 57 + row * 18, new SeerStoneItems()));
            }
        }
        this.m_38897_(new SingleItemSlot(wrapper, slotIndex++, 100, 95, ItemInit.RUNE_MARKING.get()));
        this.m_38897_(new SingleItemSlot(wrapper, slotIndex++, 118, 95, ItemInit.RUNE_MARKING.get()));
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, 37 + ypos * 18, 127 + xpos * 18));
            }
        }
        for (int xpos = 0; xpos < 9; xpos++) {
            this.m_38897_(new Slot(playerInventory, xpos, 37 + xpos * 18, 185));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.seer.m_6542_(playerIn);
    }

    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(index);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (index < SIZE) {
                if (!this.m_38903_(itemstack1, SIZE, this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, SIZE, false)) {
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
        if (!this.seer.m_58904_().isClientSide()) {
            this.seer.buildFilters();
            this.seer.m_58904_().sendBlockUpdated(this.seer.m_58899_(), this.seer.m_58900_(), this.seer.m_58900_(), 3);
        }
    }
}