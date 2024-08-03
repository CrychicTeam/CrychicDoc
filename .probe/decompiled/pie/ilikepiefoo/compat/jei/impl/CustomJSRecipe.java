package pie.ilikepiefoo.compat.jei.impl;

import java.util.ArrayList;
import java.util.List;
import mezz.jei.api.recipe.RecipeType;
import org.jetbrains.annotations.NotNull;

public class CustomJSRecipe {

    @NotNull
    private final RecipeType<CustomJSRecipe> recipeType;

    private Object recipeData;

    public CustomJSRecipe(Object recipeData, @NotNull RecipeType<CustomJSRecipe> recipeType) {
        this.recipeData = recipeData;
        this.recipeType = recipeType;
    }

    public Object getRecipeData() {
        return this.recipeData;
    }

    public void setRecipeData(Object recipeData) {
        this.recipeData = recipeData;
    }

    public Object getData() {
        return this.recipeData;
    }

    @NotNull
    public RecipeType<CustomJSRecipe> getRecipeType() {
        return this.recipeType;
    }

    public static class CustomRecipeListBuilder {

        @NotNull
        private final RecipeType<CustomJSRecipe> recipeType;

        private final List<CustomJSRecipe> recipes;

        public CustomRecipeListBuilder(@NotNull RecipeType<CustomJSRecipe> recipeType) {
            this.recipeType = recipeType;
            this.recipes = new ArrayList();
        }

        public CustomJSRecipe custom(Object recipeData) {
            CustomJSRecipe recipe = new CustomJSRecipe(recipeData, this.recipeType);
            this.recipes.add(recipe);
            return recipe;
        }

        public CustomJSRecipe.CustomRecipeListBuilder add(Object recipeData) {
            this.recipes.add(new CustomJSRecipe(recipeData, this.recipeType));
            return this;
        }

        public CustomJSRecipe.CustomRecipeListBuilder add(CustomJSRecipe recipe) {
            this.recipes.add(recipe);
            return this;
        }

        public CustomJSRecipe.CustomRecipeListBuilder addAll(List<Object> recipeData) {
            this.recipes.addAll(recipeData.stream().map(data -> new CustomJSRecipe(data, this.recipeType)).toList());
            return this;
        }

        public List<CustomJSRecipe> getRecipes() {
            return this.recipes;
        }

        @NotNull
        public RecipeType<CustomJSRecipe> getRecipeType() {
            return this.recipeType;
        }
    }
}