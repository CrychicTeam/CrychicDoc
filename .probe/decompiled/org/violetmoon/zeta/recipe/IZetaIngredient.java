package org.violetmoon.zeta.recipe;

import net.minecraft.world.item.crafting.Ingredient;

public interface IZetaIngredient<T extends Ingredient> {

    IZetaIngredientSerializer<T> zetaGetSerializer();
}