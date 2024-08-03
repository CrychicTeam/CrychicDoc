package com.mna.api.capabilities;

import com.mna.recipes.manaweaving.ManaweavingPattern;
import java.util.List;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IPlayerCantrip {

    ResourceLocation getCantripID();

    ItemStack getStack();

    void readFromNBT(CompoundTag var1);

    CompoundTag writeToNBT();

    void setStack(ItemStack var1);

    ResourceLocation getPattern(int var1);

    void setPatterns(List<ResourceLocation> var1);

    boolean matches(List<ManaweavingPattern> var1);
}