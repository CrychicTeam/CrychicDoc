package org.violetmoon.quark.content.management.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ShulkerBoxSlot;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.violetmoon.quark.api.ISortingLockedSlots;
import org.violetmoon.quark.content.management.module.ExpandedItemInteractionsModule;

public class HeldShulkerBoxMenu extends AbstractContainerMenu implements ISortingLockedSlots {

    private final Container container;

    private final Player player;

    public final int blockedSlot;

    public HeldShulkerBoxMenu(int int0, Inventory inventory1, int blockedSlot) {
        this(int0, inventory1, new SimpleContainer(27), blockedSlot);
    }

    public HeldShulkerBoxMenu(int int0, Inventory inventory1, Container container2, int blockedSlot) {
        super(ExpandedItemInteractionsModule.heldShulkerBoxMenuType, int0);
        m_38869_(container2, 27);
        this.container = container2;
        this.player = inventory1.player;
        this.blockedSlot = blockedSlot;
        container2.startOpen(inventory1.player);
        for (int k = 0; k < 3; k++) {
            for (int l = 0; l < 9; l++) {
                this.m_38897_(new ShulkerBoxSlot(container2, l + k * 9, 8 + l * 18, 18 + k * 18));
            }
        }
        for (int i1 = 0; i1 < 3; i1++) {
            for (int k1 = 0; k1 < 9; k1++) {
                int id = k1 + i1 * 9 + 9;
                if (id != blockedSlot) {
                    this.m_38897_(new Slot(inventory1, id, 8 + k1 * 18, 84 + i1 * 18));
                }
            }
        }
        for (int j1 = 0; j1 < 9; j1++) {
            if (j1 != blockedSlot) {
                this.m_38897_(new Slot(inventory1, j1, 8 + j1 * 18, 142));
            }
        }
    }

    public static HeldShulkerBoxMenu fromNetwork(int windowId, Inventory playerInventory, FriendlyByteBuf buf) {
        int slot = buf.readInt();
        HeldShulkerBoxContainer container = new HeldShulkerBoxContainer(playerInventory.player, slot);
        return new HeldShulkerBoxMenu(windowId, playerInventory, container, slot);
    }

    @Override
    public boolean stillValid(Player player0) {
        return this.container.stillValid(player0);
    }

    @Override
    public ItemStack quickMoveStack(Player player0, int int1) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = (Slot) this.f_38839_.get(int1);
        if (slot != null && slot.hasItem()) {
            ItemStack itemstack1 = slot.getItem();
            itemstack = itemstack1.copy();
            if (int1 < this.container.getContainerSize()) {
                if (!this.m_38903_(itemstack1, this.container.getContainerSize(), this.f_38839_.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!this.m_38903_(itemstack1, 0, this.container.getContainerSize(), false)) {
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
    public void suppressRemoteUpdates() {
        super.suppressRemoteUpdates();
        this.player.inventoryMenu.m_150443_();
    }

    @Override
    public void resumeRemoteUpdates() {
        super.resumeRemoteUpdates();
        this.player.inventoryMenu.m_150444_();
    }

    @Override
    public void removed(Player player0) {
        super.removed(player0);
        this.container.stopOpen(player0);
    }

    @Override
    public int[] getSortingLockedSlots(boolean sortingPlayerInventory) {
        return sortingPlayerInventory ? new int[] { this.blockedSlot } : null;
    }
}