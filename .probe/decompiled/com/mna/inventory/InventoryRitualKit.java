package com.mna.inventory;

import com.mna.items.filters.BottledWeaveItemFilter;
import com.mna.items.filters.MarkingRuneFilter;
import com.mna.items.filters.MoteItemFilter;
import com.mna.items.filters.RuneItemFilter;
import com.mna.items.ritual.ItemPractitionersPouch;
import com.mna.items.ritual.PractitionersPouchPatches;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class InventoryRitualKit extends ItemInventoryBase {

    public static final int GENERAL_INVENTORY_START = 0;

    public static final int GENERAL_INVENTORY_END = 20;

    public static final int CONVEYANCE_INVENTORY_START = 21;

    public static final int CONVEYANCE_INVENTORY_END = 21;

    public static final int GLYPH_INVENTORY_START = 22;

    public static final int GLYPH_INVENTORY_END = 37;

    public static final int MOTE_INVENTORY_START = 38;

    public static final int MOTE_INVENTORY_END = 49;

    public static final int WEAVE_INVENTORY_START = 50;

    public static final int WEAVE_INVENTORY_END = 61;

    public static final int VOID_INVENTORY_START = 62;

    public static final int VOID_INVENTORY_END = 77;

    private final int slotLimit;

    public InventoryRitualKit(ItemStack bag) {
        super(bag, 78);
        int maxSize = ((ItemPractitionersPouch) bag.getItem()).getPatchLevel(bag, PractitionersPouchPatches.DEPTH);
        if (maxSize >= 2) {
            this.slotLimit = 2048;
        } else if (maxSize >= 1) {
            this.slotLimit = 256;
        } else {
            this.slotLimit = 64;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        if (slot >= 62 && slot <= 77) {
            return 1;
        } else {
            return slot == 21 ? 1 : this.slotLimit;
        }
    }

    @Override
    public int getSlotLimit(int slot, ItemStack stack) {
        return stack.isStackable() ? this.getSlotLimit(slot) : 1;
    }

    private boolean isPatchEnabled(PractitionersPouchPatches patch) {
        return ((ItemPractitionersPouch) this.getStack().getItem()).getPatchLevel(this.getStack(), patch) > 0;
    }

    public boolean canAddItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.CONVEYANCE) && new MarkingRuneFilter().IsValidItem(stack) && this.canAddItem(stack, 21, 21)) {
            return true;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.GLYPH) && new RuneItemFilter().IsValidItem(stack) && this.canAddItem(stack, 22, 37)) {
            return true;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.GLYPH) && new MoteItemFilter().IsValidItem(stack) && this.canAddItem(stack, 38, 49)) {
            return true;
        } else {
            return this.isPatchEnabled(PractitionersPouchPatches.GLYPH) && new BottledWeaveItemFilter().IsValidItem(stack) && this.canAddItem(stack, 50, 61) ? true : this.canAddItem(stack, 0, 20);
        }
    }

    protected boolean canAddItem(ItemStack stack, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack invStack = this.getStackInSlot(i);
            if (invStack.isEmpty() || this.isSameItem(invStack, stack) && invStack.getCount() < invStack.getMaxStackSize()) {
                return true;
            }
        }
        return false;
    }

    public boolean canMergeItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return false;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.CONVEYANCE) && new MarkingRuneFilter().IsValidItem(stack) && this.canAddItem(stack, 21, 21)) {
            return true;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.GLYPH) && new RuneItemFilter().IsValidItem(stack) && this.canAddItem(stack, 22, 37)) {
            return true;
        } else if (this.isPatchEnabled(PractitionersPouchPatches.MOTE) && new MoteItemFilter().IsValidItem(stack) && this.canAddItem(stack, 38, 49)) {
            return true;
        } else {
            return this.isPatchEnabled(PractitionersPouchPatches.WEAVE) && new BottledWeaveItemFilter().IsValidItem(stack) && this.canAddItem(stack, 50, 61) ? true : this.canMergeItem(stack, 0, 20);
        }
    }

    protected boolean canMergeItem(ItemStack stack, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack invStack = this.getStackInSlot(i);
            if (this.isSameItem(invStack, stack) && invStack.getCount() < this.getSlotLimit(i, invStack)) {
                return true;
            }
        }
        return false;
    }

    public ItemStack addItem(ItemStack stack) {
        if (stack.isEmpty()) {
            return stack;
        } else {
            ItemStack opStack = stack.copy();
            if (this.isPatchEnabled(PractitionersPouchPatches.CONVEYANCE) && new MarkingRuneFilter().IsValidItem(opStack)) {
                opStack = this.addItem(opStack, 21, 21);
                if (opStack.isEmpty()) {
                    return opStack;
                }
            }
            if (this.isPatchEnabled(PractitionersPouchPatches.GLYPH) && new RuneItemFilter().IsValidItem(stack)) {
                opStack = this.addItem(opStack, 22, 37);
                if (opStack.isEmpty()) {
                    return opStack;
                }
            }
            if (this.isPatchEnabled(PractitionersPouchPatches.MOTE) && new MoteItemFilter().IsValidItem(stack)) {
                opStack = this.addItem(opStack, 38, 49);
                if (opStack.isEmpty()) {
                    return opStack;
                }
            }
            if (this.isPatchEnabled(PractitionersPouchPatches.WEAVE) && new BottledWeaveItemFilter().IsValidItem(stack)) {
                opStack = this.addItem(opStack, 50, 61);
                if (opStack.isEmpty()) {
                    return opStack;
                }
            }
            opStack = this.mergeItem(opStack, 0, 20);
            return this.shouldVoidItem(opStack) ? ItemStack.EMPTY : opStack;
        }
    }

    protected ItemStack mergeItem(ItemStack stack, int startSlot, int endSlot) {
        ItemStack itemstack = stack.copy();
        this.moveItemToOccupiedSlotsWithSameType(itemstack, startSlot, endSlot);
        return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
    }

    protected ItemStack addItem(ItemStack stack, int startSlot, int endSlot) {
        ItemStack itemstack = stack.copy();
        this.moveItemToOccupiedSlotsWithSameType(itemstack, startSlot, endSlot);
        if (itemstack.isEmpty()) {
            return ItemStack.EMPTY;
        } else {
            this.moveItemToEmptySlots(itemstack, startSlot, endSlot);
            return itemstack.isEmpty() ? ItemStack.EMPTY : itemstack;
        }
    }

    protected void moveItemToOccupiedSlotsWithSameType(ItemStack stack, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (this.isSameItem(itemstack, stack)) {
                this.moveItemsBetweenStacks(stack, itemstack, i);
                if (stack.isEmpty()) {
                    return;
                }
            }
        }
    }

    protected void moveItemToEmptySlots(ItemStack stack, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack itemstack = this.getStackInSlot(i);
            if (itemstack.isEmpty()) {
                this.setStackInSlot(i, stack.copy());
                stack.setCount(0);
                return;
            }
        }
    }

    public boolean containsItem(ItemStack stack, int startSlot, int endSlot) {
        for (int i = startSlot; i <= endSlot; i++) {
            ItemStack invStack = this.getStackInSlot(i);
            if (this.isSameItem(invStack, stack)) {
                return true;
            }
        }
        return false;
    }

    public boolean shouldVoidItem(ItemStack stack) {
        return !this.isPatchEnabled(PractitionersPouchPatches.VOID) ? false : this.containsItem(stack, 62, 77);
    }

    @Override
    protected void moveItemsBetweenStacks(ItemStack source, ItemStack dest, int slot) {
        int i = this.getSlotLimit(slot, dest);
        int j = Math.min(source.getCount(), i - dest.getCount());
        if (j > 0) {
            dest.grow(j);
            source.shrink(j);
            this.markDirty();
        }
    }

    public int countItem(Item item) {
        int count = 0;
        for (int i = 0; i < this.getSlots(); i++) {
            ItemStack stack = this.getStackInSlot(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }
}