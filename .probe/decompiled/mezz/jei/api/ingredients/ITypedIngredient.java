package mezz.jei.api.ingredients;

import java.util.Optional;
import mezz.jei.api.constants.VanillaTypes;
import net.minecraft.world.item.ItemStack;

public interface ITypedIngredient<T> {

    IIngredientType<T> getType();

    T getIngredient();

    default <V> Optional<V> getIngredient(IIngredientType<V> ingredientType) {
        return ingredientType.castIngredient(this.getIngredient());
    }

    default Optional<ItemStack> getItemStack() {
        return this.getIngredient(VanillaTypes.ITEM_STACK);
    }
}