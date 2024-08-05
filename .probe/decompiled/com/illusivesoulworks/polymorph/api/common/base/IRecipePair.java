package com.illusivesoulworks.polymorph.api.common.base;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IRecipePair extends Comparable<IRecipePair> {

    ItemStack getOutput();

    ResourceLocation getResourceLocation();
}