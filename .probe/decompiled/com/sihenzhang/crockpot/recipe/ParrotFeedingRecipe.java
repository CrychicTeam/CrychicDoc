package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.util.JsonUtils;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ParrotFeedingRecipe extends AbstractRecipe<Container> {

    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;

    private final RangedItem result;

    public ParrotFeedingRecipe(ResourceLocation id, Ingredient ingredient, RangedItem result) {
        super(id);
        this.ingredient = ingredient;
        this.result = result;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess registryAccess) {
        return this.result.getInstance(RANDOM);
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public RangedItem getResult() {
        return this.result;
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(this.ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return new ItemStack(this.result.item, this.result.max);
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.BIRDCAGE.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ParrotFeedingRecipe> {

        public ParrotFeedingRecipe fromJson(ResourceLocation pRecipeId, JsonObject pSerializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(pSerializedRecipe, "ingredient");
            RangedItem result = RangedItem.fromJson(GsonHelper.getAsJsonObject(pSerializedRecipe, "result"));
            return new ParrotFeedingRecipe(pRecipeId, ingredient, result);
        }

        @Nullable
        public ParrotFeedingRecipe fromNetwork(ResourceLocation pRecipeId, FriendlyByteBuf pBuffer) {
            Ingredient ingredient = Ingredient.fromNetwork(pBuffer);
            RangedItem result = RangedItem.fromNetwork(pBuffer);
            return new ParrotFeedingRecipe(pRecipeId, ingredient, result);
        }

        public void toNetwork(FriendlyByteBuf pBuffer, ParrotFeedingRecipe pRecipe) {
            pRecipe.ingredient.toNetwork(pBuffer);
            pRecipe.result.toNetwork(pBuffer);
        }
    }
}