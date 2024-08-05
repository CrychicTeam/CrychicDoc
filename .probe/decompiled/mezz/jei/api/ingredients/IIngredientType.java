package mezz.jei.api.ingredients;

import java.util.Optional;
import org.jetbrains.annotations.Nullable;

@FunctionalInterface
public interface IIngredientType<T> {

    Class<? extends T> getIngredientClass();

    default Optional<T> castIngredient(@Nullable Object ingredient) {
        Class<? extends T> ingredientClass = this.getIngredientClass();
        return Optional.ofNullable(ingredient).filter(ingredientClass::isInstance).map(ingredientClass::cast);
    }
}