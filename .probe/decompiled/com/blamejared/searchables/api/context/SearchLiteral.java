package com.blamejared.searchables.api.context;

import com.blamejared.searchables.api.SearchableComponent;
import com.blamejared.searchables.api.SearchableType;
import java.util.function.Predicate;

record SearchLiteral<T>(String value) implements SearchPredicate<T> {

    @Override
    public Predicate<T> predicateFrom(SearchableType<T> type) {
        return (Predicate<T>) type.defaultComponent().map(SearchableComponent::filter).map(filter -> t -> filter.test(t, this.value())).orElse((Predicate) t -> true);
    }
}