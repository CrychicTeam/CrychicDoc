package mezz.jei.api.recipe;

import java.util.Collection;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;

public interface IFocusFactory {

    <V> IFocus<V> createFocus(RecipeIngredientRole var1, IIngredientType<V> var2, V var3);

    <V> IFocus<V> createFocus(RecipeIngredientRole var1, ITypedIngredient<V> var2);

    IFocusGroup createFocusGroup(Collection<? extends IFocus<?>> var1);

    IFocusGroup getEmptyFocusGroup();
}