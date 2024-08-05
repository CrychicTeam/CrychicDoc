package net.minecraftforge.common.brewing;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import org.jetbrains.annotations.NotNull;

public class BrewingRecipe implements IBrewingRecipe {

    @NotNull
    private final Ingredient input;

    @NotNull
    private final Ingredient ingredient;

    @NotNull
    private final ItemStack output;

    public BrewingRecipe(Ingredient input, Ingredient ingredient, ItemStack output) {
        this.input = input;
        this.ingredient = ingredient;
        this.output = output;
    }

    @Override
    public boolean isInput(@NotNull ItemStack stack) {
        return this.input.test(stack);
    }

    @Override
    public ItemStack getOutput(ItemStack input, ItemStack ingredient) {
        return this.isInput(input) && this.isIngredient(ingredient) ? this.getOutput().copy() : ItemStack.EMPTY;
    }

    public Ingredient getInput() {
        return this.input;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    @Override
    public boolean isIngredient(ItemStack ingredient) {
        return this.ingredient.test(ingredient);
    }
}