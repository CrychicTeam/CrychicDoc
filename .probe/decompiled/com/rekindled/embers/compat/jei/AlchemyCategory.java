package com.rekindled.embers.compat.jei;

import com.rekindled.embers.RegistryManager;
import com.rekindled.embers.recipe.IAlchemyRecipe;
import mezz.jei.api.constants.VanillaTypes;
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
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.crafting.CompoundIngredient;

public class AlchemyCategory implements IRecipeCategory<IAlchemyRecipe> {

    private final IDrawable background;

    private final IDrawable pillar;

    private final IDrawable icon;

    public static Component title = Component.translatable("embers.jei.recipe.alchemy");

    public static ResourceLocation texture = new ResourceLocation("embers", "textures/gui/jei_alchemy.png");

    public static ResourceLocation pillarTexture = new ResourceLocation("embers", "textures/gui/jei_alchemy.png");

    public AlchemyCategory(IGuiHelper helper) {
        this.background = helper.createDrawable(texture, 0, 0, 126, 108);
        this.pillar = helper.createDrawable(pillarTexture, 126, 0, 16, 16);
        this.icon = helper.createDrawableIngredient(VanillaTypes.ITEM_STACK, new ItemStack(RegistryManager.ALCHEMY_TABLET_ITEM.get()));
    }

    @Override
    public RecipeType<IAlchemyRecipe> getRecipeType() {
        return JEIPlugin.ALCHEMY;
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

    public void setRecipe(IRecipeLayoutBuilder builder, IAlchemyRecipe recipe, IFocusGroup focuses) {
        builder.addSlot(RecipeIngredientRole.INPUT, 32, 37).addIngredients(recipe.getCenterInput());
        builder.addSlot(RecipeIngredientRole.OUTPUT, 101, 37).addItemStack(recipe.getResultItem());
        Vec3 center = new Vec3(0.0, 30.0, 0.0);
        for (int i = 0; i < recipe.getInputs().size(); i++) {
            Vec3 rotated = center.zRot((float) ((double) i * 2.0 * Math.PI / (double) recipe.getInputs().size()));
            builder.addSlot(RecipeIngredientRole.INPUT, (int) (32.0 + rotated.x()), (int) (29.0 + rotated.y())).addIngredients((Ingredient) recipe.getInputs().get(i));
            Ingredient[] aspecti = new Ingredient[recipe.getAspects().size()];
            for (int j = 0; j < recipe.getAspects().size(); j++) {
                aspecti[j] = (Ingredient) recipe.getAspects().get((j + i) % recipe.getAspects().size());
            }
            builder.addSlot(RecipeIngredientRole.CATALYST, (int) (32.0 + rotated.x()), (int) (45.0 + rotated.y())).addIngredients(CompoundIngredient.of(aspecti)).setBackground(this.pillar, 0, 0);
        }
        for (int i = 0; i < recipe.getAspects().size(); i++) {
            builder.addSlot(RecipeIngredientRole.CATALYST, 63 - 8 * recipe.getAspects().size() + 16 * i, 90).addIngredients((Ingredient) recipe.getAspects().get(i));
        }
    }
}