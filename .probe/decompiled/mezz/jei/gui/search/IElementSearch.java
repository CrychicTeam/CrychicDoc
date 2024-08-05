package mezz.jei.gui.search;

import java.util.Collection;
import java.util.Set;
import mezz.jei.gui.ingredients.IListElementInfo;

public interface IElementSearch {

    void add(IListElementInfo<?> var1);

    Collection<IListElementInfo<?>> getAllIngredients();

    Set<IListElementInfo<?>> getSearchResults(ElementPrefixParser.TokenInfo var1);

    void logStatistics();
}