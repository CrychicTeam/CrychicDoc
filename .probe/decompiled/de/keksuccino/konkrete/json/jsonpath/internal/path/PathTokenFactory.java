package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Predicate;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PathTokenFactory {

    public static RootPathToken createRootPathToken(char token) {
        return new RootPathToken(token);
    }

    public static PathToken createSinglePropertyPathToken(String property, char stringDelimiter) {
        return new PropertyPathToken(Collections.singletonList(property), stringDelimiter);
    }

    public static PathToken createPropertyPathToken(List<String> properties, char stringDelimiter) {
        return new PropertyPathToken(properties, stringDelimiter);
    }

    public static PathToken createSliceArrayPathToken(ArraySliceOperation arraySliceOperation) {
        return new ArraySliceToken(arraySliceOperation);
    }

    public static PathToken createIndexArrayPathToken(ArrayIndexOperation arrayIndexOperation) {
        return new ArrayIndexToken(arrayIndexOperation);
    }

    public static PathToken createWildCardPathToken() {
        return new WildcardPathToken();
    }

    public static PathToken crateScanToken() {
        return new ScanPathToken();
    }

    public static PathToken createPredicatePathToken(Collection<Predicate> predicates) {
        return new PredicatePathToken(predicates);
    }

    public static PathToken createPredicatePathToken(Predicate predicate) {
        return new PredicatePathToken(predicate);
    }

    public static PathToken createFunctionPathToken(String function, List<Parameter> parameters) {
        return new FunctionPathToken(function, parameters);
    }
}