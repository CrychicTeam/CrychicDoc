package net.minecraft.world;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.world.item.ItemStack;

public class ContainerHelper {

    public static ItemStack removeItem(List<ItemStack> listItemStack0, int int1, int int2) {
        return int1 >= 0 && int1 < listItemStack0.size() && !((ItemStack) listItemStack0.get(int1)).isEmpty() && int2 > 0 ? ((ItemStack) listItemStack0.get(int1)).split(int2) : ItemStack.EMPTY;
    }

    public static ItemStack takeItem(List<ItemStack> listItemStack0, int int1) {
        return int1 >= 0 && int1 < listItemStack0.size() ? (ItemStack) listItemStack0.set(int1, ItemStack.EMPTY) : ItemStack.EMPTY;
    }

    public static CompoundTag saveAllItems(CompoundTag compoundTag0, NonNullList<ItemStack> nonNullListItemStack1) {
        return saveAllItems(compoundTag0, nonNullListItemStack1, true);
    }

    public static CompoundTag saveAllItems(CompoundTag compoundTag0, NonNullList<ItemStack> nonNullListItemStack1, boolean boolean2) {
        ListTag $$3 = new ListTag();
        for (int $$4 = 0; $$4 < nonNullListItemStack1.size(); $$4++) {
            ItemStack $$5 = nonNullListItemStack1.get($$4);
            if (!$$5.isEmpty()) {
                CompoundTag $$6 = new CompoundTag();
                $$6.putByte("Slot", (byte) $$4);
                $$5.save($$6);
                $$3.add($$6);
            }
        }
        if (!$$3.isEmpty() || boolean2) {
            compoundTag0.put("Items", $$3);
        }
        return compoundTag0;
    }

    public static void loadAllItems(CompoundTag compoundTag0, NonNullList<ItemStack> nonNullListItemStack1) {
        ListTag $$2 = compoundTag0.getList("Items", 10);
        for (int $$3 = 0; $$3 < $$2.size(); $$3++) {
            CompoundTag $$4 = $$2.getCompound($$3);
            int $$5 = $$4.getByte("Slot") & 255;
            if ($$5 >= 0 && $$5 < nonNullListItemStack1.size()) {
                nonNullListItemStack1.set($$5, ItemStack.of($$4));
            }
        }
    }

    public static int clearOrCountMatchingItems(Container container0, Predicate<ItemStack> predicateItemStack1, int int2, boolean boolean3) {
        int $$4 = 0;
        for (int $$5 = 0; $$5 < container0.getContainerSize(); $$5++) {
            ItemStack $$6 = container0.getItem($$5);
            int $$7 = clearOrCountMatchingItems($$6, predicateItemStack1, int2 - $$4, boolean3);
            if ($$7 > 0 && !boolean3 && $$6.isEmpty()) {
                container0.setItem($$5, ItemStack.EMPTY);
            }
            $$4 += $$7;
        }
        return $$4;
    }

    public static int clearOrCountMatchingItems(ItemStack itemStack0, Predicate<ItemStack> predicateItemStack1, int int2, boolean boolean3) {
        if (itemStack0.isEmpty() || !predicateItemStack1.test(itemStack0)) {
            return 0;
        } else if (boolean3) {
            return itemStack0.getCount();
        } else {
            int $$4 = int2 < 0 ? itemStack0.getCount() : Math.min(int2, itemStack0.getCount());
            itemStack0.shrink($$4);
            return $$4;
        }
    }
}