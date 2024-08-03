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

public class MeltingBonusCategory implements IRecipeCategory<IMeltingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.geologic_separator");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_geologic_separator.png");

    double scale = 0.03125;

    public MeltingBonusCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 14, 108, 42);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.GEOLOGIC_SEPARATOR_ITEM.get()));
    }

    @Override
    public RecipeType<IMeltingRecipe> getRecipeType() {
        return JEIPlugin.MELTING_BONUS;
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
        int capacity2 = 1000;
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 14).addIngredients(recipe.getDisplayInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 63, 6).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) recipe.getDisplayOutput().getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredient(ForgeTypes.FLUID_STACK, recipe.getDisplayOutput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 26).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity2 * this.scale + (double) recipe.getBonus().getAmount() * (1.0 - this.scale))), false, 16, 12).addIngredient(ForgeTypes.FLUID_STACK, recipe.getBonus());
    }
}