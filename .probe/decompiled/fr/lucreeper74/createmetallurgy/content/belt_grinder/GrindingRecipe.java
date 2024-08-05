package fr.lucreeper74.createmetallurgy.content.belt_grinder;

import com.simibubi.create.content.processing.recipe.ProcessingRecipe;
import com.simibubi.create.content.processing.recipe.ProcessingRecipeBuilder;
import fr.lucreeper74.createmetallurgy.registries.CMRecipeTypes;
import net.minecraft.world.level.Level;
import net.minecraftforge.items.wrapper.RecipeWrapper;

public class GrindingRecipe extends ProcessingRecipe<RecipeWrapper> {

    public GrindingRecipe(ProcessingRecipeBuilder.ProcessingRecipeParams params) {
        super(CMRecipeTypes.GRINDING, params);
    }

    public boolean matches(RecipeWrapper inv, Level worldIn) {
        return inv.isEmpty() ? false : this.ingredients.get(0).test(inv.getItem(0));
    }

    @Override
    protected int getMaxInputCount() {
        return 1;
    }

    @Override
    protected int getMaxOutputCount() {
        return 4;
    }

    @Override
    protected boolean canSpecifyDuration() {
        return true;
    }
}