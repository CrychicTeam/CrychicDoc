package com.simibubi.create.content.kinetics.fan.processing;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class HauntingRecipe extends ProcessingRecipe<HauntingRecipe.HauntingWrapper> {

    public HauntingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.HAUNTING, params);
    }

    public boolean matches(HauntingRecipe.HauntingWrapper inv, Level worldIn) {
        return inv.m_7983_() ? false : this.ingredients.get(0).test(inv.m_8020_(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 12;
    }

    public static class HauntingWrapper extends RecipeWrapper {

        public HauntingWrapper() {
            super(new ItemStackHandler(1));
        }
    }
}