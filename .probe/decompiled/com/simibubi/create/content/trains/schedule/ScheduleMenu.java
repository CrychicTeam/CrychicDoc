package com.simibubi.create.content.trains.schedule;

import com.simibubi.create.foundation.gui.menu.GhostItemMenu;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;

public class ScheduleMenu extends GhostItemMenu<ItemStack> {

    public boolean slotsActive = true;

    public int targetSlotsActive = 1;

    static final int slots = 2;

    public ScheduleMenu(MenuType<?> type, int id, Inventory inv, FriendlyByteBuf extraData) {
        super(type, id, inv, extraData);
    }

    public ScheduleMenu(MenuType<?> type, int id, Inventory inv, ItemStack contentHolder) {
        super(type, id, inv, contentHolder);
    }

    @Override
    protected ItemStackHandler createGhostInventory() {
        return new ItemStackHandler(2);
    }

    @Override
    public void clicked(int slotId, int dragType, ClickType clickTypeIn, Player player) {
        if (slotId != this.playerInventory.selected || clickTypeIn == ClickType.THROW) {
            super.clicked(slotId, dragType, clickTypeIn, player);
        }
    }

    @Override
    protected boolean allowRepeats() {
        return true;
    }

    protected ItemStack createOnClient(FriendlyByteBuf extraData) {
        return extraData.readItem();
    }

    @Override
    protected void addSlots() {
        this.addPlayerSlots(46, 140);
        for (int i = 0; i < 2; i++) {
            this.m_38897_(new ScheduleMenu.InactiveItemHandlerSlot(this.ghostInventory, i, i, 54 + 20 * i, 88));
        }
    }

    @Override
    protected void addPlayerSlots(int x, int y) {
        for (int hotbarSlot = 0; hotbarSlot < 9; hotbarSlot++) {
            this.m_38897_(new ScheduleMenu.InactiveSlot(this.playerInventory, hotbarSlot, x + hotbarSlot * 18, y + 58));
        }
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                this.m_38897_(new ScheduleMenu.InactiveSlot(this.playerInventory, col + row * 9 + 9, x + col * 18, y + row * 18));
            }
        }
    }

    protected void saveData(ItemStack contentHolder) {
    }

    @Override
    public boolean stillValid(Player player) {
        return this.playerInventory.getSelected() == this.contentHolder;
    }

    class InactiveItemHandlerSlot extends SlotItemHandler {

        private int targetIndex;

        public InactiveItemHandlerSlot(IItemHandler itemHandler, int targetIndex, int index, int xPosition, int yPosition) {
            super(itemHandler, index, xPosition, yPosition);
            this.targetIndex = targetIndex;
        }

        @Override
        public boolean isActive() {
            return ScheduleMenu.this.slotsActive && this.targetIndex < ScheduleMenu.this.targetSlotsActive;
        }
    }

    class InactiveSlot extends Slot {

        public InactiveSlot(Container pContainer, int pIndex, int pX, int pY) {
            super(pContainer, pIndex, pX, pY);
        }

        @Override
        public boolean isActive() {
            return ScheduleMenu.this.slotsActive;
        }
    }
}