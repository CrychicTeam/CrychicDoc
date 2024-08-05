package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IBoringRecipe;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.RecipeType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;

public class ExcavationCategory extends BoringCategory {

    public static Component title = Component.translatable("embers.jei.recipe.excavation");

    public ExcavationCategory(IGuiHelper helper) {
        super(helper);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.EXCAVATION_BUCKETS_ITEM.get()));
    }

    @Override
    public RecipeType<IBoringRecipe> getRecipeType() {
        return JEIPlugin.EXCAVATION;
    }

    @Override
    public Component getTitle() {
        return title;
    }
}