package com.blamejared.searchables.api.context;

import com.blamejared.searchables.api.SearchableType;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public final class SearchContext<T> {

    private final List<SearchPredicate<T>> predicates = new ArrayList();

    public Predicate<T> createPredicate(SearchableType<T> type) {
        return (Predicate<T>) this.predicates.stream().map(tSearchPredicate -> tSearchPredicate.predicateFrom(type)).reduce((Predicate) t -> true, Predicate::and);
    }

    public void add(SearchPredicate<T> literal) {
        this.predicates.add(literal);
    }
}