package mezz.jei.core.search;

import java.util.Collection;
import java.util.function.Consumer;

public interface ISearchStorage<T> {

    void getSearchResults(String var1, Consumer<Collection<T>> var2);

    void getAllElements(Consumer<Collection<T>> var1);

    void put(String var1, T var2);

    String statistics();
}