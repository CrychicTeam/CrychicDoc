package com.sihenzhang.crockpot.recipe;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.ImmutableSortedSet;
import com.google.common.collect.ImmutableSortedSet.Builder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.base.FoodValues;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.core.RegistryAccess;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.util.GsonHelper;
import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;

public class FoodValuesDefinition extends AbstractRecipe<Container> {

    private final Set<ResourceLocation> names;

    private final FoodValues foodValues;

    private final boolean item;

    public FoodValuesDefinition(ResourceLocation id, Set<ResourceLocation> names, FoodValues foodValues, boolean item) {
        super(id);
        this.names = ImmutableSet.copyOf(names);
        this.foodValues = foodValues;
        this.item = item;
    }

    public Set<ResourceLocation> getNames() {
        return this.names;
    }

    public FoodValues getFoodValues() {
        return this.foodValues;
    }

    public boolean isItem() {
        return this.item;
    }

    @Override
    public boolean matches(Container pContainer, Level pLevel) {
        ItemStack stack = pContainer.getItem(0);
        return this.item ? this.names.stream().anyMatch(name -> name.equals(ForgeRegistries.ITEMS.getKey(stack.getItem()))) : this.names.stream().anyMatch(name -> stack.is(ItemTags.create(name)));
    }

    @Override
    public ItemStack assemble(Container pContainer, RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack getResultItem(RegistryAccess pRegistryAccess) {
        return ItemStack.EMPTY;
    }

    @Nonnull
    public static FoodValues getFoodValues(ItemStack stack, Level level) {
        List<FoodValuesDefinition> allDefs = level.getRecipeManager().getRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get(), new SimpleContainer(stack), level);
        return allDefs.isEmpty() ? FoodValues.create() : (FoodValues) allDefs.stream().filter(FoodValuesDefinition::isItem).findFirst().map(FoodValuesDefinition::getFoodValues).orElseGet(() -> {
            FoodValues foodValues = FoodValues.create();
            long maxCount = -1L;
            HashMap<ResourceLocation, FoodValues> tagDefs = new HashMap();
            allDefs.forEach(def -> def.getNames().forEach(name -> tagDefs.put(name, def.getFoodValues())));
            for (ResourceLocation tag : stack.getTags().map(TagKey::f_203868_).filter(tagDefs::containsKey).toList()) {
                long count = tag.getPath().chars().filter(c -> c == 47).count();
                if (count >= maxCount) {
                    if (count > maxCount) {
                        maxCount = count;
                        foodValues.clear();
                    }
                    ((FoodValues) tagDefs.get(tag)).entrySet().forEach(entry -> foodValues.put((FoodCategory) entry.getKey(), Math.max(foodValues.get((FoodCategory) entry.getKey()), (Float) entry.getValue())));
                }
            }
            return foodValues;
        });
    }

    public static Set<ItemStack> getMatchedItems(FoodCategory category, Level level) {
        Builder<ItemStack> builder = ImmutableSortedSet.orderedBy(Comparator.comparing(stack -> getFoodValues(stack, level).get(category)).thenComparing(stack -> ForgeRegistries.ITEMS.getKey(stack.getItem()), Comparator.comparing(key -> !"minecraft".equals(key.getNamespace())).thenComparing(key -> !"crockpot".equals(key.getNamespace())).thenComparing(Comparator.naturalOrder())));
        List<FoodValuesDefinition> allDefs = level.getRecipeManager().<Container, FoodValuesDefinition>getAllRecipesFor(CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get()).stream().filter(def -> def.getFoodValues().has(category)).toList();
        allDefs.stream().filter(FoodValuesDefinition::isItem).forEach(itemDef -> itemDef.getNames().forEach(name -> {
            Item item = ForgeRegistries.ITEMS.getValue(name);
            if (item != null && item != Items.AIR) {
                builder.add(item.getDefaultInstance());
            }
        }));
        allDefs.stream().filter(def -> !def.isItem()).forEach(tagDef -> tagDef.getNames().forEach(name -> {
            TagKey<Item> tag = ItemTags.create(name);
            if (ForgeRegistries.ITEMS.tags().isKnownTagName(tag)) {
                Ingredient.TagValue tagIngredient = new Ingredient.TagValue(tag);
                tagIngredient.getItems().forEach(stack -> {
                    if (getFoodValues(stack, level).has(category)) {
                        builder.add(stack);
                    }
                });
            }
        }));
        return builder.build();
    }

    @Nonnull
    public static List<FoodValuesDefinition.FoodCategoryMatchedItems> getFoodCategoryMatchedItemsList(Level level) {
        com.google.common.collect.ImmutableList.Builder<FoodValuesDefinition.FoodCategoryMatchedItems> builder = ImmutableList.builder();
        for (FoodCategory category : FoodCategory.values()) {
            builder.add(new FoodValuesDefinition.FoodCategoryMatchedItems(category, getMatchedItems(category, level)));
        }
        return builder.build();
    }

    @Nonnull
    @Override
    public RecipeSerializer<?> getSerializer() {
        return CrockPotRecipes.FOOD_VALUES_RECIPE_SERIALIZER.get();
    }

    @Nonnull
    @Override
    public RecipeType<?> getType() {
        return CrockPotRecipes.FOOD_VALUES_RECIPE_TYPE.get();
    }

    public static record FoodCategoryMatchedItems(FoodCategory category, Set<ItemStack> items) {
    }

    public static class Serializer implements RecipeSerializer<FoodValuesDefinition> {

        public FoodValuesDefinition fromJson(ResourceLocation recipeId, JsonObject serializedRecipe) {
            FoodValues foodValues = FoodValues.fromJson(GsonHelper.getAsJsonObject(serializedRecipe, "values"));
            if (serializedRecipe.has("items") && serializedRecipe.has("tags")) {
                throw new JsonParseException("A food value definition entry needs either tags or items, not both");
            } else if (!serializedRecipe.has("items") && !serializedRecipe.has("tags")) {
                throw new JsonParseException("A food value definition entry needs either tags or items");
            } else {
                HashSet<ResourceLocation> names = new HashSet();
                boolean isItem = serializedRecipe.has("items");
                GsonHelper.getAsJsonArray(serializedRecipe, isItem ? "items" : "tags").forEach(name -> names.add(new ResourceLocation(GsonHelper.convertToString(name, isItem ? "item" : "tag"))));
                return new FoodValuesDefinition(recipeId, names, foodValues, isItem);
            }
        }

        @Nullable
        public FoodValuesDefinition fromNetwork(ResourceLocation recipeId, FriendlyByteBuf buffer) {
            boolean isItem = buffer.readBoolean();
            HashSet<ResourceLocation> names = new HashSet();
            int length = buffer.readVarInt();
            for (int i = 0; i < length; i++) {
                names.add(buffer.readResourceLocation());
            }
            FoodValues foodValues = FoodValues.fromNetwork(buffer);
            return new FoodValuesDefinition(recipeId, names, foodValues, isItem);
        }

        public void toNetwork(FriendlyByteBuf buffer, FoodValuesDefinition recipe) {
            buffer.writeBoolean(recipe.isItem());
            buffer.writeVarInt(recipe.getNames().size());
            recipe.getNames().forEach(buffer::m_130085_);
            recipe.getFoodValues().toNetwork(buffer);
        }
    }
}