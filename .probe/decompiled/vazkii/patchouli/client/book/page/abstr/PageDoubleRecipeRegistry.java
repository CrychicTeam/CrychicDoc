package vazkii.patchouli.client.book.page.abstr;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;
import vazkii.patchouli.api.PatchouliAPI;
import vazkii.patchouli.client.book.BookContentsBuilder;
import vazkii.patchouli.client.book.BookEntry;

public abstract class PageDoubleRecipeRegistry<T extends Recipe<?>> extends PageDoubleRecipe<T> {

    private final RecipeType<? extends T> recipeType;

    public PageDoubleRecipeRegistry(RecipeType<? extends T> recipeType) {
        this.recipeType = recipeType;
    }

    @Nullable
    private T getRecipe(Level level, ResourceLocation id) {
        RecipeManager manager = level.getRecipeManager();
        return (T) manager.byKey(id).filter(recipe -> recipe.getType() == this.recipeType).orElse(null);
    }

    protected T loadRecipe(Level level, BookContentsBuilder builder, BookEntry entry, ResourceLocation res) {
        if (res != null && level != null) {
            T tempRecipe = this.getRecipe(level, res);
            if (tempRecipe == null) {
                tempRecipe = this.getRecipe(level, new ResourceLocation("crafttweaker", res.getPath()));
            }
            if (tempRecipe != null) {
                entry.addRelevantStack(builder, tempRecipe.getResultItem(level.registryAccess()), this.pageNum);
                return tempRecipe;
            } else {
                PatchouliAPI.LOGGER.warn("Recipe {} (of type {}) not found", res, BuiltInRegistries.RECIPE_TYPE.getKey(this.recipeType));
                return null;
            }
        } else {
            return null;
        }
    }
}