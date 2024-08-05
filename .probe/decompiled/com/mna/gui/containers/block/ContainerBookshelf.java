package com.mna.gui.containers.block;

import com.mna.api.blocks.tile.TileEntityWithInventory;
import com.mna.blocks.tileentities.BookshelfTile;
import com.mna.gui.containers.ContainerInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerBookshelf extends AbstractContainerMenu {

    private final TileEntityWithInventory bookshelf;

    public static int SIZE = 15;

    public ContainerBookshelf(int i, Inventory playerInventory, FriendlyByteBuf packetBuffer) {
        this(i, playerInventory, (BookshelfTile) playerInventory.player.m_9236_().getBlockEntity(packetBuffer.readBlockPos()));
    }

    public ContainerBookshelf(int id, Inventory playerInventory, BookshelfTile bookshelfTile) {
        super(ContainerInit.BOOKSHELF.get(), id);
        this.bookshelf = bookshelfTile;
        int slotIndex = 0;
        for (int col = 0; col < 5; col++) {
            for (int row = 0; row < 3; row++) {
                this.m_38897_(new Slot(this.bookshelf, slotIndex++, 44 + col * 18, 11 + row * 22));
            }
        }
        for (int xpos = 0; xpos < 3; xpos++) {
            for (int ypos = 0; ypos < 9; ypos++) {
                this.m_38897_(new Slot(playerInventory, ypos + xpos * 9 + 9, 8 + ypos * 18, 80 + xpos * 18));
            }
        }
        for (int xpos = 0; xpos < 9; xpos++) {
            this.m_38897_(new Slot(playerInventory, xpos, 8 + xpos * 18, 138));
        }
    }

    @Override
    public boolean stillValid(Player playerIn) {
        return this.bookshelf.stillValid(playerIn);
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
        if (!this.bookshelf.m_58904_().isClientSide()) {
            this.bookshelf.m_58904_().sendBlockUpdated(this.bookshelf.m_58899_(), this.bookshelf.m_58900_(), this.bookshelf.m_58900_(), 3);
        }
    }
}