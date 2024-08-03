package com.simibubi.create.content.kinetics.deployer;

import java.util.Optional;
import java.util.function.Supplier;
import javax.annotation.Nullable;
import net.minecraft.world.Container;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class DeployerRecipeSearchEvent extends Event {

    private final DeployerBlockEntity blockEntity;

    private final RecipeWrapper inventory;

    @Nullable
    Recipe<? extends Container> recipe = null;

    private int maxPriority = 0;

    public DeployerRecipeSearchEvent(DeployerBlockEntity blockEntity, RecipeWrapper inventory) {
        this.blockEntity = blockEntity;
        this.inventory = inventory;
    }

    public boolean isCancelable() {
        return true;
    }

    public DeployerBlockEntity getBlockEntity() {
        return this.blockEntity;
    }

    public RecipeWrapper getInventory() {
        return this.inventory;
    }

    public boolean shouldAddRecipeWithPriority(int priority) {
        return !this.isCanceled() && priority > this.maxPriority;
    }

    @Nullable
    public Recipe<? extends Container> getRecipe() {
        return this.isCanceled() ? null : this.recipe;
    }

    public void addRecipe(Supplier<Optional<? extends Recipe<? extends Container>>> recipeSupplier, int priority) {
        if (this.shouldAddRecipeWithPriority(priority)) {
            ((Optional) recipeSupplier.get()).ifPresent(newRecipe -> {
                this.recipe = newRecipe;
                this.maxPriority = priority;
            });
        }
    }
}