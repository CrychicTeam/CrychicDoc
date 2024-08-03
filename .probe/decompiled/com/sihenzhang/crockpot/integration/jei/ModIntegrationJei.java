package com.sihenzhang.crockpot.integration.jei;

import com.sihenzhang.crockpot.base.FoodCategory;
import com.sihenzhang.crockpot.block.CrockPotBlock;
import com.sihenzhang.crockpot.client.gui.screen.CrockPotScreen;
import com.sihenzhang.crockpot.item.CrockPotItems;
import com.sihenzhang.crockpot.recipe.CrockPotRecipes;
import com.sihenzhang.crockpot.recipe.FoodValuesDefinition;
import com.sihenzhang.crockpot.recipe.cooking.CrockPotCookingRecipe;
import com.sihenzhang.crockpot.tag.CrockPotBlockTags;
import com.sihenzhang.crockpot.util.RLUtils;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.registration.IGuiHandlerRegistration;
import mezz.jei.api.registration.IRecipeCatalystRegistration;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeManager;
import net.minecraftforge.registries.ForgeRegistries;

@JeiPlugin
public class ModIntegrationJei implements IModPlugin {

    public static final String MOD_ID = "jei";

    public static final ResourceLocation ICONS = RLUtils.createRL("textures/gui/jei/icons.png");

    @Override
    public ResourceLocation getPluginUid() {
        return RLUtils.createRL("crock_pot");
    }

    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper guiHelper = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(new CrockPotCookingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new FoodValuesCategory(guiHelper));
        registration.addRecipeCategories(new ExplosionCraftingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotLayingEggsRecipeCategory(guiHelper));
        registration.addRecipeCategories(new ParrotFeedingRecipeCategory(guiHelper));
        registration.addRecipeCategories(new PiglinBarteringRecipeCategory(guiHelper));
    }

    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        ClientLevel level = Minecraft.getInstance().level;
        RecipeManager recipeManager = level.getRecipeManager();
        registration.addRecipes(CrockPotCookingRecipeCategory.RECIPE_TYPE, recipeManager.<CrockPotCookingRecipe.Wrapper, CrockPotCookingRecipe>getAllRecipesFor(CrockPotRecipes.CROCK_POT_COOKING_RECIPE_TYPE.get()).stream().filter(r -> r.getResult().getItem() != CrockPotItems.AVAJ.get()).toList());
        registration.addRecipes(FoodValuesCategory.RECIPE_TYPE, FoodValuesDefinition.getFoodCategoryMatchedItemsList(level));
        registration.addRecipes(ExplosionCraftingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.EXPLOSION_CRAFTING_RECIPE_TYPE.get()));
        Map<Boolean, List<ItemStack>> meatsGroupByMonster = (Map<Boolean, List<ItemStack>>) FoodValuesDefinition.getMatchedItems(FoodCategory.MEAT, level).stream().collect(Collectors.groupingBy(item -> FoodValuesDefinition.getFoodValues(item, Minecraft.getInstance().level).has(FoodCategory.MONSTER)));
        List<ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper> parrotLayingEggsRecipes = List.of(new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(((List) meatsGroupByMonster.get(false)).stream()), 1, 1), new ParrotLayingEggsRecipeCategory.ParrotLayingEggsRecipeWrapper(Ingredient.of(((List) meatsGroupByMonster.get(true)).stream()), 0, 1));
        registration.addRecipes(ParrotLayingEggsRecipeCategory.RECIPE_TYPE, parrotLayingEggsRecipes);
        registration.addRecipes(ParrotFeedingRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.PARROT_FEEDING_RECIPE_TYPE.get()));
        registration.addRecipes(PiglinBarteringRecipeCategory.RECIPE_TYPE, recipeManager.getAllRecipesFor(CrockPotRecipes.PIGLIN_BARTERING_RECIPE_TYPE.get()));
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        ForgeRegistries.BLOCKS.tags().getTag(CrockPotBlockTags.CROCK_POTS).stream().filter(CrockPotBlock.class::isInstance).map(CrockPotBlock.class::cast).map(block -> block.m_5456_().getDefaultInstance()).forEach(pot -> registration.addRecipeCatalyst(pot, CrockPotCookingRecipeCategory.RECIPE_TYPE));
        registration.addRecipeCatalyst(CrockPotItems.BIRDCAGE.get().getDefaultInstance(), ParrotLayingEggsRecipeCategory.RECIPE_TYPE, ParrotFeedingRecipeCategory.RECIPE_TYPE);
    }

    @Override
    public void registerGuiHandlers(IGuiHandlerRegistration registration) {
        registration.addRecipeClickArea(CrockPotScreen.class, 80, 43, 24, 18, CrockPotCookingRecipeCategory.RECIPE_TYPE);
    }
}