package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.recipe.ParrotFeedingRecipe;
import com.sihenzhang.crockpot.recipe.RangedItem;
import com.sihenzhang.crockpot.util.I18nUtils;
import com.sihenzhang.crockpot.util.NbtUtils;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.List;
import java.util.stream.IntStream;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ParrotFeedingRecipeCategory implements IRecipeCategory<ParrotFeedingRecipe> {

    public static final RecipeType<ParrotFeedingRecipe> RECIPE_TYPE = RecipeType.create("crockpot", "parrot_feeding", ParrotFeedingRecipe.class);

    private final IDrawable background;

    private final IDrawable icon;

    public ParrotFeedingRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 64, 0, 16, 16);
    }

    @Override
    public RecipeType<ParrotFeedingRecipe> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent("jei", "parrot_feeding");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ParrotFeedingRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 8).addIngredients(recipe.getIngredients().get(0));
        RangedItem result = recipe.getResult();
        if (result.isRanged()) {
            List<ItemStack> resultList = IntStream.rangeClosed(result.min, result.max).mapToObj(cnt -> NbtUtils.setLoreString(new ItemStack(result.item, cnt), result.min + "-" + result.max)).toList();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStacks(resultList);
        } else {
            builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStack(new ItemStack(result.item, result.min));
        }
    }
}