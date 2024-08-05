package mezz.jei.api.runtime;

import mezz.jei.api.ingredients.ITypedIngredient;
import net.minecraft.client.renderer.Rect2i;

public interface IClickableIngredient<T> {

    ITypedIngredient<T> getTypedIngredient();

    Rect2i getArea();
}