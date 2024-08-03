package com.rekindled.embers.api.filter;

import net.minecraft.world.item.ItemStack;

public abstract class ComparatorMatch implements IFilterComparator {

    private String name;

    private int priority;

    public ComparatorMatch(String name, int priority) {
        this.name = name;
        this.priority = priority;
    }

    @Override
    public int getPriority() {
        return this.priority;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public Integer getCompare(ItemStack stack) {
        return 0;
    }

    @Override
    public boolean isBetween(ItemStack stack1, ItemStack stack2, ItemStack testStack, EnumFilterSetting setting) {
        return this.match(stack1, testStack);
    }
}