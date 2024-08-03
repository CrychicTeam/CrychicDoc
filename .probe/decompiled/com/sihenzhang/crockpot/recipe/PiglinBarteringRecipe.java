package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.util.random.WeightedEntry;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class PiglinBarteringRecipe extends AbstractRecipe<Container> {

    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;

    private final SimpleWeightedRandomList<RangedItem> weightedResults;

    public PiglinBarteringRecipe(ResourceLocation id, Ingredient ingredient, SimpleWeightedRandomList<RangedItem> weightedResults) {
        super(id);
        this.ingredient = ingredient;
        this.weightedResults = weightedResults;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public SimpleWeightedRandomList<RangedItem> getWeightedResults() {
        return this.weightedResults;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        return this.ingredient.test(pContainer.getItem(0));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess registryAccess) {
        return (ItemStack) this.weightedResults.getRandomValue(RANDOM).map(rangedItem -> rangedItem.isRanged() ? new ItemStack(rangedItem.item, Mth.nextInt(RANDOM, rangedItem.min, rangedItem.max)) : new ItemStack(rangedItem.item, rangedItem.min)).orElse(ItemStack.EMPTY);
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(this.ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess0) {
        return ItemStack.EMPTY;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.PIGLIN_BARTERING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<PiglinBarteringRecipe> {

        public PiglinBarteringRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            SimpleWeightedRandomList.Builder<RangedItem> builder = SimpleWeightedRandomList.builder();
            JsonArray results = GsonHelper.getAsJsonArray(serializedRecipe, "results");
            results.forEach(result -> {
                RangedItem rangedItem = RangedItem.fromJson(result);
                if (rangedItem != null) {
                    int weight = GsonHelper.getAsInt(GsonHelper.convertToJsonObject(result, "weighted ranged item"), "weight", 1);
                    builder.add(rangedItem, weight);
                }
            });
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        @Nullable
        public PiglinBarteringRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            SimpleWeightedRandomList.Builder<RangedItem> builder = SimpleWeightedRandomList.builder();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                RangedItem rangedItem = RangedItem.fromNetwork(buffer);
                int weight = buffer.readVarInt();
                builder.add(rangedItem, weight);
            }
            return new PiglinBarteringRecipe(recipeId, ingredient, builder.build());
        }

        public void toNetwork(FriendlyByteBuf buffer, PiglinBarteringRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            List<WeightedEntry.Wrapper<RangedItem>> weightedRangedItems = recipe.getWeightedResults().m_146338_();
            buffer.writeVarInt(weightedRangedItems.size());
            weightedRangedItems.forEach(weightedRangedItem -> {
                ((RangedItem) weightedRangedItem.getData()).toNetwork(buffer);
                buffer.writeVarInt(weightedRangedItem.getWeight().asInt());
            });
        }
    }
}