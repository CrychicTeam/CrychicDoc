package com.sihenzhang.crockpot.recipe;

import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.util.JsonUtils;
import com.sihenzhang.crockpot.util.MathUtils;
import java.util.Arrays;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.Util;
import net.minecraft.core.NonNullList;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class ExplosionCraftingRecipe extends AbstractRecipe<ExplosionCraftingRecipe.Wrapper> {

    private static final RandomSource RANDOM = RandomSource.create();

    private final Ingredient ingredient;

    private final ItemStack result;

    private final float lossRate;

    private final boolean onlyBlock;

    public ExplosionCraftingRecipe(ResourceLocation id, Ingredient ingredient, ItemStack result, float lossRate, boolean onlyBlock) {
        super(id);
        Ingredient dummyInput = ingredient;
        boolean inputHasBlockItem = false;
        if (onlyBlock) {
            ItemStack[] items = ingredient.getItems();
            inputHasBlockItem = Arrays.stream(items).anyMatch(stack -> stack.getItem() instanceof BlockItem);
            if (inputHasBlockItem) {
                dummyInput = Ingredient.of(Arrays.stream(items).filter(stack -> stack.getItem() instanceof BlockItem));
            }
        }
        this.ingredient = dummyInput;
        this.result = result;
        this.lossRate = Mth.clamp(lossRate, 0.0F, 1.0F);
        this.onlyBlock = inputHasBlockItem;
    }

    public Ingredient getIngredient() {
        return this.ingredient;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public float getLossRate() {
        return this.lossRate;
    }

    public boolean isOnlyBlock() {
        return this.onlyBlock;
    }

    public boolean matches(ExplosionCraftingRecipe.Wrapper pContainer, Level pLevel) {
        return (pContainer.isFromBlock() || !this.onlyBlock) && this.ingredient.test(pContainer.m_8020_(0));
    }

    public ItemStack assemble(ExplosionCraftingRecipe.Wrapper pContainer, RegistryAccess pRegistryAccess) {
        if (MathUtils.fuzzyIsZero(this.lossRate)) {
            return this.result.copy();
        } else if (this.result.getCount() == 1) {
            return RANDOM.nextFloat() >= this.lossRate ? this.result.copy() : ItemStack.EMPTY;
        } else {
            int count = (int) IntStream.range(0, this.result.getCount()).filter(i -> RANDOM.nextFloat() >= this.lossRate).count();
            if (count == 0) {
                return ItemStack.EMPTY;
            } else {
                ItemStack output = this.result.copy();
                output.setCount(count);
                return output;
            }
        }
    }

    @Override
    public NonNullList<Ingredient> getIngredients() {
        return Util.make(NonNullList.create(), list -> list.add(this.ingredient));
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<ExplosionCraftingRecipe> {

        public ExplosionCraftingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            Ingredient ingredient = JsonUtils.getAsIngredient(serializedRecipe, "ingredient");
            ItemStack result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            float lossRate = Mth.clamp(GsonHelper.getAsFloat(serializedRecipe, "lossrate", 0.0F), 0.0F, 1.0F);
            boolean onlyBlock = GsonHelper.getAsBoolean(serializedRecipe, "onlyblock", false);
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        @Nullable
        public ExplosionCraftingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            Ingredient ingredient = Ingredient.fromNetwork(buffer);
            ItemStack result = buffer.readItem();
            float lossRate = buffer.readFloat();
            boolean onlyBlock = buffer.readBoolean();
            return new ExplosionCraftingRecipe(recipeId, ingredient, result, lossRate, onlyBlock);
        }

        public void toNetwork(FriendlyByteBuf buffer, ExplosionCraftingRecipe recipe) {
            recipe.getIngredient().toNetwork(buffer);
            buffer.writeItem(recipe.getResult());
            buffer.writeFloat(recipe.getLossRate());
            buffer.writeBoolean(recipe.isOnlyBlock());
        }
    }

    public static class Wrapper extends SimpleContainer {

        private final boolean fromBlock;

        public Wrapper(ItemStack item, boolean fromBlock) {
            super(item);
            this.fromBlock = fromBlock;
        }

        public Wrapper(ItemStack item) {
            this(item, false);
        }

        public boolean isFromBlock() {
            return this.fromBlock;
        }
    }
}