package mezz.jei.library.ingredients;

import java.util.List;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IIngredientFilter;

public class IngredientFilterApiDummy implements IIngredientFilter {

    public static final IIngredientFilter INSTANCE = new IngredientFilterApiDummy();

    private IngredientFilterApiDummy() {
    }

    @Override
    public String getFilterText() {
        return "";
    }

    @Override
    public void setFilterText(String filterText) {
    }

    @Override
    public <T> List<T> getFilteredIngredients(IIngredientType<T> ingredientType) {
        return List.of();
    }
}