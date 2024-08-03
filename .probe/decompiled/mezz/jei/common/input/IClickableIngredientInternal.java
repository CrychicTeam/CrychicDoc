package mezz.jei.common.input;

import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.common.util.ImmutableRect2i;

public interface IClickableIngredientInternal<T> {

    ITypedIngredient<T> getTypedIngredient();

    ImmutableRect2i getArea();

    boolean allowsCheating();

    boolean canClickToFocus();
}