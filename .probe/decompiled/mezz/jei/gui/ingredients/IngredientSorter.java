package mezz.jei.gui.ingredients;

import java.util.Comparator;
import java.util.List;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.common.config.IClientConfig;
import mezz.jei.common.config.IngredientSortStage;
import mezz.jei.gui.config.IngredientTypeSortingConfig;
import mezz.jei.gui.config.ModNameSortingConfig;

public final class IngredientSorter implements IIngredientSorter {

    private static final Comparator<IListElementInfo<?>> PRE_SORTED = Comparator.comparing(IListElementInfo::getSortedIndex);

    private final IClientConfig clientConfig;

    private final ModNameSortingConfig modNameSortingConfig;

    private final IngredientTypeSortingConfig ingredientTypeSortingConfig;

    private boolean isCacheValid;

    public IngredientSorter(IClientConfig clientConfig, ModNameSortingConfig modNameSortingConfig, IngredientTypeSortingConfig ingredientTypeSortingConfig) {
        this.clientConfig = clientConfig;
        this.modNameSortingConfig = modNameSortingConfig;
        this.ingredientTypeSortingConfig = ingredientTypeSortingConfig;
        this.isCacheValid = false;
    }

    @Override
    public void doPreSort(IngredientFilter ingredientFilter, IIngredientManager ingredientManager) {
        IngredientSorterComparators comparators = new IngredientSorterComparators(ingredientFilter, ingredientManager, this.modNameSortingConfig, this.ingredientTypeSortingConfig);
        List<IngredientSortStage> ingredientSorterStages = this.clientConfig.getIngredientSorterStages();
        Comparator<IListElementInfo<?>> completeComparator = comparators.getComparator(ingredientSorterStages);
        List<IListElementInfo<?>> results = ingredientFilter.getIngredientListPreSort(completeComparator);
        int i = 0;
        for (int resultsSize = results.size(); i < resultsSize; i++) {
            IListElementInfo<?> element = (IListElementInfo<?>) results.get(i);
            element.setSortedIndex(i);
        }
        this.isCacheValid = true;
    }

    @Override
    public Comparator<IListElementInfo<?>> getComparator(IngredientFilter ingredientFilter, IIngredientManager ingredientManager) {
        if (!this.isCacheValid) {
            this.doPreSort(ingredientFilter, ingredientManager);
        }
        return PRE_SORTED;
    }

    @Override
    public void invalidateCache() {
        this.isCacheValid = false;
    }
}