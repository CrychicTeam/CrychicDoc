package mezz.jei.library.plugins.vanilla.stonecutting;

import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.common.Constants;
import mezz.jei.library.util.RecipeUtil;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.StonecutterRecipe;
import net.minecraft.world.level.block.Blocks;

public class StoneCuttingRecipeCategory implements IRecipeCategory<StonecutterRecipe> {

    public static final int width = 82;

    public static final int height = 34;

    private final IDrawable background;

    private final IDrawable icon;

    private final Component localizedName;

    public StoneCuttingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = Constants.RECIPE_GUI_VANILLA;
        this.background = guiHelper.createDrawable(location, 0, 220, 82, 34);
        this.icon = guiHelper.createDrawableItemStack(new ItemStack(Blocks.STONECUTTER));
        this.localizedName = Component.translatable("gui.jei.category.stoneCutter");
    }

    @Override
    public RecipeType<StonecutterRecipe> getRecipeType() {
        return RecipeTypes.STONECUTTING;
    }

    @Override
    public Component getTitle() {
        return this.localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, StonecutterRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients((Ingredient) recipe.m_7527_().get(0));
        builder.addSlot(RecipeIngredientRole.OUTPUT, 61, 9).addItemStack(RecipeUtil.getResultItem(recipe));
    }

    public boolean isHandled(StonecutterRecipe recipe) {
        return !recipe.m_5598_();
    }
}