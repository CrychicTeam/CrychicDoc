package com.mna.api.recipes;

import net.minecraft.resources.ResourceLocation;

public interface IItemAndPatternRecipe extends IMARecipe {

    ResourceLocation[] getRequiredItems();

    ResourceLocation[] getRequiredPatterns();
}