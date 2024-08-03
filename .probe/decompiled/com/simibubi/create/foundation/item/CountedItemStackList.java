package com.simibubi.create.foundation.item;

import com.simibubi.create.foundation.blockEntity.behaviour.filtering.FilteringBehaviour;
import com.simibubi.create.foundation.utility.IntAttached;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemHandlerHelper;

public class CountedItemStackList {

    Map<Item, Set<CountedItemStackList.ItemStackEntry>> items = new HashMap();

    public CountedItemStackList(IItemHandler inventory, FilteringBehaviour filteringBehaviour) {
        for (int slot = 0; slot < inventory.getSlots(); slot++) {
            ItemStack extractItem = inventory.extractItem(slot, inventory.getSlotLimit(slot), true);
            if (filteringBehaviour.test(extractItem)) {
                this.add(extractItem);
            }
        }
    }

    public Stream<IntAttached<MutableComponent>> getTopNames(int limit) {
        return this.items.values().stream().flatMap(Collection::stream).sorted(IntAttached.comparator()).limit((long) limit).map(entry -> IntAttached.with(entry.count(), entry.stack().getHoverName().copy()));
    }

    public void add(ItemStack stack) {
        this.add(stack, stack.getCount());
    }

    public void add(ItemStack stack, int amount) {
        if (!stack.isEmpty()) {
            Set<CountedItemStackList.ItemStackEntry> stackSet = this.getOrCreateItemSet(stack);
            for (CountedItemStackList.ItemStackEntry entry : stackSet) {
                if (entry.matches(stack)) {
                    entry.grow(amount);
                    return;
                }
            }
            stackSet.add(new CountedItemStackList.ItemStackEntry(stack, amount));
        }
    }

    private Set<CountedItemStackList.ItemStackEntry> getOrCreateItemSet(ItemStack stack) {
        if (!this.items.containsKey(stack.getItem())) {
            this.items.put(stack.getItem(), new HashSet());
        }
        return this.getItemSet(stack);
    }

    private Set<CountedItemStackList.ItemStackEntry> getItemSet(ItemStack stack) {
        return (Set<CountedItemStackList.ItemStackEntry>) this.items.get(stack.getItem());
    }

    public static class ItemStackEntry extends IntAttached<ItemStack> {

        public ItemStackEntry(ItemStack stack) {
            this(stack, stack.getCount());
        }

        public ItemStackEntry(ItemStack stack, int amount) {
            super(amount, stack);
        }

        public boolean matches(ItemStack other) {
            return ItemHandlerHelper.canItemStacksStack(other, this.stack());
        }

        public ItemStack stack() {
            return this.getSecond();
        }

        public void grow(int amount) {
            this.setFirst(Integer.valueOf(this.getFirst() + amount));
        }

        public int count() {
            return this.getFirst();
        }
    }
}