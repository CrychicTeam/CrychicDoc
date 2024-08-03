package mezz.jei.api.recipe;

import java.util.List;
import java.util.stream.Stream;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.item.ItemStack;

public interface IFocusGroup {

    boolean isEmpty();

    List<IFocus<?>> getAllFocuses();

    Stream<IFocus<?>> getFocuses(RecipeIngredientRole var1);

    <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> var1);

    <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> var1, RecipeIngredientRole var2);

    default Stream<IFocus<ItemStack>> getItemStackFocuses() {
        return this.getFocuses(VanillaTypes.ITEM_STACK);
    }

    default Stream<IFocus<ItemStack>> getItemStackFocuses(RecipeIngredientRole role) {
        return this.getFocuses(VanillaTypes.ITEM_STACK, role);
    }
}