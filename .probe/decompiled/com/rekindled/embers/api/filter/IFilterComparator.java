package com.rekindled.embers.api.filter;

import net.minecraft.world.item.ItemStack;

public interface IFilterComparator {

    int getPriority();

    String getName();

    boolean match(ItemStack var1, ItemStack var2);

    Integer getCompare(ItemStack var1);

    default boolean isBetween(ItemStack stack1, ItemStack stack2, ItemStack testStack, EnumFilterSetting setting) {
        if (!this.match(stack1, testStack)) {
            return false;
        } else {
            Integer a = this.getCompare(stack1);
            Integer b = this.getCompare(stack2);
            Integer test = this.getCompare(testStack);
            if (setting == EnumFilterSetting.FUZZY && a.compareTo(b) == 0) {
                return true;
            } else {
                return a.compareTo(test) <= 0 && b.compareTo(test) >= 0 ? true : a.compareTo(test) >= 0 && b.compareTo(test) <= 0;
            }
        }
    }

    String format(ItemStack var1, ItemStack var2, EnumFilterSetting var3, boolean var4);
}