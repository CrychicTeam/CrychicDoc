package com.mna.gui.containers.block;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.blocks.tileentities.ScrollShelfTile;
import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.AnyItemSlot;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.wrapper.InvWrapper;

public class ContainerScrollShelf extends AbstractContainerMenu {

    private final TileEntityWithInventory scrollshelf;

    public static int SIZE = 81;

    public ContainerScrollShelf(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, (ScrollShelfTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public ContainerScrollShelf(int id, Inventory playerInventory, ScrollShelfTile scrollShelfTile) {
        super(ContainerInit.SCROLLSHELF.get(), id);
        this.scrollshelf = scrollShelfTile;
        InvWrapper wrapper = new InvWrapper(this.scrollshelf);
        int slotIndex = 0;
        for (int row = 0; row < 7; row++) {
            boolean shortRow = row % 2 != 0;
            int slotX = shortRow ? 21 : 12;
            int cols = shortRow ? 2 : 3;
            for (int col = 0; col < cols; col++) {
                this.m_38897_(new AnyItemSlot(wrapper, slotIndex++, slotX + col * 18, 11 + row * 18));
            }
        }
        for (int col = 0; col < 9; col++) {
            for (int row = 0; row < 7; row++) {
                this.m_38897_(new AnyItemSlot(wrapper, slotIndex++, 84 + col * 18, 11 + row * 18));
            }
        }
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, 48 + ypos * 18, 144 + xpos * 18));
            }
        }
        for (int xpos = 0; xpos < 9; xpos++) {
            this.m_38897_(new Slot(playerInventory, xpos, 48 + xpos * 18, 202));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.scrollshelf.stillValid(playerIn);
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
        if (!this.scrollshelf.m_58904_().isClientSide()) {
            this.scrollshelf.m_58904_().sendBlockUpdated(this.scrollshelf.m_58899_(), this.scrollshelf.m_58900_(), this.scrollshelf.m_58900_(), 3);
        }
    }
}