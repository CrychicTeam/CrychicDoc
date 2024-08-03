package noppes.npcs;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class NoppesUtilPlayer {

    public static boolean compareItems(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
        return !NoppesUtilServer.IsItemStackNull(item) && !NoppesUtilServer.IsItemStackNull(item2) ? compareItemDetails(item, item2, ignoreDamage, ignoreNBT) : false;
    }

    private static boolean compareItemDetails(ItemStack item, ItemStack item2, boolean ignoreDamage, boolean ignoreNBT) {
        if (item.getItem() != item2.getItem()) {
            return false;
        } else if (!ignoreDamage && item.getDamageValue() != -1 && item.getDamageValue() != item2.getDamageValue()) {
            return false;
        } else {
            return ignoreNBT || item.getTag() == null || item2.getTag() != null && item.getTag().equals(item2.getTag()) ? ignoreNBT || item2.getTag() == null || item.getTag() != null : false;
        }
    }

    public static boolean compareItems(Player player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
        int size = 0;
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
            ItemStack is = player.getInventory().getItem(i);
            if (!NoppesUtilServer.IsItemStackNull(is) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
                size += is.getCount();
            }
        }
        return size >= item.getCount();
    }

    public static void consumeItem(Player player, ItemStack item, boolean ignoreDamage, boolean ignoreNBT) {
        if (!NoppesUtilServer.IsItemStackNull(item)) {
            int size = item.getCount();
            for (int i = 0; i < player.getInventory().getContainerSize(); i++) {
                ItemStack is = player.getInventory().getItem(i);
                if (!NoppesUtilServer.IsItemStackNull(is) && compareItems(item, is, ignoreDamage, ignoreNBT)) {
                    if (size < is.getCount()) {
                        is.split(size);
                        break;
                    }
                    size -= is.getCount();
                    player.getInventory().setItem(i, ItemStack.EMPTY);
                }
            }
        }
    }

    public static List<ItemStack> countStacks(Container inv, boolean ignoreDamage, boolean ignoreNBT) {
        List<ItemStack> list = new ArrayList();
        for (int i = 0; i < inv.getContainerSize(); i++) {
            ItemStack item = inv.getItem(i);
            if (!NoppesUtilServer.IsItemStackNull(item)) {
                boolean found = false;
                for (ItemStack is : list) {
                    if (compareItems(item, is, ignoreDamage, ignoreNBT)) {
                        is.setCount(is.getCount() + item.getCount());
                        found = true;
                        break;
                    }
                }
                if (!found) {
                    list.add(item.copy());
                }
            }
        }
        return list;
    }
}