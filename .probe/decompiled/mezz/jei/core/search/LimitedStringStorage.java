package mezz.jei.core.search;

import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Set;
import java.util.function.Consumer;
import mezz.jei.core.collect.SetMultiMap;
import mezz.jei.core.search.suffixtree.GeneralizedSuffixTree;

public class LimitedStringStorage<T> implements ISearchStorage<T> {

    private final SetMultiMap<String, T> multiMap = new SetMultiMap<>(() -> Collections.newSetFromMap(new IdentityHashMap()));

    private final GeneralizedSuffixTree<Set<T>> generalizedSuffixTree = new GeneralizedSuffixTree<>();

    @Override
    public void getSearchResults(String token, Consumer<Collection<T>> resultsConsumer) {
        this.generalizedSuffixTree.getSearchResults(token, resultSet -> {
            for (Collection<T> result : resultSet) {
                resultsConsumer.accept(result);
            }
        });
    }

    @Override
    public void getAllElements(Consumer<Collection<T>> resultsConsumer) {
        Collection<T> values = this.multiMap.allValues();
        resultsConsumer.accept(values);
    }

    @Override
    public void put(String key, T value) {
        boolean isNewKey = !this.multiMap.containsKey(key);
        this.multiMap.put(key, value);
        if (isNewKey) {
            Set<T> set = this.multiMap.get(key);
            this.generalizedSuffixTree.put(key, set);
        }
    }

    @Override
    public String statistics() {
        return "LimitedStringStorage: " + this.generalizedSuffixTree.statistics();
    }
}