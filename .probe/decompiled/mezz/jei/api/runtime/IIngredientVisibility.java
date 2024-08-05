package mezz.jei.api.runtime;

import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;

public interface IIngredientVisibility {

    <V> boolean isIngredientVisible(IIngredientType<V> var1, V var2);

    <V> boolean isIngredientVisible(ITypedIngredient<V> var1);

    void registerListener(IIngredientVisibility.IListener var1);

    public interface IListener {

        <V> void onIngredientVisibilityChanged(ITypedIngredient<V> var1, boolean var2);
    }
}