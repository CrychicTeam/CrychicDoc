package com.mna.api.recipes;

import com.mna.api.faction.IFaction;
import net.minecraft.resources.ResourceLocation;

public interface IManaweavingRecipe extends IItemAndPatternRecipe {

    boolean isEnchantment();

    ResourceLocation getEnchantment();

    int getEnchantmentMagnitude();

    boolean getCopyNBT();

    IFaction getFactionRequirement();

    @Override
    int getTier();
}