package io.github.lightman314.lightmanscurrency.util;

import com.google.common.collect.Lists;
import com.mojang.datafixers.util.Pair;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.IItemHandler;

public class InventoryUtil {

    public static SimpleContainer buildInventory(List<ItemStack> list) {
        SimpleContainer inventory = new SimpleContainer(list.size());
        for (int i = 0; i < list.size(); i++) {
            inventory.setItem(i, ((ItemStack) list.get(i)).copy());
        }
        return inventory;
    }

    public static Container buildInventory(ItemStack stack) {
        Container inventory = new SimpleContainer(1);
        inventory.setItem(0, stack);
        return inventory;
    }

    public static Container copyInventory(Container inventory) {
        Container copy = new SimpleContainer(inventory.getContainerSize());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            copy.setItem(i, inventory.getItem(i).copy());
        }
        return copy;
    }

    public static NonNullList<ItemStack> buildList(Container inventory) {
        NonNullList<ItemStack> list = NonNullList.withSize(inventory.getContainerSize(), ItemStack.EMPTY);
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            list.set(i, inventory.getItem(i).copy());
        }
        return list;
    }

    public static List<ItemStack> copyList(List<ItemStack> list) {
        List<ItemStack> result = new ArrayList();
        for (ItemStack stack : list) {
            result.add(stack.copy());
        }
        return result;
    }

    public static int GetItemCount(Container inventory, Item item) {
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() == item) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static int GetItemCount(Container inventory, ItemStack item) {
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (ItemMatches(stack, item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static int GetItemCount(IItemHandler inventory, ItemStack item) {
        int count = 0;
        for (int i = 0; i < inventory.getSlots(); i++) {
            ItemStack stack = inventory.getStackInSlot(i);
            if (ItemMatches(stack, item)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static int GetItemCount(Container inventory, Predicate<ItemStack> filter) {
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (filter.test(stack)) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean RemoveItemCount(Container inventory, Item item, int count) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (stack.getItem() == item) {
                if (stack.getCount() > count) {
                    stack.shrink(count);
                    return true;
                }
                count -= stack.getCount();
                inventory.setItem(i, ItemStack.EMPTY);
            }
        }
        return count <= 0;
    }

    public static boolean RemoveItemCount(Container inventory, ItemStack item) {
        if (GetItemCount(inventory, item) < item.getCount()) {
            return false;
        } else {
            int count = item.getCount();
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (ItemMatches(stack, item)) {
                    int amountToTake = MathUtil.clamp(count, 0, stack.getCount());
                    count -= amountToTake;
                    if (amountToTake == stack.getCount()) {
                        inventory.setItem(i, ItemStack.EMPTY);
                    } else {
                        stack.shrink(amountToTake);
                    }
                }
            }
            return true;
        }
    }

    public static boolean RemoveItemCount(IItemHandler itemHandler, ItemStack item) {
        if (!CanExtractItem(itemHandler, item)) {
            return false;
        } else {
            int amountToRemove = item.getCount();
            for (int i = 0; i < itemHandler.getSlots() && amountToRemove > 0; i++) {
                ItemStack stack = itemHandler.getStackInSlot(i);
                if (ItemMatches(stack, item)) {
                    ItemStack removedStack = itemHandler.extractItem(i, amountToRemove, false);
                    if (ItemMatches(removedStack, item)) {
                        amountToRemove -= removedStack.getCount();
                    } else {
                        itemHandler.insertItem(i, removedStack, false);
                    }
                }
            }
            return true;
        }
    }

    public static boolean CanExtractItem(IItemHandler itemHandler, ItemStack item) {
        int amountToRemove = item.getCount();
        for (int i = 0; i < itemHandler.getSlots() && amountToRemove > 0; i++) {
            if (ItemMatches(itemHandler.getStackInSlot(i), item)) {
                ItemStack removedStack = itemHandler.extractItem(i, amountToRemove, true);
                if (ItemMatches(removedStack, item)) {
                    amountToRemove -= removedStack.getCount();
                }
            }
        }
        return amountToRemove == 0;
    }

    public static int GetItemSpace(Container container, ItemStack item) {
        return GetItemSpace(container, item, 0, container.getContainerSize());
    }

    public static int GetItemSpace(Container container, ItemStack item, int startingIndex, int stopIndex) {
        int count = 0;
        for (int i = startingIndex; i < stopIndex && i < container.getContainerSize(); i++) {
            ItemStack stack = container.getItem(i);
            if (ItemMatches(item, stack)) {
                count += stack.getMaxStackSize() - stack.getCount();
            } else if (stack.isEmpty()) {
                count += stack.getMaxStackSize();
            }
        }
        return count;
    }

    public static int GetItemTagCount(Container inventory, TagKey<Item> itemTag, Item... blacklistItems) {
        List<Item> blacklist = Lists.newArrayList(blacklistItems);
        int count = 0;
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack stack = inventory.getItem(i);
            if (ItemHasTag(stack, itemTag) && !blacklist.contains(stack.getItem())) {
                count += stack.getCount();
            }
        }
        return count;
    }

    public static boolean RemoveItemTagCount(Container inventory, TagKey<Item> itemTag, int count, Item... blacklistItems) {
        if (GetItemTagCount(inventory, itemTag, blacklistItems) < count) {
            return false;
        } else {
            List<Item> blacklist = Lists.newArrayList(blacklistItems);
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                ItemStack stack = inventory.getItem(i);
                if (ItemHasTag(stack, itemTag) && !blacklist.contains(stack.getItem())) {
                    int amountToTake = MathUtil.clamp(count, 0, stack.getCount());
                    count -= amountToTake;
                    if (amountToTake == stack.getCount()) {
                        inventory.setItem(i, ItemStack.EMPTY);
                    } else {
                        stack.shrink(amountToTake);
                    }
                }
            }
            return true;
        }
    }

    public static boolean PutItemStack(Container inventory, ItemStack stack) {
        int amountToMerge = stack.getCount();
        Item mergeItem = stack.getItem();
        List<Pair<Integer, Integer>> mergeOrders = new ArrayList();
        for (int i = 0; i < inventory.getContainerSize() && amountToMerge > 0; i++) {
            ItemStack inventoryStack = inventory.getItem(i);
            if (ItemMatches(stack, inventoryStack) && inventoryStack.getCount() != inventoryStack.getMaxStackSize()) {
                int amountToPlace = MathUtil.clamp(amountToMerge, 0, inventoryStack.getMaxStackSize() - inventoryStack.getCount());
                mergeOrders.add(new Pair(i, amountToPlace));
                amountToMerge -= amountToPlace;
            }
        }
        for (int ix = 0; ix < inventory.getContainerSize() && amountToMerge > 0; ix++) {
            ItemStack inventoryStack = inventory.getItem(ix);
            if (inventoryStack.isEmpty()) {
                int amountToPlace = MathUtil.clamp(amountToMerge, 0, stack.getMaxStackSize());
                mergeOrders.add(new Pair(ix, amountToPlace));
                amountToMerge -= amountToPlace;
            }
        }
        if (amountToMerge > 0) {
            return false;
        } else {
            mergeOrders.forEach(order -> {
                ItemStack itemStack = inventory.getItem((Integer) order.getFirst());
                if (itemStack.isEmpty()) {
                    ItemStack newStack = new ItemStack(mergeItem, (Integer) order.getSecond());
                    if (stack.hasTag()) {
                        newStack.setTag(stack.getTag().copy());
                    }
                    inventory.setItem((Integer) order.getFirst(), newStack);
                } else {
                    itemStack.setCount(itemStack.getCount() + (Integer) order.getSecond());
                }
            });
            return true;
        }
    }

    public static ItemStack TryPutItemStack(Container inventory, ItemStack stack) {
        int amountToMerge = stack.getCount();
        for (int i = 0; i < inventory.getContainerSize() && amountToMerge > 0; i++) {
            ItemStack inventoryStack = inventory.getItem(i);
            if (ItemMatches(stack, inventoryStack) && inventoryStack.getCount() < inventoryStack.getMaxStackSize()) {
                int amountToPlace = MathUtil.clamp(amountToMerge, 0, inventoryStack.getMaxStackSize() - inventoryStack.getCount());
                inventoryStack.grow(amountToPlace);
                inventory.setItem(i, inventoryStack);
                amountToMerge -= amountToPlace;
            }
        }
        for (int ix = 0; ix < inventory.getContainerSize() && amountToMerge > 0; ix++) {
            ItemStack inventoryStack = inventory.getItem(ix);
            if (inventoryStack.isEmpty()) {
                int amountToPlace = MathUtil.clamp(amountToMerge, 0, stack.getMaxStackSize());
                ItemStack newStack = stack.copy();
                newStack.setCount(amountToPlace);
                inventory.setItem(ix, newStack);
                amountToMerge -= amountToPlace;
            }
        }
        if (amountToMerge > 0) {
            ItemStack leftovers = stack.copy();
            leftovers.setCount(amountToMerge);
            return leftovers;
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static boolean CanPutItemStack(Container inventory, ItemStack stack) {
        if (stack.isEmpty()) {
            return true;
        } else {
            int amountToMerge = stack.getCount();
            for (int i = 0; i < inventory.getContainerSize() && amountToMerge > 0; i++) {
                ItemStack inventoryStack = inventory.getItem(i);
                if (ItemMatches(stack, inventoryStack) && inventoryStack.getCount() != inventoryStack.getMaxStackSize()) {
                    int amountToPlace = MathUtil.clamp(amountToMerge, 0, inventoryStack.getMaxStackSize() - inventoryStack.getCount());
                    amountToMerge -= amountToPlace;
                }
            }
            for (int ix = 0; ix < inventory.getContainerSize() && amountToMerge > 0; ix++) {
                ItemStack inventoryStack = inventory.getItem(ix);
                if (inventoryStack.isEmpty()) {
                    int amountToPlace = MathUtil.clamp(amountToMerge, 0, stack.getMaxStackSize());
                    amountToMerge -= amountToPlace;
                }
            }
            return amountToMerge <= 0;
        }
    }

    public static boolean CanPutItemStacks(Container inventory, ItemStack... stacks) {
        return CanPutItemStacks(inventory, Lists.newArrayList(stacks));
    }

    public static boolean CanPutItemStacks(Container inventory, List<ItemStack> stacks) {
        Container copyInventory = new SimpleContainer(inventory.getContainerSize());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            copyInventory.setItem(i, inventory.getItem(i).copy());
        }
        for (int i = 0; i < stacks.size(); i++) {
            if (!PutItemStack(copyInventory, (ItemStack) stacks.get(i))) {
                return false;
            }
        }
        return true;
    }

    public static void MergeStacks(Container inventory) {
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            ItemStack thisStack = inventory.getItem(i);
            if (!thisStack.isEmpty()) {
                int amountWanted = thisStack.getMaxStackSize() - thisStack.getCount();
                if (amountWanted > 0) {
                    for (int j = i + 1; j < inventory.getContainerSize(); j++) {
                        ItemStack nextStack = inventory.getItem(j);
                        if (!nextStack.isEmpty() && nextStack.getItem() == thisStack.getItem() && ItemStackHelper.TagEquals(thisStack, nextStack)) {
                            while (amountWanted > 0 && !nextStack.isEmpty()) {
                                nextStack.setCount(nextStack.getCount() - 1);
                                thisStack.setCount(thisStack.getCount() + 1);
                                amountWanted--;
                            }
                        }
                    }
                }
            }
        }
    }

    public static void saveAllItems(String key, CompoundTag compound, Container inventory) {
        ItemStackHelper.saveAllItems(key, compound, buildList(inventory));
    }

    public static SimpleContainer loadAllItems(String key, CompoundTag compound, int inventorySize) {
        NonNullList<ItemStack> tempInventory = NonNullList.withSize(inventorySize, ItemStack.EMPTY);
        ItemStackHelper.loadAllItems(key, compound, tempInventory);
        return buildInventory(tempInventory);
    }

    public static void encodeItems(Container inventory, FriendlyByteBuf buffer) {
        CompoundTag tag = new CompoundTag();
        saveAllItems("ITEMS", tag, inventory);
        buffer.writeInt(inventory.getContainerSize());
        buffer.writeNbt(tag);
    }

    public static SimpleContainer decodeItems(FriendlyByteBuf buffer) {
        int inventorySize = buffer.readInt();
        return loadAllItems("ITEMS", buffer.readAnySizeNbt(), inventorySize);
    }

    public static SimpleContainer copy(Container inventory) {
        SimpleContainer copy = new SimpleContainer(inventory.getContainerSize());
        for (int i = 0; i < inventory.getContainerSize(); i++) {
            copy.setItem(i, inventory.getItem(i).copy());
        }
        return copy;
    }

    public static void dumpContents(Level level, BlockPos pos, Container inventory) {
        if (!level.isClientSide) {
            for (int i = 0; i < inventory.getContainerSize(); i++) {
                dumpContents(level, pos, inventory.getItem(i));
            }
        }
    }

    public static void dumpContents(Level level, BlockPos pos, List<ItemStack> inventory) {
        if (!level.isClientSide) {
            for (ItemStack itemStack : inventory) {
                dumpContents(level, pos, itemStack);
            }
        }
    }

    public static void dumpContents(Level level, BlockPos pos, ItemStack stack) {
        if (!level.isClientSide) {
            if (!stack.isEmpty()) {
                ItemEntity entity = new ItemEntity(level, (double) pos.m_123341_(), (double) pos.m_123342_(), (double) pos.m_123343_(), stack);
                level.m_7967_(entity);
            }
        }
    }

    public static List<ItemStack> combineQueryItems(ItemStack... items) {
        return combineQueryItems(Lists.newArrayList(items));
    }

    public static List<ItemStack> combineQueryItems(List<ItemStack> items) {
        List<ItemStack> itemList = new ArrayList();
        for (ItemStack item : items) {
            boolean addNew = true;
            for (int i = 0; i < itemList.size() && addNew; i++) {
                if (ItemMatches(item, (ItemStack) itemList.get(i))) {
                    ((ItemStack) itemList.get(i)).grow(item.getCount());
                    addNew = false;
                }
            }
            if (addNew && !item.isEmpty()) {
                itemList.add(item.copy());
            }
        }
        return itemList;
    }

    public static List<ItemRequirement> combineRequirements(ItemRequirement... requirements) {
        List<ItemRequirement> list = new ArrayList();
        for (ItemRequirement requirement : requirements) {
            boolean addNew = !requirement.isNull();
            for (int i = 0; i < list.size() && addNew; i++) {
                if (((ItemRequirement) list.get(i)).filter.equals(requirement.filter)) {
                    list.set(i, requirement.merge((ItemRequirement) list.get(i)));
                    addNew = false;
                }
            }
            if (addNew) {
                list.add(requirement);
            }
        }
        return list;
    }

    public static boolean ItemMatches(ItemStack stack1, ItemStack stack2) {
        return stack1.getItem() == stack2.getItem() ? ItemStackHelper.TagEquals(stack1, stack2) : false;
    }

    public static boolean ItemsFullyMatch(ItemStack stack1, ItemStack stack2) {
        return ItemMatches(stack1, stack2) && stack1.getCount() == stack2.getCount();
    }

    public static boolean ContainerMatches(@Nonnull Container container1, @Nonnull Container container2) {
        if (container1.getContainerSize() != container2.getContainerSize()) {
            return false;
        } else {
            for (int i = 0; i < container1.getContainerSize(); i++) {
                if (!ItemsFullyMatch(container1.getItem(i), container2.getItem(i))) {
                    return false;
                }
            }
            return true;
        }
    }

    public static boolean ItemHasTag(ItemStack item, TagKey<Item> tag) {
        return item.getTags().anyMatch(t -> t.equals(tag));
    }

    public static int safeGiveToPlayer(Inventory inv, ItemStack stack) {
        int i = inv.getSlotWithRemainingSpace(stack);
        if (i == -1) {
            i = inv.getFreeSlot();
        }
        if (i >= 0) {
            ItemStack stackInSlot = inv.getItem(i);
            int putCount = Math.min(stack.getCount(), stackInSlot.isEmpty() ? stack.getMaxStackSize() : stackInSlot.getMaxStackSize() - stackInSlot.getCount());
            if (putCount > 0) {
                if (stackInSlot.isEmpty()) {
                    stackInSlot = stack.copy();
                    stackInSlot.setCount(putCount);
                } else {
                    stackInSlot.grow(putCount);
                }
                stack.shrink(putCount);
                inv.setItem(i, stackInSlot);
                inv.setChanged();
            }
            return putCount;
        } else {
            return 0;
        }
    }

    public static int totalItemCount(@Nonnull List<ItemStack> list) {
        int count = 0;
        for (ItemStack s : list) {
            count += s.getCount();
        }
        return count;
    }
}