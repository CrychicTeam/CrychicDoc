package mezz.jei.library.focus;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.recipe.IFocus;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.runtime.IIngredientManager;

public class FocusGroup implements IFocusGroup {

    public static final IFocusGroup EMPTY = new FocusGroup(List.of());

    private final List<IFocus<?>> focuses;

    public static IFocusGroup create(Collection<? extends IFocus<?>> focuses, IIngredientManager ingredientManager) {
        List<Focus<?>> checkedFocuses = focuses.stream().filter(Objects::nonNull).map(f -> Focus.checkOne(f, ingredientManager)).toList();
        return (IFocusGroup) (checkedFocuses.isEmpty() ? EMPTY : new FocusGroup(checkedFocuses));
    }

    private FocusGroup(List<Focus<?>> focuses) {
        this.focuses = List.copyOf(focuses);
    }

    @Override
    public boolean isEmpty() {
        return this.focuses.isEmpty();
    }

    @Override
    public List<IFocus<?>> getAllFocuses() {
        return this.focuses;
    }

    @Override
    public Stream<IFocus<?>> getFocuses(RecipeIngredientRole role) {
        return this.focuses.stream().filter(focus -> focus.getRole() == role);
    }

    @Override
    public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType) {
        return this.focuses.stream().map(focus -> focus.checkedCast(ingredientType)).flatMap(Optional::stream);
    }

    @Override
    public <T> Stream<IFocus<T>> getFocuses(IIngredientType<T> ingredientType, RecipeIngredientRole role) {
        return this.getFocuses(role).map(focus -> focus.checkedCast(ingredientType)).flatMap(Optional::stream);
    }
}