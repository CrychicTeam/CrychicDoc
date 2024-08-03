package mezz.jei.api.runtime;

import java.util.List;
import java.util.Optional;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.ingredients.ITypedIngredient;
import org.jetbrains.annotations.Nullable;

public interface IIngredientListOverlay {

    Optional<ITypedIngredient<?>> getIngredientUnderMouse();

    @Nullable
    <T> T getIngredientUnderMouse(IIngredientType<T> var1);

    boolean isListDisplayed();

    boolean hasKeyboardFocus();

    <T> List<T> getVisibleIngredients(IIngredientType<T> var1);
}