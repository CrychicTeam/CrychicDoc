package vectorwing.farmersdelight.client.gui;

import java.util.List;
import javax.annotation.Nonnull;
import net.minecraft.client.gui.screens.recipebook.RecipeBookComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import vectorwing.farmersdelight.common.crafting.CookingPotRecipe;
import vectorwing.farmersdelight.common.utility.TextUtils;

public class CookingPotRecipeBookComponent extends RecipeBookComponent {

    protected static final ResourceLocation RECIPE_BOOK_BUTTONS = new ResourceLocation("farmersdelight", "textures/gui/recipe_book_buttons.png");

    @Override
    protected void initFilterButtonTextures() {
        this.f_100270_.initTextureValues(0, 0, 28, 18, RECIPE_BOOK_BUTTONS);
    }

    public void hide() {
        this.m_100369_(false);
    }

    @Nonnull
    @Override
    protected Component getRecipeFilterName() {
        return TextUtils.getTranslation("container.recipe_book.cookable");
    }

    @Override
    public void setupGhostRecipe(Recipe<?> recipe, List<Slot> slots) {
        ItemStack resultStack = recipe.getResultItem(this.f_100272_.level.m_9598_());
        this.f_100269_.setRecipe(recipe);
        if (((Slot) slots.get(6)).getItem().isEmpty()) {
            this.f_100269_.addIngredient(Ingredient.of(resultStack), ((Slot) slots.get(6)).x, ((Slot) slots.get(6)).y);
        }
        if (recipe instanceof CookingPotRecipe cookingRecipe) {
            ItemStack containerStack = cookingRecipe.getOutputContainer();
            if (!containerStack.isEmpty()) {
                this.f_100269_.addIngredient(Ingredient.of(containerStack), ((Slot) slots.get(7)).x, ((Slot) slots.get(7)).y);
            }
        }
        this.m_135408_(this.f_100271_.getGridWidth(), this.f_100271_.getGridHeight(), this.f_100271_.getResultSlotIndex(), recipe, recipe.getIngredients().iterator(), 0);
    }
}