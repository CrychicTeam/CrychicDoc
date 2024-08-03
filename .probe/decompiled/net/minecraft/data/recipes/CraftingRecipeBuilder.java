package net.minecraft.data.recipes;

import com.google.gson.JsonObject;
import net.minecraft.world.item.crafting.CraftingBookCategory;

public abstract class CraftingRecipeBuilder {

    protected static CraftingBookCategory determineBookCategory(RecipeCategory recipeCategory0) {
        return switch(recipeCategory0) {
            case BUILDING_BLOCKS ->
                CraftingBookCategory.BUILDING;
            case TOOLS, COMBAT ->
                CraftingBookCategory.EQUIPMENT;
            case REDSTONE ->
                CraftingBookCategory.REDSTONE;
            default ->
                CraftingBookCategory.MISC;
        };
    }

    protected abstract static class CraftingResult implements FinishedRecipe {

        private final CraftingBookCategory category;

        protected CraftingResult(CraftingBookCategory craftingBookCategory0) {
            this.category = craftingBookCategory0;
        }

        @Override
        public void serializeRecipeData(JsonObject jsonObject0) {
            jsonObject0.addProperty("category", this.category.getSerializedName());
        }
    }
}