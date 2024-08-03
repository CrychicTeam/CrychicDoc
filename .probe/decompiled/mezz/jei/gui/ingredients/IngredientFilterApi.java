package mezz.jei.gui.ingredients;

import java.util.List;
import mezz.jei.api.ingredients.IIngredientType;
import mezz.jei.api.runtime.IIngredientFilter;
import mezz.jei.common.util.ErrorUtil;
import mezz.jei.gui.filter.IFilterTextSource;

public class IngredientFilterApi implements IIngredientFilter {

    private final IngredientFilter ingredientFilter;

    private final IFilterTextSource filterTextSource;

    public IngredientFilterApi(IngredientFilter ingredientFilter, IFilterTextSource filterTextSource) {
        this.ingredientFilter = ingredientFilter;
        this.filterTextSource = filterTextSource;
    }

    @Override
    public String getFilterText() {
        return this.filterTextSource.getFilterText();
    }

    @Override
    public void setFilterText(String filterText) {
        ErrorUtil.checkNotNull(filterText, "filterText");
        this.filterTextSource.setFilterText(filterText);
    }

    @Override
    public <T> List<T> getFilteredIngredients(IIngredientType<T> ingredientType) {
        return this.ingredientFilter.getFilteredIngredients(ingredientType);
    }
}