package de.keksuccino.konkrete.json.jsonpath.internal.function;

import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import java.util.List;

public interface PathFunction {

    Object invoke(String var1, PathRef var2, Object var3, EvaluationContext var4, List<Parameter> var5);
}