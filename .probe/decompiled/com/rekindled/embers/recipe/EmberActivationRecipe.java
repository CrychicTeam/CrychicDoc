package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

public class EmberActivationRecipe implements IEmberActivationRecipe {

    public static final EmberActivationRecipe.Serializer SERIALIZER = new EmberActivationRecipe.Serializer();

    public final ResourceLocation id;

    public final Ingredient ingredient;

    public final int ember;

    public EmberActivationRecipe(ResourceLocation id, Ingredient ingredient, int ember) {
        this.id = id;
        this.ingredient = ingredient;
        this.ember = ember;
    }

    @Override
    public boolean matches(Container context, Level pLevel) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (this.ingredient.test(context.getItem(i))) {
                return true;
            }
        }
        return false;
    }

    @Override
    public int getOutput(Container context) {
        return this.ember;
    }

    @Override
    public int process(Container context) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (this.ingredient.test(context.getItem(i))) {
                context.removeItem(i, 1);
                break;
            }
        }
        return this.ember;
    }

    @Override
    public ResourceLocation getId() {
        return this.id;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return SERIALIZER;
    }

    @Override
    public Ingredient getDisplayInput() {
        return this.ingredient;
    }

    @Override
    public int getDisplayOutput() {
        return this.ember;
    }

    public static class Serializer implements RecipeSerializer<EmberActivationRecipe> {

        public EmberActivationRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            int ember = GsonHelper.getAsInt(json, "ember");
            return new EmberActivationRecipe(recipeId, ingredient, ember);
        }

        @Nullable
        public EmberActivationRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            int ember = buffer.readVarInt();
            return new EmberActivationRecipe(recipeId, ingredient, ember);
        }

        public void toNetwork(FriendlyByteBuf buffer, EmberActivationRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            buffer.writeVarInt(recipe.ember);
        }
    }
}