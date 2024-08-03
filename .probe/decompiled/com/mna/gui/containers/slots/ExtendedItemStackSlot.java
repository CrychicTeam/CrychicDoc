package com.mna.gui.containers.slots;

import com.mna.gui.containers.IExtendedItemHandler;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.SlotItemHandler;

public class ExtendedItemStackSlot extends SlotItemHandler {

    private final IExtendedItemHandler inventory;

    private final int index;

    private List<Item> blacklistedItems;

    public ExtendedItemStackSlot(IExtendedItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
        this.inventory = itemHandler;
        this.blacklistedItems = new ArrayList();
    }

    public ExtendedItemStackSlot blacklistItem(Item item) {
        this.blacklistedItems.add(item);
        return this;
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return !this.blacklistedItems.contains(stack.getItem()) && stack.getItem().canFitInsideContainerItems() ? super.mayPlace(stack) : false;
    }

    @Override
    public boolean mayPickup(Player playerIn) {
        return true;
    }

    @Override
    public int getMaxStackSize(@Nonnull ItemStack stack) {
        return !stack.isStackable() ? 1 : this.inventory.getSlotLimit(this.index);
    }

    public boolean isSameInventory(Slot other) {
        return other instanceof ExtendedItemStackSlot && ((ExtendedItemStackSlot) other).getItemHandler() == this.getItemHandler();
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {
        super.onQuickCraft(oldStackIn, newStackIn);
        this.setChanged();
    }

    @Override
    public void setChanged() {
        this.inventory.markDirty();
    }
}