package org.violetmoon.quark.api;

import java.util.Comparator;
import net.minecraft.world.item.ItemStack;

public interface ICustomSorting {

    Comparator<ItemStack> getItemComparator();

    String getSortingCategory();
}