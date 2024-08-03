package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.FluidIngredient;
import com.rekindled.embers.recipe.IMixingRecipe;
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
import net.minecraftforge.fluids.FluidStack;

public class MixingCategory implements IRecipeCategory<IMixingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.mixing");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_mixer.png");

    double scale = 0.001;

    public MixingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 108, 124);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.MIXER_CENTRIFUGE_ITEM.get()));
    }

    @Override
    public RecipeType<IMixingRecipe> getRecipeType() {
        return JEIPlugin.MIXING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, IMixingRecipe recipe, IFocusGroup focuses) {
        int count = 0;
        int capacity = 8000;
        for (FluidIngredient ingredient : recipe.getDisplayInputFluids()) {
            if (count > 2) {
                break;
            }
            if (count == 0) {
                builder.addSlot(RecipeIngredientRole.INPUT, 46, 7).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) ((FluidStack) ingredient.getFluids().get(0)).getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredients(ForgeTypes.FLUID_STACK, ingredient.getFluids());
            }
            if (count == 1) {
                builder.addSlot(RecipeIngredientRole.INPUT, 8, 46).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) ((FluidStack) ingredient.getFluids().get(0)).getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredients(ForgeTypes.FLUID_STACK, ingredient.getFluids());
            }
            if (count == 2) {
                builder.addSlot(RecipeIngredientRole.INPUT, 46, 84).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) ((FluidStack) ingredient.getFluids().get(0)).getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredients(ForgeTypes.FLUID_STACK, ingredient.getFluids());
            }
            count++;
        }
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 46).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) ((double) capacity * this.scale + (double) recipe.getDisplayOutput().getAmount() * (1.0 - this.scale))), false, 16, 32).addIngredient(ForgeTypes.FLUID_STACK, recipe.getDisplayOutput());
    }
}