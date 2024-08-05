package de.keksuccino.konkrete.json.jsonpath;

import de.keksuccino.konkrete.json.jsonpath.spi.mapper.MappingException;

public interface Predicate {

    boolean apply(Predicate.PredicateContext var1);

    public interface PredicateContext {

        Object item();

        <T> T item(Class<T> var1) throws MappingException;

        Object root();

        Configuration configuration();
    }
}