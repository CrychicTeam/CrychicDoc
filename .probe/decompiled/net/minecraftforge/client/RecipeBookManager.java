package net.minecraftforge.client;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.inventory.RecipeBookType;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import net.minecraftforge.fml.ModLoader;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.ApiStatus.Internal;

public final class RecipeBookManager {

    private static final Map<RecipeBookCategories, List<RecipeBookCategories>> AGGREGATE_CATEGORIES = new HashMap();

    private static final Map<RecipeBookType, List<RecipeBookCategories>> TYPE_CATEGORIES = new HashMap();

    private static final Map<RecipeType<?>, Function<Recipe<?>, RecipeBookCategories>> RECIPE_CATEGORY_LOOKUPS = new HashMap();

    private static final Map<RecipeBookCategories, List<RecipeBookCategories>> AGGREGATE_CATEGORIES_VIEW = Collections.unmodifiableMap(AGGREGATE_CATEGORIES);

    @Nullable
    public static <T extends Recipe<?>> RecipeBookCategories findCategories(RecipeType<T> type, T recipe) {
        Function<Recipe<?>, RecipeBookCategories> lookup = (Function<Recipe<?>, RecipeBookCategories>) RECIPE_CATEGORY_LOOKUPS.get(type);
        return lookup != null ? (RecipeBookCategories) lookup.apply(recipe) : null;
    }

    @Internal
    public static Map<RecipeBookCategories, List<RecipeBookCategories>> getAggregateCategories() {
        return AGGREGATE_CATEGORIES_VIEW;
    }

    @Internal
    public static List<RecipeBookCategories> getCustomCategoriesOrEmpty(RecipeBookType recipeBookType) {
        return (List<RecipeBookCategories>) TYPE_CATEGORIES.getOrDefault(recipeBookType, List.of());
    }

    @Internal
    public static void init() {
        HashMap<RecipeBookCategories, ImmutableList<RecipeBookCategories>> aggregateCategories = new HashMap(ImmutableMap.of(RecipeBookCategories.CRAFTING_SEARCH, ImmutableList.of(RecipeBookCategories.CRAFTING_EQUIPMENT, RecipeBookCategories.CRAFTING_BUILDING_BLOCKS, RecipeBookCategories.CRAFTING_MISC, RecipeBookCategories.CRAFTING_REDSTONE), RecipeBookCategories.FURNACE_SEARCH, ImmutableList.of(RecipeBookCategories.FURNACE_FOOD, RecipeBookCategories.FURNACE_BLOCKS, RecipeBookCategories.FURNACE_MISC), RecipeBookCategories.BLAST_FURNACE_SEARCH, ImmutableList.of(RecipeBookCategories.BLAST_FURNACE_BLOCKS, RecipeBookCategories.BLAST_FURNACE_MISC), RecipeBookCategories.SMOKER_SEARCH, ImmutableList.of(RecipeBookCategories.SMOKER_FOOD)));
        HashMap<RecipeBookType, ImmutableList<RecipeBookCategories>> typeCategories = new HashMap();
        HashMap<RecipeType<?>, Function<Recipe<?>, RecipeBookCategories>> recipeCategoryLookups = new HashMap();
        RegisterRecipeBookCategoriesEvent event = new RegisterRecipeBookCategoriesEvent(aggregateCategories, typeCategories, recipeCategoryLookups);
        ModLoader.get().postEventWrapContainerInModOrder(event);
        AGGREGATE_CATEGORIES.putAll(aggregateCategories);
        TYPE_CATEGORIES.putAll(typeCategories);
        RECIPE_CATEGORY_LOOKUPS.putAll(recipeCategoryLookups);
    }
}