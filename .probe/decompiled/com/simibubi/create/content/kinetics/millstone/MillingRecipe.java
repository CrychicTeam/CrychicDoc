package com.simibubi.create.content.kinetics.millstone;

import com.simibubi.create.AllRecipeTypes;
import com.simibubi.create.content.kinetics.crusher.AbstractCrushingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

@ParametersAreNonnullByDefault
public class MillingRecipe extends AbstractCrushingRecipe {

    public MillingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(AllRecipeTypes.MILLING, params);
    }

    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return inv.isEmpty() ? false : this.ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }
}