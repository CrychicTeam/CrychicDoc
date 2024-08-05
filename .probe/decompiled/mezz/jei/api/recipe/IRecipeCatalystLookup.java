package mezz.jei.api.recipe;

import java.util.stream.Stream;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.world.item.ItemStack;

public interface IRecipeCatalystLookup {

    IRecipeCatalystLookup includeHidden();

    Stream<ITypedIngredient<?>> get();

    <S> Stream<S> get(IIngredientType<S> var1);

    default Stream<ItemStack> getItemStack() {
        return this.get(VanillaTypes.ITEM_STACK);
    }
}