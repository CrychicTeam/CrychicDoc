package com.rekindled.embers.recipe;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;

public class AnvilDisplayRecipe implements IDawnstoneAnvilRecipe {

    public final ResourceLocation id;

    public List<ItemStack> outputs;

    public List<ItemStack> inputs;

    public Ingredient ingredient;

    public AnvilDisplayRecipe(ResourceLocation id, List<ItemStack> outputs, List<ItemStack> inputs, Ingredient ingredient) {
        this.id = id;
        this.outputs = outputs;
        this.inputs = inputs;
        this.ingredient = ingredient;
    }

    @Override
    public boolean matches(Container context, Level pLevel) {
        return false;
    }

    @Override
    public List<ItemStack> getOutput(Container context) {
        return List.of();
    }

    @Override
    public List<ItemStack> getDisplayInputBottom() {
        return this.inputs;
    }

    @Override
    public List<ItemStack> getDisplayInputTop() {
        return List.of(this.ingredient.getItems());
    }

    @Override
    public List<ItemStack> getDisplayOutput() {
        return this.outputs;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return AnvilRepairRecipe.SERIALIZER;
    }
}