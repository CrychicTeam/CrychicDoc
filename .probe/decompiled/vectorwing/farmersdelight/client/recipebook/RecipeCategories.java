package vectorwing.farmersdelight.client.recipebook;

import com.google.common.base.Suppliers;
import com.google.common.collect.ImmutableList;
import java.util.function.Supplier;
import net.minecraft.client.RecipeBookCategories;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraftforge.client.event.RegisterRecipeBookCategoriesEvent;
import vectorwing.farmersdelight.FarmersDelight;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.registry.ModItems;
import vectorwing.farmersdelight.common.registry.ModRecipeTypes;

public class RecipeCategories {

    public static final Supplier<RecipeBookCategories> COOKING_SEARCH = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_SEARCH", new ItemStack[] { new ItemStack(Items.COMPASS) }));

    public static final Supplier<RecipeBookCategories> COOKING_MEALS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MEALS", new ItemStack[] { new ItemStack(ModItems.VEGETABLE_NOODLES.get()) }));

    public static final Supplier<RecipeBookCategories> COOKING_DRINKS = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_DRINKS", new ItemStack[] { new ItemStack(ModItems.APPLE_CIDER.get()) }));

    public static final Supplier<RecipeBookCategories> COOKING_MISC = Suppliers.memoize(() -> RecipeBookCategories.create("COOKING_MISC", new ItemStack[] { new ItemStack(ModItems.DUMPLINGS.get()), new ItemStack(ModItems.TOMATO_SAUCE.get()) }));

    public static void init(RegisterRecipeBookCategoriesEvent event) {
        event.registerBookCategories(FarmersDelight.RECIPE_TYPE_COOKING, ImmutableList.of((RecipeBookCategories) COOKING_SEARCH.get(), (RecipeBookCategories) COOKING_MEALS.get(), (RecipeBookCategories) COOKING_DRINKS.get(), (RecipeBookCategories) COOKING_MISC.get()));
        event.registerAggregateCategory((RecipeBookCategories) COOKING_SEARCH.get(), ImmutableList.of((RecipeBookCategories) COOKING_MEALS.get(), (RecipeBookCategories) COOKING_DRINKS.get(), (RecipeBookCategories) COOKING_MISC.get()));
        event.registerRecipeCategoryFinder(ModRecipeTypes.COOKING.get(), recipe -> {
            if (recipe instanceof CookingPotRecipe cookingRecipe) {
                CookingPotRecipeBookTab tab = cookingRecipe.getRecipeBookTab();
                if (tab != null) {
                    return switch(tab) {
                        case MEALS ->
                            (RecipeBookCategories) COOKING_MEALS.get();
                        case DRINKS ->
                            (RecipeBookCategories) COOKING_DRINKS.get();
                        case MISC ->
                            (RecipeBookCategories) COOKING_MISC.get();
                    };
                }
            }
            return (RecipeBookCategories) COOKING_MISC.get();
        });
    }
}