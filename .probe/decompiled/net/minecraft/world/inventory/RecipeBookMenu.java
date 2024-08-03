package net.minecraft.world.inventory;

import net.minecraft.recipebook.ServerPlaceRecipe;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.crafting.Recipe;

public abstract class RecipeBookMenu<C extends Container> extends AbstractContainerMenu {

    public RecipeBookMenu(MenuType<?> menuType0, int int1) {
        super(menuType0, int1);
    }

    public void handlePlacement(boolean boolean0, Recipe<?> recipe1, ServerPlayer serverPlayer2) {
        new ServerPlaceRecipe<>(this).recipeClicked(serverPlayer2, (Recipe<C>) recipe1, boolean0);
    }

    public abstract void fillCraftSlotsStackedContents(StackedContents var1);

    public abstract void clearCraftingContent();

    public abstract boolean recipeMatches(Recipe<? super C> var1);

    public abstract int getResultSlotIndex();

    public abstract int getGridWidth();

    public abstract int getGridHeight();

    public abstract int getSize();

    public abstract RecipeBookType getRecipeBookType();

    public abstract boolean shouldMoveToInventory(int var1);
}