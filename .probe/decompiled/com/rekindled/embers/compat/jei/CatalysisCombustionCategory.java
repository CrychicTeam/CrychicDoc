package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.ICatalysisCombustionRecipe;
import com.rekindled.embers.util.DecimalFormats;
import com.rekindled.embers.util.Misc;
import java.text.DecimalFormat;
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

public class CatalysisCombustionCategory implements IRecipeCategory<ICatalysisCombustionRecipe> {

    private final IDrawable background;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.catalysis_combustion");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_catalysis_combustion.png");

    public CatalysisCombustionCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 126, 46);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.IGNEM_REACTOR_ITEM.get()));
    }

    @Override
    public RecipeType<ICatalysisCombustionRecipe> getRecipeType() {
        return JEIPlugin.CATALYSIS_COMBUSTION;
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

    public void setRecipe(IRecipeLayoutBuilder builder, ICatalysisCombustionRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.CATALYST, 6, 6).addIngredients(recipe.getDisplayMachine());
        builder.addSlot(RecipeIngredientRole.INPUT, 6, 24).addIngredients(recipe.getDisplayInput());
    }

    public void draw(ICatalysisCombustionRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        DecimalFormat multiplierFormat = DecimalFormats.getDecimalFormat("embers.decimal_format.ember_multiplier");
        Misc.drawComponents(Minecraft.getInstance().font, guiGraphics, 28, 10, Component.translatable("embers.jei.recipe.catalysis_combustion.multiplier", multiplierFormat.format(recipe.getDisplayMultiplier())));
        Misc.drawComponents(Minecraft.getInstance().font, guiGraphics, 28, 28, Component.translatable("embers.jei.recipe.catalysis_combustion.burn_time", recipe.getDisplayTime()));
    }
}