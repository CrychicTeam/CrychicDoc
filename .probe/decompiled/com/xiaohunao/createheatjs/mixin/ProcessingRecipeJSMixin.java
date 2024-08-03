package com.xiaohunao.createheatjs.mixin;

import dev.latvian.mods.kubejs.create.ProcessingRecipeSchema;
import dev.latvian.mods.kubejs.recipe.RecipeJS;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

@Mixin({ ProcessingRecipeSchema.ProcessingRecipeJS.class })
public abstract class ProcessingRecipeJSMixin extends RecipeJS {

    @Unique
    public RecipeJS heatLevel(String heatLevel) {
        return this.setValue(ProcessingRecipeSchema.HEAT_REQUIREMENT, heatLevel.toLowerCase());
    }
}