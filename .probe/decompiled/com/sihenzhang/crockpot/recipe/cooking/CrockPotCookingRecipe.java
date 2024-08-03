package com.sihenzhang.crockpot.recipe.cooking;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;
import com.google.gson.JsonObject;
import com.sihenzhang.crockpot.base.FoodValues;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.AbstractRecipe;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.cooking.requirement.IRequirement;
import com.sihenzhang.crockpot.util.JsonUtils;
import java.util.List;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.stream.IntStream;
import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.GsonHelper;
import net.minecraft.util.RandomSource;
import net.minecraft.util.random.SimpleWeightedRandomList;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;

public class CrockPotCookingRecipe extends AbstractRecipe<CrockPotCookingRecipe.Wrapper> {

    private static final RandomSource RANDOM = RandomSource.create();

    private final List<IRequirement> requirements;

    private final ItemStack result;

    private final int priority;

    private final int weight;

    private final int cookingTime;

    private final int potLevel;

    public CrockPotCookingRecipe(ResourceLocation id, List<IRequirement> requirements, ItemStack result, int priority, int weight, int cookingTime, int potLevel) {
        super(id);
        this.requirements = ImmutableList.copyOf(requirements);
        this.result = result;
        this.priority = priority;
        this.weight = Math.max(weight, 1);
        this.cookingTime = cookingTime;
        this.potLevel = potLevel;
    }

    public boolean matches(CrockPotCookingRecipe.Wrapper pContainer, Level pLevel) {
        return pContainer.getPotLevel() >= this.potLevel && this.requirements.stream().allMatch(r -> r.test(pContainer));
    }

    public ItemStack assemble(CrockPotCookingRecipe.Wrapper pContainer, RegistryAccess pRegistryAccess) {
        return this.result.copy();
    }

    public static Optional<CrockPotCookingRecipe> getRecipeFor(CrockPotCookingRecipe.Wrapper container, Level level) {
        List<CrockPotCookingRecipe> recipes = level.getRecipeManager().getRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get(), container, level);
        OptionalInt optionalMaxPriority = recipes.stream().mapToInt(CrockPotCookingRecipe::getPriority).max();
        if (optionalMaxPriority.isPresent()) {
            int maxPriority = optionalMaxPriority.getAsInt();
            SimpleWeightedRandomList.Builder<CrockPotCookingRecipe> matchedRecipes = SimpleWeightedRandomList.builder();
            recipes.stream().filter(r -> r.getPriority() == maxPriority).forEach(r -> matchedRecipes.add(r, r.getWeight()));
            return matchedRecipes.build().getRandomValue(RANDOM);
        } else {
            return Optional.empty();
        }
    }

    public List<IRequirement> getRequirements() {
        return this.requirements;
    }

    public ItemStack getResult() {
        return this.result;
    }

    public int getPriority() {
        return this.priority;
    }

    public int getWeight() {
        return this.weight;
    }

    public int getCookingTime() {
        return this.cookingTime;
    }

    public int getPotLevel() {
        return this.potLevel;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess registryAccess) {
        return this.result;
    }

    @Override
    public ItemStack getToastSymbol() {
        return CrockPotItems.CROCK_POT.get().getDefaultInstance();
    }

    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_SERIALIZER.get();
    }

    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get();
    }

    public static class Serializer implements RecipeSerializer<CrockPotCookingRecipe> {

        public CrockPotCookingRecipe fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            List<IRequirement> requirements = Streams.stream(GsonHelper.getAsJsonArray(serializedRecipe, "requirements")).map(IRequirement::fromJson).toList();
            ItemStack result = JsonUtils.getAsItemStack(serializedRecipe, "result");
            int priority = GsonHelper.getAsInt(serializedRecipe, "priority");
            int weight = GsonHelper.getAsInt(serializedRecipe, "weight", 1);
            int cookingTime = GsonHelper.getAsInt(serializedRecipe, "cookingtime");
            int potLevel = GsonHelper.getAsInt(serializedRecipe, "potlevel");
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        @Nullable
        public CrockPotCookingRecipe fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            int length = buffer.readVarInt();
            List<IRequirement> requirements = IntStream.range(0, length).mapToObj(i -> IRequirement.fromNetwork(buffer)).toList();
            ItemStack result = buffer.readItem();
            int priority = buffer.readVarInt();
            int weight = buffer.readVarInt();
            int cookingTime = buffer.readVarInt();
            byte potLevel = buffer.readByte();
            return new CrockPotCookingRecipe(recipeId, requirements, result, priority, weight, cookingTime, potLevel);
        }

        public void toNetwork(FriendlyByteBuf buffer, CrockPotCookingRecipe recipe) {
            buffer.writeVarInt(recipe.getRequirements().size());
            recipe.getRequirements().forEach(requirement -> requirement.toNetwork(buffer));
            buffer.writeItem(recipe.getResult());
            buffer.writeVarInt(recipe.getPriority());
            buffer.writeVarInt(recipe.getWeight());
            buffer.writeVarInt(recipe.getCookingTime());
            buffer.writeByte(recipe.getPotLevel());
        }
    }

    public static class Wrapper extends SimpleContainer {

        private final FoodValues foodValues;

        private final int potLevel;

        public Wrapper(List<ItemStack> items, FoodValues foodValues, int potLevel) {
            super((ItemStack[]) items.toArray(new ItemStack[0]));
            this.foodValues = foodValues;
            this.potLevel = potLevel;
        }

        public FoodValues getFoodValues() {
            return this.foodValues;
        }

        public int getPotLevel() {
            return this.potLevel;
        }
    }
}