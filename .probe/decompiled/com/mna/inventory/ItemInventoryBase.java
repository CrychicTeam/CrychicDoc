package com.mna.inventory;

import com.mna.ManaAndArtifice;
import com.mna.api.ManaAndArtificeMod;
import com.mna.inventory.stack_extension.AbstractItemHandler;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.ItemStack;

public class ItemInventoryBase extends AbstractItemHandler {

    protected final ItemStack stack;

    private static final String TAG_ITEMS = "Items";

    public ItemInventoryBase(ItemStack stack) {
        super(54);
        this.stack = stack;
        this.setup();
    }

    public ItemInventoryBase(ItemStack stack, int numSlots) {
        super(numSlots);
        this.stack = stack;
        this.setup();
    }

    public void setup() {
        this.readItemStack();
    }

    public ItemStack getStack() {
        return this.stack;
    }

    public void readItemStack() {
        if (this.stack.getTag() != null && this.stack.getTag().contains("Items")) {
            if (this.stack.getTag().getCompound("Items").contains("slots")) {
                this.deserialize(this.stack.getTag().getCompound("Items"));
            } else {
                this.readNBT(this.stack.getTag());
            }
        }
    }

    public void writeItemStack() {
        if (this.isEmpty()) {
            this.stack.removeTagKey("Items");
        } else {
            CompoundTag tag = this.stack.getOrCreateTag();
            tag.put("Items", this.serialize());
        }
    }

    protected boolean isSameItem(ItemStack a, ItemStack b) {
        return a.getItem() == b.getItem() && ManaAndArtificeMod.getItemHelper().AreTagsEqual(a, b);
    }

    protected void moveItemToEmptySlots(ItemStack stack) {
        for (int i = 0; i < this.size(); i++) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack.isEmpty()) {
                this.setStackInSlot(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }
    }

    protected void moveItemToOccupiedSlotsWithSameType(ItemStack stack) {
        for (int i = 0; i < this.size(); i++) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (this.isSameItem(itemstack, stack)) {
                this.moveItemsBetweenStacks(stack, itemstack, i);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }
    }

    protected void moveItemsBetweenStacks(ItemStack source, ItemStack dest, int slot) {
        int i = Math.min(this.getSlotLimit(slot, dest), dest.getMaxStackSize());
        int j = Math.min(source.getCount(), i - dest.getCount());
        if (j > 0) {
            dest.grow(j);
            source.shrink(j);
            this.markDirty();
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return true;
    }

    private void readNBT(CompoundTag compound) {
        ManaAndArtifice.LOGGER.info("Upconverting old ritual kit to Practitioner's Pouch");
        NonNullList<ItemStack> list = NonNullList.withSize(this.getSlots(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(compound, list);
        int mySlotIndex = 0;
        for (int i = 0; i < list.size(); i++) {
            if (!list.get(i).isEmpty()) {
                this.setStackInSlot(mySlotIndex++, list.get(i));
            }
        }
        this.writeItemStack();
    }

    public NonNullList<ItemStack> getNonAirItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        this.stacks.forEach(is -> {
            if (!is.isEmpty()) {
                items.add(is.getStackOriginal());
            }
        });
        return items;
    }

    public NonNullList<ItemStack> getAllItems() {
        NonNullList<ItemStack> items = NonNullList.create();
        this.stacks.forEach(is -> items.add(is.getStackOriginal()));
        return items;
    }
}