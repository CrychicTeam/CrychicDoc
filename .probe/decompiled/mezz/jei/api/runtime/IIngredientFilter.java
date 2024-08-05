package mezz.jei.api.runtime;

import java.util.List;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.ingredients.IIngredientType;
import net.minecraft.world.item.ItemStack;

public interface IIngredientFilter {

    void setFilterText(String var1);

    String getFilterText();

    default List<ItemStack> getFilteredItemStacks() {
        return this.getFilteredIngredients(VanillaTypes.ITEM_STACK);
    }

    <T> List<T> getFilteredIngredients(IIngredientType<T> var1);
}