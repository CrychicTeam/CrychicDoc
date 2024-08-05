package net.minecraft.client.gui.screens.recipebook;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import java.util.List;
import java.util.Set;
import net.minecraft.core.RegistryAccess;
import net.minecraft.stats.RecipeBook;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;

public class RecipeCollection {

    private final RegistryAccess registryAccess;

    private final List<Recipe<?>> recipes;

    private final boolean singleResultItem;

    private final Set<Recipe<?>> craftable = Sets.newHashSet();

    private final Set<Recipe<?>> fitsDimensions = Sets.newHashSet();

    private final Set<Recipe<?>> known = Sets.newHashSet();

    public RecipeCollection(RegistryAccess registryAccess0, List<Recipe<?>> listRecipe1) {
        this.registryAccess = registryAccess0;
        this.recipes = ImmutableList.copyOf(listRecipe1);
        if (listRecipe1.size() <= 1) {
            this.singleResultItem = true;
        } else {
            this.singleResultItem = allRecipesHaveSameResult(registryAccess0, listRecipe1);
        }
    }

    private static boolean allRecipesHaveSameResult(RegistryAccess registryAccess0, List<Recipe<?>> listRecipe1) {
        int $$2 = listRecipe1.size();
        ItemStack $$3 = ((Recipe) listRecipe1.get(0)).getResultItem(registryAccess0);
        for (int $$4 = 1; $$4 < $$2; $$4++) {
            ItemStack $$5 = ((Recipe) listRecipe1.get($$4)).getResultItem(registryAccess0);
            if (!ItemStack.isSameItemSameTags($$3, $$5)) {
                return false;
            }
        }
        return true;
    }

    public RegistryAccess registryAccess() {
        return this.registryAccess;
    }

    public boolean hasKnownRecipes() {
        return !this.known.isEmpty();
    }

    public void updateKnownRecipes(RecipeBook recipeBook0) {
        for (Recipe<?> $$1 : this.recipes) {
            if (recipeBook0.contains($$1)) {
                this.known.add($$1);
            }
        }
    }

    public void canCraft(StackedContents stackedContents0, int int1, int int2, RecipeBook recipeBook3) {
        for (Recipe<?> $$4 : this.recipes) {
            boolean $$5 = $$4.canCraftInDimensions(int1, int2) && recipeBook3.contains($$4);
            if ($$5) {
                this.fitsDimensions.add($$4);
            } else {
                this.fitsDimensions.remove($$4);
            }
            if ($$5 && stackedContents0.canCraft($$4, null)) {
                this.craftable.add($$4);
            } else {
                this.craftable.remove($$4);
            }
        }
    }

    public boolean isCraftable(Recipe<?> recipe0) {
        return this.craftable.contains(recipe0);
    }

    public boolean hasCraftable() {
        return !this.craftable.isEmpty();
    }

    public boolean hasFitting() {
        return !this.fitsDimensions.isEmpty();
    }

    public List<Recipe<?>> getRecipes() {
        return this.recipes;
    }

    public List<Recipe<?>> getRecipes(boolean boolean0) {
        List<Recipe<?>> $$1 = Lists.newArrayList();
        Set<Recipe<?>> $$2 = boolean0 ? this.craftable : this.fitsDimensions;
        for (Recipe<?> $$3 : this.recipes) {
            if ($$2.contains($$3)) {
                $$1.add($$3);
            }
        }
        return $$1;
    }

    public List<Recipe<?>> getDisplayRecipes(boolean boolean0) {
        List<Recipe<?>> $$1 = Lists.newArrayList();
        for (Recipe<?> $$2 : this.recipes) {
            if (this.fitsDimensions.contains($$2) && this.craftable.contains($$2) == boolean0) {
                $$1.add($$2);
            }
        }
        return $$1;
    }

    public boolean hasSingleResultItem() {
        return this.singleResultItem;
    }
}