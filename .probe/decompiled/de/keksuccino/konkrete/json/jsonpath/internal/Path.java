package de.keksuccino.konkrete.json.jsonpath.internal;

import de.keksuccino.konkrete.json.jsonpath.Configuration;

public interface Path {

    EvaluationContext evaluate(Object var1, Object var2, Configuration var3);

    EvaluationContext evaluate(Object var1, Object var2, Configuration var3, boolean var4);

    boolean isDefinite();

    boolean isFunctionPath();

    boolean isRootPath();
}