package com.sihenzhang.crockpot.recipe;

import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;

public class CrockPotRecipeType<T extends Recipe<?>> implements RecipeType<T> {

    private final String name;

    public CrockPotRecipeType(String name) {
        this.name = name;
    }

    public String toString() {
        return "crockpot:" + this.name;
    }
}