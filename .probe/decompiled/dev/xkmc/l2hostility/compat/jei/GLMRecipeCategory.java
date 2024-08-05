package dev.xkmc.l2hostility.compat.jei;

import dev.xkmc.l2hostility.init.data.LangData;
import dev.xkmc.l2library.serial.recipe.BaseRecipeCategory;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;

public class GLMRecipeCategory extends BaseRecipeCategory<ITraitLootRecipe, GLMRecipeCategory> {

    protected static final ResourceLocation BG = new ResourceLocation("l2complements", "textures/jei/background.png");

    public GLMRecipeCategory() {
        super(new ResourceLocation("l2hostility", "loot"), ITraitLootRecipe.class);
    }

    public GLMRecipeCategory init(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(BG, 0, 18, 90, 18);
        this.icon = guiHelper.createDrawableIngredient(VanillaTypes.ITEM_STACK, Items.IRON_SWORD.getDefaultInstance());
        return this;
    }

    @Override
    public Component getTitle() {
        return LangData.LOOT_TITLE.get();
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ITraitLootRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 1).addItemStacks(recipe.getCurioRequired());
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 1).addItemStacks(recipe.getInputs());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 73, 1).addItemStacks(recipe.getResults()).addTooltipCallback((v, l) -> recipe.addTooltip(l));
    }
}