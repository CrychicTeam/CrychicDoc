package mezz.jei.api.runtime;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.RecipeType;

public interface IRecipesGui {

    default <V> void show(IFocus<V> focus) {
        this.show(List.of(focus));
    }

    void show(List<IFocus<?>> var1);

    void showTypes(List<RecipeType<?>> var1);

    <T> Optional<T> getIngredientUnderMouse(IIngredientType<T> var1);
}