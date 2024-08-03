package com.mna.api.recipes;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public interface IMARecipe {

    ResourceLocation getRegistryId();

    ItemStack getResultItem();

    ItemStack getGuiRepresentationStack();

    int getTier();
}