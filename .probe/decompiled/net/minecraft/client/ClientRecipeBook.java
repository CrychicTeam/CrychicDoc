package net.minecraft.client;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Table;
import com.google.common.collect.ImmutableList.Builder;
import com.mojang.logging.LogUtils;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import net.minecraft.client.gui.screens.recipebook.RecipeCollection;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.item.crafting.CookingBookCategory;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeType;
import org.slf4j.Logger;

public class ClientRecipeBook extends RecipeBook {

    private static final Logger LOGGER = LogUtils.getLogger();

    private Map<RecipeBookCategories, List<RecipeCollection>> collectionsByTab = ImmutableMap.of();

    private List<RecipeCollection> allCollections = ImmutableList.of();

    public void setupCollections(Iterable<Recipe<?>> iterableRecipe0, RegistryAccess registryAccess1) {
        Map<RecipeBookCategories, List<List<Recipe<?>>>> $$2 = categorizeAndGroupRecipes(iterableRecipe0);
        Map<RecipeBookCategories, List<RecipeCollection>> $$3 = Maps.newHashMap();
        Builder<RecipeCollection> $$4 = ImmutableList.builder();
        $$2.forEach((p_266602_, p_266603_) -> $$3.put(p_266602_, (List) p_266603_.stream().map(p_266605_ -> new RecipeCollection(registryAccess1, p_266605_)).peek($$4::add).collect(ImmutableList.toImmutableList())));
        RecipeBookCategories.AGGREGATE_CATEGORIES.forEach((p_90637_, p_90638_) -> $$3.put(p_90637_, (List) p_90638_.stream().flatMap(p_167706_ -> ((List) $$3.getOrDefault(p_167706_, ImmutableList.of())).stream()).collect(ImmutableList.toImmutableList())));
        this.collectionsByTab = ImmutableMap.copyOf($$3);
        this.allCollections = $$4.build();
    }

    private static Map<RecipeBookCategories, List<List<Recipe<?>>>> categorizeAndGroupRecipes(Iterable<Recipe<?>> iterableRecipe0) {
        Map<RecipeBookCategories, List<List<Recipe<?>>>> $$1 = Maps.newHashMap();
        Table<RecipeBookCategories, String, List<Recipe<?>>> $$2 = HashBasedTable.create();
        for (Recipe<?> $$3 : iterableRecipe0) {
            if (!$$3.isSpecial() && !$$3.isIncomplete()) {
                RecipeBookCategories $$4 = getCategory($$3);
                String $$5 = $$3.getGroup();
                if ($$5.isEmpty()) {
                    ((List) $$1.computeIfAbsent($$4, p_90645_ -> Lists.newArrayList())).add(ImmutableList.of($$3));
                } else {
                    List<Recipe<?>> $$6 = (List<Recipe<?>>) $$2.get($$4, $$5);
                    if ($$6 == null) {
                        $$6 = Lists.newArrayList();
                        $$2.put($$4, $$5, $$6);
                        ((List) $$1.computeIfAbsent($$4, p_90641_ -> Lists.newArrayList())).add($$6);
                    }
                    $$6.add($$3);
                }
            }
        }
        return $$1;
    }

    private static RecipeBookCategories getCategory(Recipe<?> recipe0) {
        if (recipe0 instanceof CraftingRecipe $$1) {
            return switch($$1.category()) {
                case BUILDING ->
                    RecipeBookCategories.CRAFTING_BUILDING_BLOCKS;
                case EQUIPMENT ->
                    RecipeBookCategories.CRAFTING_EQUIPMENT;
                case REDSTONE ->
                    RecipeBookCategories.CRAFTING_REDSTONE;
                case MISC ->
                    RecipeBookCategories.CRAFTING_MISC;
            };
        } else {
            RecipeType<?> $$2 = recipe0.getType();
            if (recipe0 instanceof AbstractCookingRecipe $$3) {
                CookingBookCategory $$4 = $$3.category();
                if ($$2 == RecipeType.SMELTING) {
                    return switch($$4) {
                        case BLOCKS ->
                            RecipeBookCategories.FURNACE_BLOCKS;
                        case FOOD ->
                            RecipeBookCategories.FURNACE_FOOD;
                        case MISC ->
                            RecipeBookCategories.FURNACE_MISC;
                    };
                }
                if ($$2 == RecipeType.BLASTING) {
                    return $$4 == CookingBookCategory.BLOCKS ? RecipeBookCategories.BLAST_FURNACE_BLOCKS : RecipeBookCategories.BLAST_FURNACE_MISC;
                }
                if ($$2 == RecipeType.SMOKING) {
                    return RecipeBookCategories.SMOKER_FOOD;
                }
                if ($$2 == RecipeType.CAMPFIRE_COOKING) {
                    return RecipeBookCategories.CAMPFIRE;
                }
            }
            if ($$2 == RecipeType.STONECUTTING) {
                return RecipeBookCategories.STONECUTTER;
            } else if ($$2 == RecipeType.SMITHING) {
                return RecipeBookCategories.SMITHING;
            } else {
                LOGGER.warn("Unknown recipe category: {}/{}", LogUtils.defer(() -> BuiltInRegistries.RECIPE_TYPE.getKey(recipe0.getType())), LogUtils.defer(recipe0::m_6423_));
                return RecipeBookCategories.UNKNOWN;
            }
        }
    }

    public List<RecipeCollection> getCollections() {
        return this.allCollections;
    }

    public List<RecipeCollection> getCollection(RecipeBookCategories recipeBookCategories0) {
        return (List<RecipeCollection>) this.collectionsByTab.getOrDefault(recipeBookCategories0, Collections.emptyList());
    }
}