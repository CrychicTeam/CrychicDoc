package net.minecraftforge.client.event;

import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.eventbus.api.Event;
import org.jetbrains.annotations.ApiStatus.Internal;

public class RecipesUpdatedEvent extends Event {

    private final RecipeManager recipeManager;

    @Internal
    public RecipesUpdatedEvent(RecipeManager recipeManager) {
        this.recipeManager = recipeManager;
    }

    public RecipeManager getRecipeManager() {
        return this.recipeManager;
    }
}