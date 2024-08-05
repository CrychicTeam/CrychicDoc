package dev.ftb.mods.ftbquests.integration.item_filtering;

import dev.ftb.mods.ftbquests.api.ItemFilterAdapter;
import dev.ftb.mods.ftbquests.util.NBTUtils;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import net.minecraft.world.item.ItemStack;

public enum ItemMatchingSystem {

    INSTANCE;

    private final List<ItemFilterAdapter> adapters = new CopyOnWriteArrayList();

    public void registerFilterAdapter(ItemFilterAdapter adapter) {
        this.adapters.add(adapter);
    }

    public boolean isItemFilter(ItemStack stack) {
        return this.getFilterAdapter(stack).isPresent();
    }

    public Optional<ItemFilterAdapter> getFilterAdapter(ItemStack stack) {
        return this.adapters.stream().filter(adapter -> adapter.isFilterStack(stack)).findFirst();
    }

    public boolean doesItemMatch(ItemStack filterStack, ItemStack toCheck, boolean matchNBT, boolean fuzzyNBT) {
        return (Boolean) this.getFilterAdapter(filterStack).map(adapter -> adapter.doesItemMatch(filterStack, toCheck)).orElse(this.areItemStacksEqual(filterStack, toCheck, matchNBT, fuzzyNBT));
    }

    public List<ItemStack> getAllMatchingStacks(ItemStack filterStack) {
        List<ItemStack> res = new ArrayList();
        this.adapters.forEach(adapter -> {
            if (adapter.isFilterStack(filterStack)) {
                res.addAll(DisplayStacksCache.getCachedDisplayStacks(filterStack, adapter));
            }
        });
        return res.isEmpty() ? List.of(filterStack) : res;
    }

    private boolean areItemStacksEqual(ItemStack stackA, ItemStack stackB, boolean matchNBT, boolean fuzzyNBT) {
        if (stackA == stackB) {
            return true;
        } else if (stackA.getItem() != stackB.getItem()) {
            return false;
        } else {
            return !stackA.hasTag() && !stackB.hasTag() ? true : !matchNBT || NBTUtils.compareNbt(stackA.getTag(), stackB.getTag(), fuzzyNBT, true);
        }
    }

    public Collection<ItemFilterAdapter> adapters() {
        return Collections.unmodifiableCollection(this.adapters);
    }
}