package dev.xkmc.l2complements.content.recipe;

import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class BurntRecipeBuilder extends BaseRecipeBuilder<BurntRecipeBuilder, BurntRecipe, BurntRecipe, BurntRecipe.Inv> {

    public BurntRecipeBuilder(Ingredient in, ItemStack out, int chance) {
        super((BaseRecipe.RecType<BurntRecipe, BurntRecipe, BurntRecipe.Inv>) LCRecipes.RS_BURNT.get());
        this.recipe.ingredient = in;
        this.recipe.result = out;
        this.recipe.chance = chance;
    }
}