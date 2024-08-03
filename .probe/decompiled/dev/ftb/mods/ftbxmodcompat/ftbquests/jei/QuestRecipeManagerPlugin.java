package dev.ftb.mods.ftbxmodcompat.ftbquests.jei;

import dev.ftb.mods.ftbquests.item.FTBQuestsItems;
import dev.ftb.mods.ftbxmodcompat.ftbquests.recipemod_common.WrappedQuestCache;
import java.util.List;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.advanced.IRecipeManagerPlugin;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.world.item.ItemStack;

public enum QuestRecipeManagerPlugin implements IRecipeManagerPlugin {

    INSTANCE;

    private final WrappedQuestCache cache = new WrappedQuestCache();

    public void refresh() {
        this.cache.clear();
    }

    @Override
    public <T, V> List<T> getRecipes(IRecipeCategory<T> recipeCategory, IFocus<V> focus) {
        if (!(recipeCategory instanceof QuestCategory) || !(focus.getTypedValue().getIngredient() instanceof ItemStack stack)) {
            return List.of();
        } else if (stack.getItem() == FTBQuestsItems.BOOK.get() && focus.getRole() == RecipeIngredientRole.CATALYST) {
            return (List<T>) this.cache.getCachedItems();
        } else {
            return switch(focus.getRole()) {
                case INPUT ->
                    this.cache.findQuestsWithInput(stack);
                case OUTPUT ->
                    this.cache.findQuestsWithOutput(stack);
                default ->
                    List.of();
            };
        }
    }

    @Override
    public <T> List<T> getRecipes(IRecipeCategory<T> recipeCategory) {
        return (List<T>) (recipeCategory instanceof QuestCategory ? this.cache.getCachedItems() : List.of());
    }

    @Override
    public <V> List<RecipeType<?>> getRecipeTypes(IFocus<V> focus) {
        if (focus.getTypedValue().getIngredient() instanceof ItemStack stack && (focus.getRole() == RecipeIngredientRole.INPUT && (stack.getItem() == FTBQuestsItems.BOOK.get() || !this.cache.findQuestsWithInput(stack).isEmpty()) || focus.getRole() == RecipeIngredientRole.OUTPUT && !this.cache.findQuestsWithOutput(stack).isEmpty())) {
            return List.of(JEIRecipeTypes.QUEST);
        }
        return List.of();
    }
}