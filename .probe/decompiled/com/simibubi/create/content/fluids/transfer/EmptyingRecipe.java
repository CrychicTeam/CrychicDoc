package com.simibubi.create.content.fluids.transfer;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import net.minecraft.world.level.Level;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class EmptyingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public EmptyingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.EMPTYING, params);
    }

    public boolean matches(RecipeWrapper inv, Level p_77569_2_) {
        return this.ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 1;
    }

    @Override
    protected int getMaxFluidOutputCount() {
        return 1;
    }

    public FluidStack getResultingFluid() {
        if (this.fluidResults.isEmpty()) {
            throw new IllegalStateException("Emptying Recipe: " + this.id.toString() + " has no fluid output!");
        } else {
            return this.fluidResults.get(0);
        }
    }
}