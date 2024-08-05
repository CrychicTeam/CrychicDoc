package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IEmberActivationRecipe;
import com.rekindled.embers.util.Misc;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;

public class EmberActivationCategory implements IRecipeCategory<IEmberActivationRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.ember_activation");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_item_text.png");

    public EmberActivationCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 126, 28);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.EMBER_ACTIVATOR_ITEM.get()));
    }

    @Override
    public RecipeType<IEmberActivationRecipe> getRecipeType() {
        return JEIPlugin.EMBER_ACTIVATION;
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

    public void setRecipe(IRecipeLayoutBuilder builder, IEmberActivationRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 6).addIngredients(recipe.getDisplayInput());
    }

    public void draw(IEmberActivationRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        Misc.drawComponents(Minecraft.getInstance().font, guiGraphics, 28, 10, Component.translatable("embers.jei.recipe.ember_activation.ember", recipe.getDisplayOutput()));
    }
}