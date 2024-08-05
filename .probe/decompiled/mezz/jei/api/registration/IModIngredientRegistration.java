package mezz.jei.api.registration;

import java.util.Collection;
import mezz.jei.api.helpers.IColorHelper;
import mezz.jei.api.ingredients.IIngredientHelper;
import mezz.jei.api.ingredients.IIngredientRenderer;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.subtypes.ISubtypeManager;

public interface IModIngredientRegistration {

    ISubtypeManager getSubtypeManager();

    IColorHelper getColorHelper();

    <V> void register(IIngredientType<V> var1, Collection<V> var2, IIngredientHelper<V> var3, IIngredientRenderer<V> var4);
}