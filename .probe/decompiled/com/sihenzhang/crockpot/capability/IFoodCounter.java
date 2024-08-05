package com.sihenzhang.crockpot.capability;

import java.util.Map;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.util.INBTSerializable;

public interface IFoodCounter extends INBTSerializable<CompoundTag> {

    boolean hasEaten(Item var1);

    void addFood(Item var1);

    int getCount(Item var1);

    void setCount(Item var1, int var2);

    void clear();

    Map<Item, Integer> asMap();
}