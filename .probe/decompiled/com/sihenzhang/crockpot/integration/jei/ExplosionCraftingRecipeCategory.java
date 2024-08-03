package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.integration.jei.gui.DrawableFramed;
import com.sihenzhang.crockpot.recipe.ExplosionCraftingRecipe;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import com.sihenzhang.crockpot.util.StringUtils;
import java.util.List;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.drawable.IDrawableAnimated;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;

public class ExplosionCraftingRecipeCategory implements IRecipeCategory<ExplosionCraftingRecipe> {

    public static final RecipeType<ExplosionCraftingRecipe> RECIPE_TYPE = RecipeType.create("crockpot", "explosion_crafting", ExplosionCraftingRecipe.class);

    private final IDrawable background;

    private final IDrawable icon;

    private final IDrawableAnimated animatedExplosion;

    private final IDrawable onlyBlock;

    public ExplosionCraftingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation recipeGui = RLUtils.createRL("textures/gui/jei/explosion_crafting.png");
        this.background = guiHelper.createDrawable(recipeGui, 0, 0, 127, 46);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 0, 0, 16, 16);
        this.animatedExplosion = new DrawableFramed(guiHelper.createDrawable(recipeGui, 127, 0, 27, 240), 20, 10, IDrawableAnimated.StartDirection.TOP);
        this.onlyBlock = guiHelper.createDrawable(recipeGui, 154, 0, 16, 16);
    }

    @Override
    public RecipeType<ExplosionCraftingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent("jei", "explosion_crafting");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ExplosionCraftingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 19, 10).addIngredients(recipe.getIngredient());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 88, 10).addItemStack(recipe.getResult());
    }

    public void draw(ExplosionCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, GuiGraphics guiGraphics, double mouseX, double mouseY) {
        this.animatedExplosion.draw(guiGraphics, 46, 6);
        if (recipe.isOnlyBlock()) {
            this.onlyBlock.draw(guiGraphics, 21, 29);
        }
        Font font = Minecraft.getInstance().font;
        String chance = StringUtils.format(1.0F - recipe.getLossRate(), "0.##%");
        guiGraphics.drawString(font, chance, 97 - font.width(chance) / 2, 36, -8355712, false);
    }

    public List<Component> getTooltipStrings(ExplosionCraftingRecipe recipe, IRecipeSlotsView recipeSlotsView, double mouseX, double mouseY) {
        return recipe.isOnlyBlock() && mouseX >= 21.0 && mouseX <= 37.0 && mouseY >= 29.0 && mouseY <= 45.0 ? List.of(I18nUtils.createIntegrationComponent("jei", "explosion_crafting.only_block")) : IRecipeCategory.super.getTooltipStrings(recipe, recipeSlotsView, mouseX, mouseY);
    }
}