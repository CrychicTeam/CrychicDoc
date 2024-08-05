package com.simibubi.create.foundation.data.recipe;

import com.simibubi.create.AllItems;
import com.simibubi.create.AllRecipeTypes;
import net.minecraft.data.PackOutput;
import net.minecraft.world.level.ItemLike;

public class PolishingRecipeGen extends ProcessingRecipeGen {

    CreateRecipeProvider.GeneratedRecipe ROSE_QUARTZ = this.create(AllItems.ROSE_QUARTZ::get, b -> b.output((ItemLike) AllItems.POLISHED_ROSE_QUARTZ.get()));

    public PolishingRecipeGen(PackOutput output) {
        super(output);
    }

    protected AllRecipeTypes getRecipeType() {
        return AllRecipeTypes.SANDPAPER_POLISHING;
    }
}