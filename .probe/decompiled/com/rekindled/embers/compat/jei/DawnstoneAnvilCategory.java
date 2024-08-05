package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IDawnstoneAnvilRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class DawnstoneAnvilCategory implements IRecipeCategory<IDawnstoneAnvilRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.dawnstone_anvil");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_dawnstone_anvil.png");

    public DawnstoneAnvilCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 110, 58);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.DAWNSTONE_ANVIL_ITEM.get()));
    }

    @Override
    public RecipeType<IDawnstoneAnvilRecipe> getRecipeType() {
        return JEIPlugin.DAWNSTONE_ANVIL;
    }

    @Override
    public Component getTitle() {
        return title;
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, IDawnstoneAnvilRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 19).addItemStacks(recipe.getDisplayInputTop());
        builder.addSlot(RecipeIngredientRole.INPUT, 22, 37).addItemStacks(recipe.getDisplayInputBottom());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 28).addItemStacks(recipe.getDisplayOutput());
    }
}