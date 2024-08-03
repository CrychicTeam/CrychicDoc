package com.rekindled.embers.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;

public class MachineRecipeEvent<T> extends UpgradeEvent {

    T recipe;

    T originalRecipe;

    public MachineRecipeEvent(BlockEntity tile, T recipe) {
        super(tile);
        this.recipe = recipe;
        this.originalRecipe = recipe;
    }

    public T getOriginalRecipe() {
        return this.originalRecipe;
    }

    public T getRecipe() {
        return this.recipe;
    }

    public void setRecipe(T recipe) {
        this.recipe = recipe;
    }

    public static class Success<T> extends MachineRecipeEvent<T> {

        public Success(BlockEntity tile, T recipe) {
            super(tile, recipe);
        }
    }
}