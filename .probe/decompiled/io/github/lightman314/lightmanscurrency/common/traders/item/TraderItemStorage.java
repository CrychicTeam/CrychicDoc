package io.github.lightman314.lightmanscurrency.common.traders.item;

import com.google.common.collect.Lists;
import io.github.lightman314.lightmanscurrency.common.blockentity.handler.ICanCopy;
import io.github.lightman314.lightmanscurrency.util.InventoryUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

public class TraderItemStorage implements IItemHandler, ICanCopy<TraderItemStorage> {

    private final TraderItemStorage.ITraderItemFilter filter;

    private final List<ItemStack> storage = new ArrayList();

    public TraderItemStorage(@Nonnull TraderItemStorage.ITraderItemFilter filter) {
        this.filter = filter;
    }

    public CompoundTag save(CompoundTag compound, String tag) {
        ListTag list = new ListTag();
        for (ItemStack item : this.storage) {
            if (!item.isEmpty()) {
                CompoundTag itemTag = new CompoundTag();
                item.save(itemTag);
                itemTag.putInt("Count", item.getCount());
                list.add(itemTag);
            }
        }
        compound.put(tag, list);
        return compound;
    }

    public void load(CompoundTag compound, String tag) {
        if (compound.contains(tag, 9)) {
            ListTag list = compound.getList(tag, 10);
            this.storage.clear();
            for (int i = 0; i < list.size(); i++) {
                CompoundTag itemTag = list.getCompound(i);
                ItemStack item = ItemStack.of(itemTag);
                item.setCount(itemTag.getInt("Count"));
                if (!item.isEmpty()) {
                    this.storage.add(item);
                }
            }
        }
    }

    public List<ItemStack> getContents() {
        return this.storage;
    }

    public List<ItemStack> getSplitContents() {
        List<ItemStack> contents = new ArrayList();
        for (ItemStack s : this.storage) {
            ItemStack stack = s.copy();
            int maxCount = stack.getMaxStackSize();
            while (stack.getCount() > maxCount) {
                contents.add(stack.split(maxCount));
            }
            contents.add(stack);
        }
        return contents;
    }

    public int getSlotCount() {
        return this.storage.size();
    }

    public boolean hasItem(ItemStack item) {
        for (ItemStack stack : this.storage) {
            if (InventoryUtil.ItemMatches(stack, item)) {
                return stack.getCount() >= item.getCount();
            }
        }
        return false;
    }

