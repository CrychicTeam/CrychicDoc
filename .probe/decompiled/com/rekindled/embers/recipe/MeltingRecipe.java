package com.rekindled.embers.recipe;

import com.google.gson.JsonObject;
import com.rekindled.embers.util.Misc;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import org.jetbrains.annotations.Nullable;

public class MeltingRecipe implements IMeltingRecipe {

    public static final MeltingRecipe.Serializer SERIALIZER = new MeltingRecipe.Serializer();

    public final ResourceLocation id;

    public final Ingredient ingredient;

    public final FluidStack output;

    public final FluidStack bonus;

    public MeltingRecipe(ResourceLocation id, Ingredient ingredient, FluidStack output, FluidStack bonus) {
        this.id = id;
        this.ingredient = ingredient;
        this.output = output;
        this.bonus = bonus;
    }

    public MeltingRecipe(ResourceLocation id, Ingredient ingredient, FluidStack output) {
        this(id, ingredient, output, FluidStack.EMPTY);
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
    public FluidStack getOutput(Container context) {
        return this.output;
    }

    @Override
    public FluidStack getBonus() {
        return this.bonus;
    }

    @Override
    public FluidStack process(Container context) {
        for (int i = 0; i < context.getContainerSize(); i++) {
            if (this.ingredient.test(context.getItem(i))) {
                context.removeItem(i, 1);
                break;
            }
        }
        return this.output;
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
    public FluidStack getDisplayOutput() {
        return this.output;
    }

    @Override
    public Ingredient getDisplayInput() {
        return this.ingredient;
    }

    public static class Serializer implements RecipeSerializer<MeltingRecipe> {

        public MeltingRecipe fromJson(ResourceLocation recipeId, JsonObject json) {
            Ingredient ingredient = Ingredient.fromJson(json.get("input"));
            FluidStack output = Misc.deserializeFluidStack(GsonHelper.getAsJsonObject(json, "output"));
            FluidStack bonus = FluidStack.EMPTY;
            if (json.has("bonus")) {
                bonus = Misc.deserializeFluidStack(GsonHelper.getAsJsonObject(json, "bonus"));
            }
            return new MeltingRecipe(recipeId, ingredient, output, bonus);
        }

        @Nullable
        public MeltingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            FluidStack output = FluidStack.readFromPacket(buffer);
            FluidStack bonus = FluidStack.readFromPacket(buffer);
            return new MeltingRecipe(recipeId, ingredient, output, bonus);
        }

        public void toNetwork(FriendlyByteBuf buffer, MeltingRecipe recipe) {
            recipe.ingredient.toNetwork(buffer);
            recipe.output.writeToPacket(buffer);
            recipe.bonus.writeToPacket(buffer);
        }
    }
}