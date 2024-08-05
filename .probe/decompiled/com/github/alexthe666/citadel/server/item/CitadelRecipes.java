package com.github.alexthe666.citadel.server.item;

import java.util.ArrayList;
import java.util.List;
import net.minecraft.world.item.crafting.SmithingRecipe;

public class CitadelRecipes {

    private static List<SmithingRecipe> smithingRecipes = new ArrayList();

    public static void registerSmithingRecipe(SmithingRecipe recipe) {
        smithingRecipes.add(recipe);
    }

    public static List<SmithingRecipe> getSmithingRecipes() {
        return smithingRecipes;
    }
}