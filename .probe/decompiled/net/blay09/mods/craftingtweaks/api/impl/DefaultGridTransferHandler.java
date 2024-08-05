package net.blay09.mods.craftingtweaks.api.impl;

import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.GridTransferHandler;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class DefaultGridTransferHandler implements GridTransferHandler<AbstractContainerMenu> {

    @Override
    public ItemStack putIntoGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu, int slotId, ItemStack itemStack) {
        Container craftMatrix = grid.getCraftingMatrix(player, menu);
        if (craftMatrix == null) {
            return itemStack;
        } else {
            ItemStack craftStack = craftMatrix.getItem(slotId);
            if (!craftStack.isEmpty()) {
                if (ItemStack.isSameItemSameTags(craftStack, itemStack)) {
                    int spaceLeft = Math.min(craftMatrix.getMaxStackSize(), craftStack.getMaxStackSize()) - craftStack.getCount();
                    if (spaceLeft > 0) {
                        ItemStack splitStack = itemStack.split(Math.min(spaceLeft, itemStack.getCount()));
                        craftStack.grow(splitStack.getCount());
                        if (itemStack.getCount() <= 0) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
            } else {
                ItemStack transferStack = itemStack.split(Math.min(itemStack.getCount(), craftMatrix.getMaxStackSize()));
                craftMatrix.setItem(slotId, transferStack);
            }
            return itemStack.getCount() <= 0 ? ItemStack.EMPTY : itemStack;
        }
    }

    @Override
    public boolean transferIntoGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu, Slot fromSlot) {
        Container craftMatrix = grid.getCraftingMatrix(player, menu);
        if (craftMatrix == null) {
            return false;
        } else {
            int start = grid.getGridStartSlot(player, menu);
            int size = grid.getGridSize(player, menu);
            ItemStack itemStack = fromSlot.getItem();
            if (itemStack.isEmpty()) {
                return false;
            } else {
                int firstEmptySlot = -1;
                for (int i = start; i < start + size; i++) {
                    int slotIndex = menu.slots.get(i).getContainerSlot();
                    ItemStack craftStack = craftMatrix.getItem(slotIndex);
                    if (!craftStack.isEmpty()) {
                        if (ItemStack.isSameItemSameTags(craftStack, itemStack)) {
                            int spaceLeft = Math.min(craftMatrix.getMaxStackSize(), craftStack.getMaxStackSize()) - craftStack.getCount();
                            if (spaceLeft > 0) {
                                ItemStack splitStack = itemStack.split(Math.min(spaceLeft, itemStack.getCount()));
                                craftStack.grow(splitStack.getCount());
                                if (itemStack.getCount() <= 0) {
                                    return true;
                                }
                            }
                        }
                    } else if (firstEmptySlot == -1) {
                        firstEmptySlot = slotIndex;
                    }
                }
                if (itemStack.getCount() > 0 && firstEmptySlot != -1) {
                    ItemStack transferStack = itemStack.split(Math.min(itemStack.getCount(), craftMatrix.getMaxStackSize()));
                    craftMatrix.setItem(firstEmptySlot, transferStack);
                    return true;
                } else {
                    return false;
                }
            }
        }
    }

    @Override
    public boolean canTransferFrom(Player player, AbstractContainerMenu menu, Slot slot, CraftingGrid toGrid) {
        return slot.container == player.getInventory();
    }
}