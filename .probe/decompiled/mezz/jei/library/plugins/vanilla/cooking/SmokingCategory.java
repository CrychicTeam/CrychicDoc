package mezz.jei.library.plugins.vanilla.cooking;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.world.item.crafting.SmokingRecipe;
import net.minecraft.world.level.block.Blocks;

public class SmokingCategory extends AbstractCookingCategory<SmokingRecipe> {

    public SmokingCategory(IGuiHelper guiHelper) {
        super(guiHelper, Blocks.SMOKER, "gui.jei.category.smoking", 100);
    }

    @Override
    public RecipeType<SmokingRecipe> getRecipeType() {
        return RecipeTypes.SMOKING;
    }
}