package com.mna.recipes.rituals;

import com.mna.recipes.RecipeInit;
import java.util.Collection;
import java.util.Optional;
import java.util.stream.Collectors;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.level.Level;

public class RitualRecipeHelper {

    public static RitualRecipe GetRitualRecipe(Level world, ResourceLocation rLoc) {
        if (world == null) {
            return null;
        } else {
            Optional<RitualRecipe> recipe = world.getRecipeManager().getRecipesFor(RecipeInit.RITUAL_TYPE.get(), null, world).stream().filter(r -> r.m_6423_().toString().equals(rLoc.toString())).findFirst();
            return recipe.isPresent() ? (RitualRecipe) recipe.get() : null;
        }
    }

    public static Collection<RitualRecipe> getAllRituals(Level world) {
        Collection<RitualRecipe> collection = world.getRecipeManager().<CraftingContainer, RitualRecipe>getRecipesFor(RecipeInit.RITUAL_TYPE.get(), null, world);
        return (Collection<RitualRecipe>) collection.stream().sorted((c1, c2) -> c2.getLowerBound() - c1.getLowerBound()).collect(Collectors.toList());
    }
}