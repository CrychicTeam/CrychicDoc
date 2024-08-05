package com.mna.gui.containers.item;

import com.mna.gui.containers.ContainerInit;
import com.mna.gui.containers.slots.GhostSlot;
import com.mna.inventory.ItemInventoryBase;
import com.mna.items.ItemInit;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.DataSlot;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class ContainerFilterItem extends AbstractContainerMenu {

    public ItemStack heldItem;

    private ItemInventoryBase inventory;

    public int bagHash;

    protected int mySlot;

    private int myPlayerIndex;

    private final DataSlot matchDurability = DataSlot.standalone();

    private final DataSlot matchTag = DataSlot.standalone();

    public ContainerFilterItem(MenuType<?> type, int id, Inventory playerInv, ItemInventoryBase inventory) {
        super(type, id);
        this.inventory = inventory;
        this.bagHash = inventory.getStack().hashCode();
        this.myPlayerIndex = playerInv.selected;
        this.heldItem = inventory.getStack();
        this.matchDurability.set(ItemInit.FILTER_ITEM.get().getMatchDurability(this.heldItem) ? 1 : 0);
        this.matchTag.set(ItemInit.FILTER_ITEM.get().getMatchTag(this.heldItem) ? 1 : 0);
        this.initializeSlots(playerInv);
    }

    public ContainerFilterItem(int i, Inventory playerInv, FriendlyByteBuf buffer) {
        this(ContainerInit.FILTER_ITEM.get(), i, playerInv, new ItemInventoryBase(playerInv.getItem(playerInv.selected), 24));
    }

    protected void initializeSlots(Inventory playerInv) {
        int slotIndex = 0;
        int bagSlotIndex = 0;
        this.m_38895_(this.matchDurability);
        this.m_38895_(this.matchTag);
        for (int col = 0; col < 6; col++) {
            for (int row = 0; row < 4; row++) {
                this.m_38897_(new GhostSlot(this.inventory, bagSlotIndex++, 35 + col * 18, 7 + row * 18));
                slotIndex++;
            }
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new Slot(playerInv, col + row * 9 + 9, 8 + col * 18, 106 + row * 18));
                slotIndex++;
            }
        }
        for (int col = 0; col < 9; col++) {
            if (col == playerInv.selected) {
                this.mySlot = slotIndex;
            }
            this.m_38897_(new Slot(playerInv, col, 8 + col * 18, 164));
            slotIndex++;
        }
    }

    @Override
    public boolean stillValid(Player player0) {
        return true;
    }

    @Override
    public void broadcastChanges() {
        super.broadcastChanges();
        this.inventory.writeItemStack();
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId != this.mySlot && (clickTypeIn != ClickType.SWAP || dragType != this.myPlayerIndex)) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    public void removed(Player player0) {
        if (!player0.m_9236_().isClientSide()) {
            ItemInit.FILTER_ITEM.get().setMatchDurability(this.inventory.getStack(), this.getDurabilityMatching());
            ItemInit.FILTER_ITEM.get().setMatchTag(this.inventory.getStack(), this.getTagMatching());
        }
        super.removed(player0);
    }

    @Override
    public boolean clickMenuButton(Player player, int index) {
        if (index == 0) {
            this.matchDurability.set(this.getDurabilityMatching() ? 0 : 1);
        } else if (index == 1) {
            this.matchTag.set(this.getTagMatching() ? 0 : 1);
        }
        this.broadcastChanges();
        return true;
    }

    public boolean getDurabilityMatching() {
        return this.matchDurability.get() == 1;
    }

    public boolean getTagMatching() {
        return this.matchTag.get() == 1;
    }

    @Override
    public ItemStack quickMoveStack(Player player, int index) {
        Slot slot = (Slot) this.f_38839_.get(index);
        int mainSlots = 24;
        if (slot != null && slot.hasItem()) {
            ItemStack shiftClickedStack = slot.getItem();
            if (index > mainSlots) {
                this.moveItemStackTo(shiftClickedStack, 0, mainSlots, false);
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    protected boolean moveItemStackTo(ItemStack stack, int startIndex, int endIndex, boolean wrap) {
        boolean flag = false;
        if (wrap) {
            int i = endIndex - 1;
        }
        int var10;
        if (wrap) {
            var10 = endIndex - 1;
        } else {
            var10 = startIndex;
        }
        while (wrap ? var10 >= startIndex : var10 < endIndex) {
            Slot slot = (Slot) this.f_38839_.get(var10);
            ItemStack slotStack = slot.getItem();
            if (slotStack.isEmpty() && (slot instanceof GhostSlot || slot.mayPlace(stack))) {
                ItemStack copy = stack.copy();
                copy.setCount(1);
                slot.set(copy);
                slot.setChanged();
                flag = true;
                break;
            }
            if (wrap) {
                var10--;
            } else {
                var10++;
            }
        }
        return flag;
    }
}