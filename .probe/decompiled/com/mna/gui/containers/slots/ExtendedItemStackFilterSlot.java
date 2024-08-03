package com.mna.gui.containers.slots;

import com.mna.gui.containers.IExtendedItemHandler;
import com.mna.items.filters.ItemFilter;
import javax.annotation.Nonnull;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.items.SlotItemHandler;

public class ExtendedItemStackFilterSlot extends SlotItemHandler {

    private final IExtendedItemHandler inventory;

    private final int index;

    private ItemFilter itemType;

    private boolean active = true;

    public ExtendedItemStackFilterSlot(IExtendedItemHandler itemHandler, int index, int xPosition, int yPosition, ItemFilter validItems) {
        super(itemHandler, index, xPosition, yPosition);
        this.index = index;
        this.inventory = itemHandler;
        this.itemType = validItems;
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
        return other instanceof ExtendedItemStackFilterSlot && ((ExtendedItemStackFilterSlot) other).getItemHandler() == this.getItemHandler();
    }

    @Override
    public void onQuickCraft(@Nonnull ItemStack oldStackIn, @Nonnull ItemStack newStackIn) {
        super.onQuickCraft(oldStackIn, newStackIn);
        this.setChanged();
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return this.itemType.IsValidItem(stack) ? super.mayPlace(stack) : false;
    }

    @Override
    public void setChanged() {
        this.inventory.markDirty();
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public boolean isActive() {
        return this.active;
    }

    @OnlyIn(Dist.CLIENT)
    public void setActive(boolean active) {
        this.active = active;
    }
}