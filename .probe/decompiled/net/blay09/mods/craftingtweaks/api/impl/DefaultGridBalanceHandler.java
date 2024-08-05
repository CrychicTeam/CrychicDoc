package net.blay09.mods.craftingtweaks.api.impl;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultiset;
import com.google.common.collect.Multiset;
import java.util.List;
import java.util.Objects;
import net.blay09.mods.craftingtweaks.api.CraftingGrid;
import net.blay09.mods.craftingtweaks.api.GridBalanceHandler;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;

public class DefaultGridBalanceHandler implements GridBalanceHandler<AbstractContainerMenu> {

    @Override
    public void balanceGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu) {
        Container craftMatrix = grid.getCraftingMatrix(player, menu);
        if (craftMatrix != null) {
            ArrayListMultimap<String, ItemStack> itemMap = ArrayListMultimap.create();
            Multiset<String> itemCount = HashMultiset.create();
            int start = grid.getGridStartSlot(player, menu);
            int size = grid.getGridSize(player, menu);
            for (int i = start; i < start + size; i++) {
                int slotIndex = menu.slots.get(i).getContainerSlot();
                ItemStack itemStack = craftMatrix.getItem(slotIndex);
                if (!itemStack.isEmpty() && itemStack.getMaxStackSize() > 1) {
                    ResourceLocation registryName = BuiltInRegistries.ITEM.getKey(itemStack.getItem());
                    String key = Objects.toString(registryName);
                    if (itemStack.getTag() != null) {
                        key = key + "@" + itemStack.getTag();
                    }
                    itemMap.put(key, itemStack);
                    itemCount.add(key, itemStack.getCount());
                }
            }
            for (String key : itemMap.keySet()) {
                List<ItemStack> balanceList = itemMap.get(key);
                int totalCount = itemCount.count(key);
                int countPerStack = totalCount / balanceList.size();
                int restCount = totalCount % balanceList.size();
                for (ItemStack itemStack : balanceList) {
                    itemStack.setCount(countPerStack);
                }
                int idx = 0;
                while (restCount > 0) {
                    ItemStack itemStack = (ItemStack) balanceList.get(idx);
                    if (itemStack.getCount() < itemStack.getMaxStackSize()) {
                        itemStack.grow(1);
                        restCount--;
                    }
                    if (++idx >= balanceList.size()) {
                        idx = 0;
                    }
                }
            }
            menu.broadcastChanges();
        }
    }

    @Override
    public void spreadGrid(CraftingGrid grid, Player player, AbstractContainerMenu menu) {
        Container craftMatrix = grid.getCraftingMatrix(player, menu);
        if (craftMatrix != null) {
            boolean emptyBiggestSlot;
            do {
                ItemStack biggestSlotStack = null;
                int biggestSlotSize = 1;
                int start = grid.getGridStartSlot(player, menu);
                int size = grid.getGridSize(player, menu);
                for (int i = start; i < start + size; i++) {
                    int slotIndex = menu.slots.get(i).getContainerSlot();
                    ItemStack itemStack = craftMatrix.getItem(slotIndex);
                    if (!itemStack.isEmpty() && itemStack.getCount() > biggestSlotSize) {
                        biggestSlotStack = itemStack;
                        biggestSlotSize = itemStack.getCount();
                    }
                }
                if (biggestSlotStack == null) {
                    return;
                }
                emptyBiggestSlot = false;
                for (int ix = start; ix < start + size; ix++) {
                    int slotIndex = menu.slots.get(ix).getContainerSlot();
                    ItemStack itemStack = craftMatrix.getItem(slotIndex);
                    if (itemStack.isEmpty()) {
                        if (biggestSlotStack.getCount() > 1) {
                            craftMatrix.setItem(slotIndex, biggestSlotStack.split(1));
                        } else {
                            emptyBiggestSlot = true;
                        }
                    }
                }
            } while (emptyBiggestSlot);
            this.balanceGrid(grid, player, menu);
        }
    }
}