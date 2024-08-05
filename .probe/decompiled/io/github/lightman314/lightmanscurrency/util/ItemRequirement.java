package io.github.lightman314.lightmanscurrency.util;

import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.function.Predicate;
import net.minecraft.world.Container;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.items.IItemHandler;

public final class ItemRequirement implements Predicate<ItemStack> {

    public static final ItemRequirement NULL = of(s -> false, 0);

    public final Predicate<ItemStack> filter;

    public final int count;

    private ItemRequirement(Predicate<ItemStack> filter, int count) {
        this.filter = filter;
        this.count = count;
    }

    public boolean test(ItemStack stack) {
        return this.filter.test(stack);
    }

    public boolean isNull() {
        return this == NULL;
    }

    public ItemRequirement merge(ItemRequirement other) {
        return of(this.filter, this.count + other.count);
    }

    public static ItemRequirement of(Predicate<ItemStack> filter, int count) {
        return new ItemRequirement(filter, count);
    }

    public static ItemRequirement of(ItemStack stack) {
        return stack != null && !stack.isEmpty() ? of(s -> InventoryUtil.ItemMatches(s, stack), stack.getCount()) : NULL;
    }

    public static ItemRequirement ofItemNoNBT(ItemStack stack) {
        return stack != null && !stack.isEmpty() ? of(stack.getItem(), stack.getCount()) : NULL;
    }

    public static ItemRequirement of(Item item, int count) {
        return item != null && item != Items.AIR ? of(s -> s.getItem() == item, count) : NULL;
    }

    public static List<ItemStack> getRandomItemsMatchingRequirements(IItemHandler container, ItemRequirement requirement1, ItemRequirement requirement2) {
        if (requirement1.isNull() && requirement2.isNull()) {
            return null;
        } else if (requirement1.isNull()) {
            List<ItemStack> validItems = getValidItems(container, requirement2);
            return validItems.size() == 0 ? null : Lists.newArrayList(new ItemStack[] { getRandomItem(validItems, requirement2.count) });
        } else if (requirement2.isNull()) {
            List<ItemStack> validItems = getValidItems(container, requirement1);
            return validItems.size() == 0 ? null : Lists.newArrayList(new ItemStack[] { getRandomItem(validItems, requirement1.count) });
        } else {
            List<ItemStack> validItems1 = getValidItems(container, requirement1);
            List<ItemStack> validItems2 = getValidItems(container, requirement2);
            for (int x = 0; x < validItems1.size(); x++) {
                ItemStack s1 = (ItemStack) validItems1.get(x);
                for (int y = 0; y < validItems2.size(); y++) {
                    ItemStack s2 = (ItemStack) validItems2.get(y);
                    if (InventoryUtil.ItemMatches(s1, s2)) {
                        int count = InventoryUtil.GetItemCount(container, s1);
                        if (count < requirement1.count + requirement2.count) {
                            if (validItems2.size() == 1) {
                                validItems1.remove(s1);
                                x--;
                            } else {
                                validItems2.remove(s2);
                                y--;
                            }
                        }
                    }
                }
            }
            return validItems1.size() > 0 && validItems2.size() > 0 ? Lists.newArrayList(new ItemStack[] { getRandomItem(validItems1, requirement1.count), getRandomItem(validItems2, requirement2.count) }) : null;
        }
    }

    public static List<ItemStack> getValidItems(IItemHandler container, ItemRequirement requirement) {
        List<ItemStack> validItems = new ArrayList();
        for (int i = 0; i < container.getSlots(); i++) {
            ItemStack stack = container.getStackInSlot(i);
            if (requirement.test(stack) && InventoryUtil.GetItemCount(container, stack) >= requirement.count && isNotInList(validItems, stack)) {
                validItems.add(stack.copy());
            }
        }
        return validItems;
    }

    public static boolean isNotInList(List<ItemStack> list, ItemStack stack) {
        for (ItemStack i : list) {
            if (InventoryUtil.ItemMatches(i, stack)) {
                return false;
            }
        }
        return true;
    }

    public static ItemStack getRandomItem(List<ItemStack> validItems, int count) {
        if (validItems.size() == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack stack = (ItemStack) validItems.get(new Random().nextInt(validItems.size()));
            stack.setCount(count);
            return stack;
        }
    }

    public static List<ItemStack> getFirstItemsMatchingRequirements(Container container, ItemRequirement... requirements) {
        List<ItemStack> results = new ArrayList();
        Map<Integer, Integer> consumedItems = new HashMap();
        for (ItemRequirement requirement : requirements) {
            int leftToConsume = requirement.count;
            for (int i = 0; i < container.getContainerSize() && leftToConsume > 0; i++) {
                ItemStack stack = container.getItem(i);
                if (requirement.test(stack)) {
                    int alreadyConsumed = (Integer) consumedItems.getOrDefault(i, 0);
                    int consumeCount = Math.min(leftToConsume, stack.getCount() - alreadyConsumed);
                    leftToConsume -= consumeCount;
                    if (consumeCount > 0) {
                        consumedItems.put(i, alreadyConsumed + consumeCount);
                        boolean query = true;
                        for (int x = 0; x < results.size() && query; x++) {
                            if (InventoryUtil.ItemMatches((ItemStack) results.get(x), stack)) {
                                query = false;
                                ((ItemStack) results.get(x)).grow(consumeCount);
                            }
                        }
                        if (query) {
                            ItemStack result = stack.copy();
                            result.setCount(consumeCount);
                            results.add(result);
                        }
                    }
                }
            }
            if (leftToConsume > 0) {
                return null;
            }
        }
        return results;
    }

    public static List<ItemStack> getFirstItemsMatchingRequirements(IItemHandler container, ItemRequirement... requirements) {
        List<ItemStack> results = new ArrayList();
        Map<Integer, Integer> consumedItems = new HashMap();
        for (ItemRequirement requirement : requirements) {
            int leftToConsume = requirement.count;
            for (int i = 0; i < container.getSlots() && leftToConsume > 0; i++) {
                ItemStack stack = container.getStackInSlot(i);
                if (requirement.test(stack)) {
                    int alreadyConsumed = (Integer) consumedItems.getOrDefault(i, 0);
                    int consumeCount = Math.min(leftToConsume, stack.getCount() - alreadyConsumed);
                    leftToConsume -= consumeCount;
                    if (consumeCount > 0) {
                        consumedItems.put(i, alreadyConsumed + consumeCount);
                        boolean query = true;
                        for (int x = 0; x < results.size() && query; x++) {
                            if (InventoryUtil.ItemMatches((ItemStack) results.get(x), stack)) {
                                query = false;
                                ((ItemStack) results.get(x)).grow(consumeCount);
                            }
                        }
                        if (query) {
                            ItemStack result = stack.copy();
                            result.setCount(consumeCount);
                            results.add(result);
                        }
                    }
                }
            }
            if (leftToConsume > 0) {
                return null;
            }
        }
        return results;
    }
}