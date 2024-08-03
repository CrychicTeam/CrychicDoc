package dev.xkmc.l2backpack.content.click;

import dev.xkmc.l2backpack.content.bag.AbstractBag;
import dev.xkmc.l2backpack.content.drawer.BaseDrawerItem;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public interface DrawerQuickInsert {

    static boolean moveItemStackTo(Player pl, AbstractContainerMenu menu, ItemStack stack, int start, int end, boolean reverse) {
        return moveItemStackTo(pl, menu, stack, start, end, reverse, false);
    }

    static boolean moveItemStackTo(Player pl, AbstractContainerMenu menu, ItemStack stack, int start, int end, boolean reverse, boolean split) {
        boolean changed = false;
        changed |= doMerge(pl, menu, stack, start, end, reverse, false);
        changed |= doMerge(pl, menu, stack, start, end, reverse, true);
        if (!stack.isEmpty()) {
            changed |= doTake(pl, menu, stack, start, end, reverse, split);
        }
        return changed;
    }

    private static boolean doMerge(Player pl, AbstractContainerMenu menu, ItemStack stack, int start, int end, boolean reverse, boolean allowEmpty) {
        boolean changed = false;
        int i = start;
        if (reverse) {
            i = end - 1;
        }
        while (!stack.isEmpty() && (reverse ? i >= start : i < end)) {
            Slot slot = menu.slots.get(i);
            if (tryMerge(pl, stack, slot.getItem(), slot, allowEmpty)) {
                changed = true;
            }
            if (reverse) {
                i--;
            } else {
                i++;
            }
        }
        return changed;
    }

    private static boolean doTake(Player pl, AbstractContainerMenu menu, ItemStack stack, int start, int end, boolean reverse, boolean split) {
        boolean changed = false;
        int i;
        if (reverse) {
            i = end - 1;
        } else {
            i = start;
        }
        while (reverse ? i >= start : i < end) {
            Slot slot = menu.slots.get(i);
            if (tryTake(pl, stack, slot.getItem(), slot, split)) {
                changed = true;
                break;
            }
            if (reverse) {
                i--;
            } else {
                i++;
            }
        }
        return changed;
    }

    private static boolean tryMerge(Player pl, ItemStack src, ItemStack dst, Slot slot, boolean allowEmpty) {
        if (dst.isEmpty()) {
            return false;
        } else {
            if (dst.getItem() instanceof AbstractBag bag && pl instanceof ServerPlayer sp && bag.isValidContent(src)) {
                int count = src.getCount();
                bag.attemptInsert(dst, src, sp);
                return count != src.getCount();
            }
            if (pl instanceof ServerPlayer sp && src.getTag() == null && dst.getItem() instanceof BaseDrawerItem item) {
                if (!allowEmpty && item.canSetNewItem(dst)) {
                    return false;
                }
                int count = src.getCount();
                item.attemptInsert(dst, src, sp);
                return count != src.getCount();
            }
            if (src.isStackable() && ItemStack.isSameItemSameTags(src, dst)) {
                int j = dst.getCount() + src.getCount();
                int maxSize = Math.min(slot.getMaxStackSize(), src.getMaxStackSize());
                if (j <= maxSize) {
                    src.setCount(0);
                    dst.setCount(j);
                    slot.setChanged();
                    return true;
                }
                if (dst.getCount() < maxSize) {
                    src.shrink(maxSize - dst.getCount());
                    dst.setCount(maxSize);
                    slot.setChanged();
                    return true;
                }
            }
            return false;
        }
    }

    private static boolean tryTake(Player pl, ItemStack src, ItemStack dst, Slot slot, boolean split) {
        if (!dst.isEmpty()) {
            return false;
        } else {
            if (split && pl instanceof ServerPlayer sp && src.getItem() instanceof BaseDrawerItem item) {
                ItemStack content = item.takeItem(src, slot.getMaxStackSize(), sp, true);
                if (slot.mayPlace(content)) {
                    item.takeItem(src, slot.getMaxStackSize(), sp, false);
                    slot.setByPlayer(content);
                    slot.setChanged();
                    return true;
                }
                return false;
            }
            if (slot.mayPlace(src)) {
                if (src.getCount() > slot.getMaxStackSize()) {
                    slot.setByPlayer(src.split(slot.getMaxStackSize()));
                } else {
                    slot.setByPlayer(src.split(src.getCount()));
                }
                slot.setChanged();
                return true;
            } else {
                return false;
            }
        }
    }

    boolean quickMove(Player var1, AbstractContainerMenu var2, ItemStack var3, int var4);
}