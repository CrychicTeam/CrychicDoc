package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IMeltingRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.forge.ForgeTypes;
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

public class MeltingCategory implements IRecipeCategory<IMeltingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.melting");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_melter.png");

    double scale = 0.03125;

    public MeltingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 14, 108, 42);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.MELTER_ITEM.get()));
    }

    @Override
    public RecipeType<IMeltingRecipe> getRecipeType() {
        return JEIPlugin.MELTING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, IMeltingRecipe recipe, IFocusGroup focuses) {
        int capacity = 4000;
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 14).addIngredients(recipe.getDisplayInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 6).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) recipe.getDisplayOutput().getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredient(ForgeTypes.FLUID_STACK, recipe.getDisplayOutput());
    }
}