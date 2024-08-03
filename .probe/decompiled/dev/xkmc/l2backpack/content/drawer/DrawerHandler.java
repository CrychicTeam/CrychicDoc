package dev.xkmc.l2backpack.content.drawer;

import dev.xkmc.l2serial.serialization.SerialClass;
import dev.xkmc.l2serial.serialization.SerialClass.SerialField;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

@SerialClass
public class DrawerHandler implements IDrawerHandler {

    @SerialField(toClient = true)
    public Item item = Items.AIR;

    @SerialField(toClient = true)
    public int count = 0;

    @SerialField(toClient = true)
    public CompoundTag config = new CompoundTag();

    private final DrawerBlockEntity parent;

    public DrawerHandler(DrawerBlockEntity parent) {
        this.parent = parent;
    }

    @Override
    public int getSlots() {
        return 2;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot == 1 ? ItemStack.EMPTY : new ItemStack(this.item, this.count);
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        if (stack.isEmpty()) {
            return ItemStack.EMPTY;
        } else if (stack.hasTag()) {
            return stack;
        } else {
            int max = BaseDrawerItem.getStacking(this.config) * stack.getMaxStackSize();
            if (this.count >= max) {
                return stack;
            } else if (this.item == Items.AIR) {
                int toInsert = Math.min(max, stack.getCount());
                if (!simulate) {
                    this.item = stack.getItem();
                    this.count = toInsert;
                    this.parent.sync();
                }
                if (toInsert == stack.getCount()) {
                    return ItemStack.EMPTY;
                } else {
                    ItemStack ans = stack.copy();
                    ans.setCount(stack.getCount() - toInsert);
                    return ans;
                }
            } else if (this.item != stack.getItem()) {
                return stack;
            } else {
                int toInsertx = Math.min(max - this.count, stack.getCount());
                if (!simulate) {
                    this.item = stack.getItem();
                    this.count += toInsertx;
                    this.parent.sync();
                }
                if (toInsertx == stack.getCount()) {
                    return ItemStack.EMPTY;
                } else {
                    ItemStack ans = stack.copy();
                    ans.setCount(stack.getCount() - toInsertx);
                    return ans;
                }
            }
        }
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        if (this.item != Items.AIR && this.count != 0) {
            int toExtract = Math.min(amount, this.count);
            ItemStack ans = new ItemStack(this.item, toExtract);
            if (!simulate) {
                this.count -= toExtract;
                if (this.count == 0) {
                    this.item = Items.AIR;
                }
                this.parent.sync();
            }
            return ans;
        } else {
            return ItemStack.EMPTY;
        }
    }

    @Override
    public int getSlotLimit(int slot) {
        return BaseDrawerItem.getStacking(this.config) * this.item.getMaxStackSize();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return stack.getTag() == null && (this.item == Items.AIR || stack.getItem() == this.item);
    }
}