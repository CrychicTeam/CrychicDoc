package dev.xkmc.l2complements.content.recipe;

import dev.xkmc.l2complements.init.registrate.LCRecipes;
import dev.xkmc.l2library.serial.recipe.BaseRecipe;
import dev.xkmc.l2library.serial.recipe.BaseRecipeBuilder;
import net.minecraft.world.level.block.Block;

public class DiffusionRecipeBuilder extends BaseRecipeBuilder<DiffusionRecipeBuilder, DiffusionRecipe, DiffusionRecipe, DiffusionRecipe.Inv> {

    public DiffusionRecipeBuilder(Block in, Block base, Block out) {
        super((BaseRecipe.RecType<DiffusionRecipe, DiffusionRecipe, DiffusionRecipe.Inv>) LCRecipes.RS_DIFFUSION.get());
        this.recipe.ingredient = in;
        this.recipe.base = base;
        this.recipe.result = out;
    }
}