package dev.xkmc.l2complements.compat;

import dev.xkmc.l2complements.content.recipe.DiffusionRecipe;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2complements.init.registrate.LCItems;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class DiffuseRecipeCategory extends BaseRecipeCategory<DiffusionRecipe, DiffuseRecipeCategory> {

    protected static final ResourceLocation BG = new ResourceLocation("l2complements", "textures/jei/background.png");

    public DiffuseRecipeCategory() {
        super(new ResourceLocation("l2complements", "diffusion"), DiffusionRecipe.class);
    }

    public DiffuseRecipeCategory init(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BG, 0, 18, 90, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, LCItems.DIFFUSION_WAND.asStack());
        return this;
    }

    @Override
    public Component getTitle() {
        return LangData.IDS.DIFFUSE_TITLE.get();
    }

    public void setRecipe(IRecipeLayoutBuilder builder, DiffusionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStack(recipe.ingredient.asItem().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStack(recipe.base.asItem().getDefaultInstance());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 1).addItemStack(recipe.result.asItem().getDefaultInstance());
    }
}