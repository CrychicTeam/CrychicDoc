package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrate;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuest;
import mezz.jei.api.recipe.RecipeType;

public class JEIRecipeTypes {

    public static RecipeType<WrappedQuest> QUEST = RecipeType.create("ftbquests", "quest", WrappedQuest.class);

    public static RecipeType<WrappedLootCrate> LOOT_CRATE = RecipeType.create("ftbquests", "loot_crate", WrappedLootCrate.class);
}