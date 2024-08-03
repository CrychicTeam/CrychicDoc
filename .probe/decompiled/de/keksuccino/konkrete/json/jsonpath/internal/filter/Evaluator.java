package de.keksuccino.konkrete.json.jsonpath.internal.filter;

import de.keksuccino.konkrete.json.jsonpath.Predicate;

public interface Evaluator {

    boolean evaluate(ValueNode var1, ValueNode var2, Predicate.PredicateContext var3);
}