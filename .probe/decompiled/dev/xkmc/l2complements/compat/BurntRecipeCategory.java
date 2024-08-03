package dev.xkmc.l2complements.compat;

import dev.xkmc.l2complements.content.recipe.BurntRecipe;
import dev.xkmc.l2complements.init.data.LangData;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class BurntRecipeCategory extends BaseRecipeCategory<BurntRecipe, BurntRecipeCategory> {

    protected static final ResourceLocation BG = new ResourceLocation("l2complements", "textures/jei/background.png");

    public BurntRecipeCategory() {
        super(new ResourceLocation("l2complements", "ritual"), BurntRecipe.class);
    }

    public BurntRecipeCategory init(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BG, 0, 0, 72, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.LAVA_BUCKET.getDefaultInstance());
        return this;
    }

    @Override
    public Component getTitle() {
        return LangData.IDS.BURNT_TITLE.get();
    }

    public void setRecipe(IRecipeLayoutBuilder builder, BurntRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addIngredients(recipe.ingredient);
        builder.addSlot(RecipeIngredientRole.OUTPUT, 55, 1).addItemStack(recipe.result).addTooltipCallback((e, list) -> list.add(LangData.IDS.BURNT_COUNT.get(recipe.chance)));
    }
}