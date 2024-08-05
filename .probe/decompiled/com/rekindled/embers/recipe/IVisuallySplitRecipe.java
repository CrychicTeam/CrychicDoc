package com.rekindled.embers.recipe;

import java.util.List;
import net.minecraft.world.item.crafting.Recipe;

public interface IVisuallySplitRecipe<R extends Recipe<?>> {

    List<R> getVisualRecipes();
}