    public boolean hasItems(ItemStack... items) {
        for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
            if (!this.hasItem(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean allowItem(ItemStack item) {
        return item.isEmpty() ? false : this.filter.isItemRelevant(item);
    }

    public int getMaxAmount() {
        return this.filter.getStorageStackLimit();
    }

    public int getItemCount(ItemStack item) {
        for (ItemStack stack : this.storage) {
            if (InventoryUtil.ItemMatches(item, stack)) {
                return stack.getCount();
            }
        }
        return 0;
    }

    public int getItemCount(Predicate<ItemStack> filter) {
        int count = 0;
        for (ItemStack stack : this.storage) {
            if (filter.test(stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public int getItemTagCount(TagKey<Item> itemTag, Item... blacklistItems) {
        List<Item> blacklist = Lists.newArrayList(blacklistItems);
        int count = 0;
        for (ItemStack stack : this.storage) {
            if (InventoryUtil.ItemHasTag(stack, itemTag) && !blacklist.contains(stack.getItem())) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public int getFittableAmount(ItemStack item) {
        return !this.allowItem(item) ? 0 : this.getMaxAmount() - this.getItemCount(item);
    }

    public boolean canFitItem(ItemStack item) {
        return this.getFittableAmount(item) >= item.getCount();
    }

    public boolean canFitItems(ItemStack... items) {
        for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
            if (!this.canFitItem(item)) {
                return false;
            }
        }
        return true;
    }

    public boolean canFitItems(List<ItemStack> items) {
        if (items == null) {
            return true;
        } else {
            for (ItemStack item : InventoryUtil.combineQueryItems(items)) {
                if (!this.canFitItem(item)) {
                    return false;
                }
            }
            return true;
        }
    }

    public boolean addItem(ItemStack item) {
        if (!this.canFitItem(item)) {
            return false;
        } else {
            this.forceAddItem(item);
            return true;
        }
    }

    public void tryAddItem(ItemStack item) {
        if (this.allowItem(item)) {
            int amountToAdd = Math.min(item.getCount(), this.getFittableAmount(item));
            if (amountToAdd > 0) {
                ItemStack addStack = item.split(amountToAdd);
                this.forceAddItem(addStack);
            }
        }
    }

    public void forceAddItem(ItemStack item) {
        if (!item.isEmpty()) {
            for (ItemStack stack : this.storage) {
                if (InventoryUtil.ItemMatches(stack, item)) {
                    stack.grow(item.getCount());
                    return;
                }
            }
            this.storage.add(item.copy());
        }
    }

    public ItemStack removeItem(ItemStack item) {
        if (!this.hasItem(item)) {
            return ItemStack.EMPTY;
        } else {
            for (int i = 0; i < this.storage.size(); i++) {
                ItemStack stack = (ItemStack) this.storage.get(i);
                if (InventoryUtil.ItemMatches(item, stack)) {
                    int amountToRemove = Math.min(item.getCount(), item.getMaxStackSize());
                    ItemStack output = stack.split(amountToRemove);
                    if (stack.isEmpty()) {
                        this.storage.remove(i);
                    }
                    return output;
                }
            }
            return ItemStack.EMPTY;
        }
    }

    public void removeItemTagCount(TagKey<Item> itemTag, int count, List<ItemStack> ignoreIfPossible, Item... blacklistItems) {
        List<Item> blacklist = Lists.newArrayList(blacklistItems);
        for (int i = 0; i < this.storage.size() && count > 0; i++) {
            ItemStack stack = (ItemStack) this.storage.get(i);
            if (InventoryUtil.ItemHasTag(stack, itemTag) && !blacklist.contains(stack.getItem()) && !ListContains(ignoreIfPossible, stack)) {
                int amountToTake = Math.min(count, stack.getCount());
                count -= amountToTake;
                stack.shrink(amountToTake);
                if (stack.isEmpty()) {
                    this.storage.remove(i);
                    i--;
                }
            }
        }
        for (int ix = 0; ix < this.storage.size() && count > 0; ix++) {
            ItemStack stack = (ItemStack) this.storage.get(ix);
            if (InventoryUtil.ItemHasTag(stack, itemTag) && !blacklist.contains(stack.getItem())) {
                int amountToTake = Math.min(count, stack.getCount());
                count -= amountToTake;
                stack.shrink(amountToTake);
                if (stack.isEmpty()) {
                    this.storage.remove(ix);
                    ix--;
                }
            }
        }
    }

    private static boolean ListContains(List<ItemStack> list, ItemStack stack) {
        for (ItemStack item : list) {
            if (InventoryUtil.ItemMatches(item, stack)) {
                return true;
            }
        }
        return false;
    }

    public TraderItemStorage copy() {
        CompoundTag tag = this.save(new CompoundTag(), "copy");
        TraderItemStorage copy = new TraderItemStorage(this.filter);
        copy.load(tag, "copy");
        return copy;
    }

    @Override
    public int getSlots() {
        return this.storage.size() + 1;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return slot >= 0 && slot < this.storage.size() ? (ItemStack) this.storage.get(slot) : ItemStack.EMPTY;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        int amountToAdd = Math.min(stack.getCount(), this.getFittableAmount(stack));
        ItemStack remainder = stack.copy();
        if (amountToAdd >= stack.getCount()) {
            remainder = ItemStack.EMPTY;
        } else {
            remainder.shrink(amountToAdd);
        }
        if (!simulate && amountToAdd > 0) {
            ItemStack addedStack = stack.copy();
            addedStack.setCount(amountToAdd);
            this.forceAddItem(addedStack);
        }
        return remainder;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        ItemStack stackInSlot = this.getStackInSlot(slot);
        int amountToRemove = Math.min(amount, stackInSlot.getCount());
        ItemStack removedStack = stackInSlot.copy();
        if (amountToRemove > 0) {
            removedStack.setCount(amountToRemove);
        } else {
            removedStack = ItemStack.EMPTY;
        }
        if (!simulate && amountToRemove > 0) {
            this.removeItem(removedStack);
        }
        return removedStack;
    }

    @Override
    public int getSlotLimit(int slot) {
        return this.getMaxAmount();
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.allowItem(stack);
    }

    public interface ITraderItemFilter {

        boolean isItemRelevant(ItemStack var1);

        int getStorageStackLimit();
    }

    public static class LockedTraderStorage extends TraderItemStorage {

        public LockedTraderStorage(TraderItemStorage.ITraderItemFilter filter, List<ItemStack> startingInventory) {
            super(filter);
            for (ItemStack item : startingInventory) {
                this.forceAddItem(item);
            }
        }

        @Override
        public boolean allowItem(ItemStack item) {
            return false;
        }
    }
}