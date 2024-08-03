package mezz.jei.api.gui.ingredient;

import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.recipe.RecipeIngredientRole;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;

public interface IRecipeSlotView {

    <T> Stream<T> getIngredients(IIngredientType<T> var1);

    default Stream<ItemStack> getItemStacks() {
        return this.getIngredients(VanillaTypes.ITEM_STACK);
    }

    Stream<ITypedIngredient<?>> getAllIngredients();

    boolean isEmpty();

    default Optional<ItemStack> getDisplayedItemStack() {
        return this.getDisplayedIngredient(VanillaTypes.ITEM_STACK);
    }

    <T> Optional<T> getDisplayedIngredient(IIngredientType<T> var1);

    Optional<ITypedIngredient<?>> getDisplayedIngredient();

    Optional<String> getSlotName();

    RecipeIngredientRole getRole();

    void drawHighlight(GuiGraphics var1, int var2);
}