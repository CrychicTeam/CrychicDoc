package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrate;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedLootCrateCache;
import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;

public enum LootCrateRecipeManagerPlugin implements IRecipeManagerPlugin {

    INSTANCE;

    private final WrappedLootCrateCache cache = new WrappedLootCrateCache(crates -> {
        if (FTBQuestsJEIIntegration.runtime != null && !crates.isEmpty()) {
            FTBQuestsJEIIntegration.runtime.getIngredientManager().removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, crates);
        }
    }, crates -> {
        if (FTBQuestsJEIIntegration.runtime != null && !crates.isEmpty()) {
            FTBQuestsJEIIntegration.runtime.getIngredientManager().addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, crates);
        }
    });

    @Override
    public <V> List<RecipeType<?>> getRecipeTypes(IFocus<V> focus) {
        if (focus.getTypedValue().getIngredient() instanceof ItemStack stack) {
            if (focus.getRole() == RecipeIngredientRole.INPUT) {
                if (stack.getItem() == FTBQuestsItems.LOOTCRATE.get()) {
                    return List.of(JEIRecipeTypes.LOOT_CRATE);
                }
            } else if (focus.getRole() == RecipeIngredientRole.OUTPUT && !this.cache.findCratesWithOutput(stack).isEmpty()) {
                return List.of(JEIRecipeTypes.LOOT_CRATE);
            }
        }
        return List.of();
    }

    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        if (!(recipeCategory instanceof LootCrateCategory) || !(focus.getTypedValue().getIngredient() instanceof ItemStack stack)) {
            return List.of();
        } else if (stack.getItem() == FTBQuestsItems.LOOTCRATE.get() && focus.getRole() == RecipeIngredientRole.CATALYST) {
            return (List<T>) this.cache.getWrappedLootCrates();
        } else {
            return switch(focus.getRole()) {
                case INPUT ->
                    this.cache.findCratesWithInput(stack);
                case OUTPUT ->
                    this.cache.findCratesWithOutput(stack);
                default ->
                    List.of();
            };
        }
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
        return (List<T>) (recipeCategory instanceof LootCrateCategory ? this.cache.getWrappedLootCrates() : List.of());
    }

    public List<WrappedLootCrate> getWrappedLootCrates() {
        return this.cache.getWrappedLootCrates();
    }

    public void refresh() {
        this.cache.refresh();
    }
}