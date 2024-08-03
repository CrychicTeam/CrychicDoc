package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.tag.CrockPotItemTags;
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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.registries.ForgeRegistries;

public class ParrotLayingEggsRecipeCategory implements IRecipeCategory<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> {

    public static final RecipeType<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> RECIPE_TYPE = RecipeType.create("crockpot", "parrot_laying_eggs", ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper.class);

    private final IDrawable background;

    private final IDrawable icon;

    public ParrotLayingEggsRecipeCategory(IGuiHelper guiHelper) {
        this.background = guiHelper.createDrawable(RLUtils.createRL("textures/gui/jei/parrot_feeding.png"), 0, 0, 87, 33);
        this.icon = guiHelper.createDrawable(ModIntegrationJei.ICONS, 48, 0, 16, 16);
    }

    @Override
    public RecipeType<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> getRecipeType() {
        return RECIPE_TYPE;
    }

    @Override
    public Component getTitle() {
        return I18nUtils.createIntegrationComponent("jei", "parrot_laying_eggs");
    }

    @Override
    public IDrawable getBackground() {
        return this.background;
    }

    @Override
    public IDrawable getIcon() {
        return this.icon;
    }

    public void setRecipe(IRecipeLayoutBuilder builder, ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 1, 8).addIngredients(recipe.ingredient);
        List<Item> eggs = ForgeRegistries.ITEMS.tags().getTag(CrockPotItemTags.PARROT_EGGS).stream().toList();
        int[] counts = IntStream.rangeClosed(recipe.min, recipe.max).filter(i -> i != 0).toArray();
        List<ItemStack> result = IntStream.range(0, eggs.size() * counts.length).mapToObj(i -> {
            Item egg = (Item) eggs.get(i % eggs.size());
            int count = counts[i % counts.length];
            ItemStack stack = new ItemStack(egg, count);
            if (recipe.min != recipe.max) {
                NbtUtils.setLoreString(stack, recipe.min + "-" + recipe.max);
            }
            return stack;
        }).toList();
        builder.addSlot(RecipeIngredientRole.OUTPUT, 66, 8).addItemStacks(result);
    }

    public static class ParrotLayingEggsRecipeWrapper {

        Ingredient ingredient;

        int min;

        int max;

        public ParrotLayingEggsRecipeWrapper(Ingredient ingredient, int min, int max) {
            this.ingredient = ingredient;
            this.min = min;
            this.max = max;
        }
    }
}