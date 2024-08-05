package dev.latvian.mods.kubejs.recipe;

import dev.latvian.mods.kubejs.item.ItemStackSet;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

public class IngredientMatch implements ItemMatch {

    public final Ingredient ingredient;

    public final boolean exact;

    private ItemStackSet allItems;

    private ItemStack[] allItemsArray;

    public IngredientMatch(Ingredient ingredient, boolean exact) {
        this.ingredient = ingredient;
        this.exact = exact;
    }

    public ItemStackSet getAllItems() {
        if (this.allItems == null) {
            this.allItems = this.ingredient.kjs$getStacks();
            this.allItemsArray = this.allItems.toArray();
        }
        return this.allItems;
    }

    public ItemStack[] getAllItemArray() {
        if (this.allItemsArray == null) {
            this.allItems = this.ingredient.kjs$getStacks();
            this.allItemsArray = this.allItems.toArray();
        }
        return this.allItemsArray;
    }

    @Override
    public boolean contains(ItemStack item) {
        return !item.isEmpty() && this.getAllItems().contains(item);
    }

    @Override
    public boolean contains(Ingredient in) {
        if (in == Ingredient.EMPTY) {
            return false;
        } else {
            try {
                for (ItemStack stack : this.getAllItemArray()) {
                    if (in.test(stack)) {
                        return true;
                    }
                }
                return false;
            } catch (Exception var6) {
                throw new RecipeExceptionJS("Failed to test ingredient " + in, var6);
            }
        }
    }

    public String toString() {
        return this.ingredient.toString();
    }
}