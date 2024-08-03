package mezz.jei.core.search;

import java.util.Collection;
import java.util.function.Consumer;

public interface ISearchable<T> {

    void getSearchResults(String var1, Consumer<Collection<T>> var2);

    void getAllElements(Consumer<Collection<T>> var1);

    default SearchMode getMode() {
        return SearchMode.ENABLED;
    }
}