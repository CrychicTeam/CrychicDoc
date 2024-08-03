package mezz.jei.library.gui.recipes;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.gui.ingredient.IRecipeSlotTooltipCallback;
import mezz.jei.api.gui.ingredient.IRecipeSlotView;
import mezz.jei.api.helpers.IModIdHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;

public class OutputSlotTooltipCallback implements IRecipeSlotTooltipCallback {

    private final ResourceLocation recipeName;

    private final IModIdHelper modIdHelper;

    private final IIngredientManager ingredientManager;

    public OutputSlotTooltipCallback(ResourceLocation recipeName, IModIdHelper modIdHelper, IIngredientManager ingredientManager) {
        this.recipeName = recipeName;
        this.modIdHelper = modIdHelper;
        this.ingredientManager = ingredientManager;
    }

    @Override
    public void onTooltip(IRecipeSlotView recipeSlotView, List<Component> tooltip) {
        if (recipeSlotView.getRole() == RecipeIngredientRole.OUTPUT) {
            Optional<ITypedIngredient<?>> displayedIngredient = recipeSlotView.getDisplayedIngredient();
            if (!displayedIngredient.isEmpty()) {
                if (this.modIdHelper.isDisplayingModNameEnabled()) {
                    ResourceLocation ingredientName = this.getResourceLocation((ITypedIngredient) displayedIngredient.get());
                    String recipeModId = this.recipeName.getNamespace();
                    String ingredientModId = ingredientName.getNamespace();
                    if (!recipeModId.equals(ingredientModId)) {
                        String modName = this.modIdHelper.getFormattedModNameForModId(recipeModId);
                        MutableComponent recipeBy = Component.translatable("jei.tooltip.recipe.by", modName);
                        tooltip.add(recipeBy.withStyle(ChatFormatting.GRAY));
                    }
                }
                Minecraft minecraft = Minecraft.getInstance();
                boolean showAdvanced = minecraft.options.advancedItemTooltips || Screen.hasShiftDown();
                if (showAdvanced) {
                    MutableComponent recipeId = Component.translatable("jei.tooltip.recipe.id", this.recipeName.toString());
                    tooltip.add(recipeId.withStyle(ChatFormatting.DARK_GRAY));
                }
            }
        }
    }

    private <T> ResourceLocation getResourceLocation(ITypedIngredient<T> ingredient) {
        IIngredientHelper<T> ingredientHelper = this.ingredientManager.getIngredientHelper(ingredient.getType());
        return ingredientHelper.getResourceLocation(ingredient.getIngredient());
    }
}