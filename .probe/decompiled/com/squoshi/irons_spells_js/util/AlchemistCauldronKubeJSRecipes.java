package com.squoshi.irons_spells_js.util;

import dev.latvian.mods.kubejs.typings.Info;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipe;
import io.redspace.ironsspellbooks.block.alchemist_cauldron.AlchemistCauldronRecipeRegistry;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.Potion;

public class AlchemistCauldronKubeJSRecipes {

    private static AlchemistCauldronRecipe addAlchemistCauldronRecipe(ItemStack input, ItemStack ingredient, ItemStack result) {
        AlchemistCauldronRecipe recipe = new AlchemistCauldronRecipe(input, ingredient, result);
        AlchemistCauldronRecipeRegistry.addRecipe(recipe);
        return recipe;
    }

    private static AlchemistCauldronRecipe addAlchemistCauldronRecipe(Potion input, ItemStack ingredient, ItemStack result) {
        AlchemistCauldronRecipe recipe = new AlchemistCauldronRecipe(input, ingredient.getItem(), result.getItem());
        AlchemistCauldronRecipeRegistry.addRecipe(recipe);
        return recipe;
    }

    @Info("    Creates a new Alchemist Cauldron recipe. Used in StartupEvents.postInit\n")
    public static class AlchemistCauldronRecipeBuilder {

        private ItemStack input;

        private ItemStack ingredient;

        private ItemStack result;

        private Potion potionInput;

        private int baseRequirement = 1;

        private int resultLimit = 4;

        public static AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder create() {
            return new AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder();
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setInput(ItemStack input) {
            this.input = input;
            return this;
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setIngredient(ItemStack ingredient) {
            this.ingredient = ingredient;
            return this;
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setResult(ItemStack result) {
            this.result = result;
            return this;
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setPotionInput(Potion potionInput) {
            this.potionInput = potionInput;
            return this;
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setBaseRequirement(int i) {
            this.baseRequirement = i;
            return this;
        }

        public AlchemistCauldronKubeJSRecipes.AlchemistCauldronRecipeBuilder setResultLimit(int i) {
            this.resultLimit = i;
            return this;
        }

        public AlchemistCauldronRecipe register() {
            if (this.input != null && this.ingredient != null && this.result != null) {
                return AlchemistCauldronKubeJSRecipes.addAlchemistCauldronRecipe(this.input, this.ingredient, this.result).setBaseRequirement(this.baseRequirement).setResultLimit(this.resultLimit);
            } else if (this.potionInput != null && this.ingredient != null && this.result != null) {
                return AlchemistCauldronKubeJSRecipes.addAlchemistCauldronRecipe(this.potionInput, this.ingredient, this.result).setBaseRequirement(this.baseRequirement).setResultLimit(this.resultLimit);
            } else {
                throw new IllegalArgumentException("Invalid recipe parameters");
            }
        }
    }
}