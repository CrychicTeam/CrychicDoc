package net.blay09.mods.balm.api.container;

import net.blay09.mods.balm.api.Balm;
import net.minecraft.core.BlockPos;
import net.minecraft.world.Container;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class ContainerUtils {

    public static ItemStack extractItem(Container container, int slot, int amount, boolean simulate) {
        if (container instanceof ExtractionAwareContainer extractionAwareContainer && !extractionAwareContainer.canExtractItem(slot)) {
            return ItemStack.EMPTY;
        }
        if (amount == 0) {
            return ItemStack.EMPTY;
        } else if (slot >= 0 && slot < container.getContainerSize()) {
            ItemStack existing = container.getItem(slot);
            if (existing.isEmpty()) {
                return ItemStack.EMPTY;
            } else {
                int toExtract = Math.min(amount, existing.getMaxStackSize());
                if (existing.getCount() <= toExtract) {
                    if (!simulate) {
                        container.setItem(slot, ItemStack.EMPTY);
                        return existing;
                    } else {
                        return existing.copy();
                    }
                } else {
                    if (!simulate) {
                        container.setItem(slot, copyStackWithSize(existing, existing.getCount() - toExtract));
                    }
                    return copyStackWithSize(existing, toExtract);
                }
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack insertItem(Container container, ItemStack itemStack, boolean simulate) {
        if (container != null && !itemStack.isEmpty()) {
            for (int i = 0; i < container.getContainerSize(); i++) {
                itemStack = insertItem(container, i, itemStack, simulate);
                if (itemStack.isEmpty()) {
                    return ItemStack.EMPTY;
                }
            }
            return itemStack;
        } else {
            return itemStack;
        }
    }

    public static ItemStack insertItem(Container container, int slot, ItemStack itemStack, boolean simulate) {
        if (container == null || itemStack.isEmpty()) {
            return itemStack;
        } else if (slot >= 0 && slot < container.getContainerSize()) {
            ItemStack existing = container.getItem(slot);
            int limit = Math.min(container.getMaxStackSize(), itemStack.getMaxStackSize());
            if (!existing.isEmpty()) {
                if (!Balm.getHooks().canItemsStack(itemStack, existing)) {
                    return itemStack;
                }
                limit -= existing.getCount();
            }
            if (limit <= 0) {
                return itemStack;
            } else {
                boolean reachedLimit = itemStack.getCount() > limit;
                if (!simulate) {
                    if (existing.isEmpty()) {
                        container.setItem(slot, reachedLimit ? copyStackWithSize(itemStack, limit) : itemStack);
                    } else {
                        existing.grow(reachedLimit ? limit : itemStack.getCount());
                        container.setChanged();
                    }
                }
                return reachedLimit ? copyStackWithSize(itemStack, itemStack.getCount() - limit) : ItemStack.EMPTY;
            }
        } else {
            return ItemStack.EMPTY;
        }
    }

    public static ItemStack insertItemStacked(Container container, ItemStack itemStack, boolean simulate) {
        if (container != null && !itemStack.isEmpty()) {
            if (!itemStack.isStackable()) {
                return insertItem(container, itemStack, simulate);
            } else {
                int firstEmptySlot = -1;
                for (int i = 0; i < container.getContainerSize(); i++) {
                    ItemStack slotStack = container.getItem(i);
                    if (slotStack.isEmpty() && firstEmptySlot == -1) {
                        firstEmptySlot = i;
                    } else {
                        if (slotStack.isStackable() && ItemStack.isSameItemSameTags(slotStack, itemStack)) {
                            itemStack = insertItem(container, i, itemStack, simulate);
                        }
                        if (itemStack.isEmpty()) {
                            return ItemStack.EMPTY;
                        }
                    }
                }
                if (firstEmptySlot != -1) {
                    for (int ix = firstEmptySlot; ix < container.getContainerSize(); ix++) {
                        if (container.getItem(ix).isEmpty()) {
                            itemStack = insertItem(container, ix, itemStack, simulate);
                            if (itemStack.isEmpty()) {
                                return ItemStack.EMPTY;
                            }
                        }
                    }
                }
                return itemStack;
            }
        } else {
            return itemStack;
        }
    }

    public static void dropItems(Container container, Level level, BlockPos pos) {
        for (int i = 0; i < container.getContainerSize(); i++) {
            ItemStack itemStack = container.getItem(i);
            if (!itemStack.isEmpty()) {
                ItemEntity itemEntity = new ItemEntity(level, (double) ((float) pos.m_123341_() + 0.5F), (double) ((float) pos.m_123342_() + 0.5F), (double) ((float) pos.m_123343_() + 0.5F), itemStack);
                itemEntity.m_20334_(0.0, 0.2F, 0.0);
                level.m_7967_(itemEntity);
            }
        }
        container.m_6211_();
    }

    public static ItemStack copyStackWithSize(ItemStack itemStack, int size) {
        if (size == 0) {
            return ItemStack.EMPTY;
        } else {
            ItemStack copy = itemStack.copy();
            copy.setCount(size);
            return copy;
        }
    }
}