package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IStampingRecipe;
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

public class StampingCategory implements IRecipeCategory<IStampingRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.stamping");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_stamp.png");

    double scale = 0.0625;

    public StampingCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 108, 83);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.STAMPER_ITEM.get()));
    }

    @Override
    public RecipeType<IStampingRecipe> getRecipeType() {
        return JEIPlugin.STAMPING;
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

    public void setRecipe(IRecipeLayoutBuilder builder, IStampingRecipe recipe, IFocusGroup focuses) {
        int amount = 0;
        for (FluidStack fluid : recipe.getDisplayInputFluid().getFluids()) {
            amount = fluid.getAmount();
        }
        builder.addSlot(RecipeIngredientRole.INPUT, 8, 28).addIngredients(recipe.getDisplayInput());
        builder.addSlot(RecipeIngredientRole.CATALYST, 47, 7).addIngredients(recipe.getDisplayStamp());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 84, 28).addItemStack(recipe.getResultItem());
        builder.addSlot(RecipeIngredientRole.INPUT, 47, 48).addTooltipCallback(IngotTooltipCallback.INSTANCE).setFluidRenderer((long) ((int) (1500.0 * this.scale + (double) amount * (1.0 - this.scale))), false, 16, 32).addIngredients(ForgeTypes.FLUID_STACK, recipe.getDisplayInputFluid().getFluids());
    }
}