package mezz.jei.gui.ingredients;

import java.util.Comparator;
import mezz.jei.api.runtime.IIngredientManager;

public interface IIngredientSorter {

    default void doPreSort(IngredientFilter ingredientFilter, IIngredientManager ingredientManager) {
    }

    Comparator<IListElementInfo<?>> getComparator(IngredientFilter var1, IIngredientManager var2);

    default void invalidateCache() {
    }
}