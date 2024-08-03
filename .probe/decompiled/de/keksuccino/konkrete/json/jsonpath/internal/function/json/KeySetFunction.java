package de.keksuccino.konkrete.json.jsonpath.internal.function.json;

import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunction;
import java.util.List;

public class KeySetFunction implements PathFunction {

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        return ctx.configuration().jsonProvider().isMap(model) ? ctx.configuration().jsonProvider().getPropertyKeys(model) : null;
    }
}