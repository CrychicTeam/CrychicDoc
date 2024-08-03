package de.keksuccino.konkrete.json.jsonpath.internal.function.numeric;

import de.keksuccino.konkrete.json.jsonpath.JsonPathException;
import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunction;
import java.util.List;

public abstract class AbstractAggregation implements PathFunction {

    protected abstract void next(Number var1);

    protected abstract Number getValue();

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        int count = 0;
        if (ctx.configuration().jsonProvider().isArray(model)) {
            for (Object obj : ctx.configuration().jsonProvider().toIterable(model)) {
                if (obj instanceof Number value) {
                    count++;
                    this.next(value);
                }
            }
        }
        if (parameters != null) {
            for (Number value : Parameter.toList(Number.class, ctx, parameters)) {
                count++;
                this.next(value);
            }
        }
        if (count != 0) {
            return this.getValue();
        } else {
            throw new JsonPathException("Aggregation function attempted to calculate value using empty array");
        }
    }
}