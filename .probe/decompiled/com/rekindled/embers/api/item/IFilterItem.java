package com.rekindled.embers.api.item;

import com.rekindled.embers.api.filter.IFilter;
import net.minecraft.world.item.ItemStack;

public interface IFilterItem {

    IFilter getFilter(ItemStack var1);
}