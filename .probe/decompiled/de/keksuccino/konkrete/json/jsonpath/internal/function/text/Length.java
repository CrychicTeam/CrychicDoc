package de.keksuccino.konkrete.json.jsonpath.internal.function.text;

import de.keksuccino.konkrete.json.jsonpath.internal.EvaluationContext;
import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunction;
import de.keksuccino.konkrete.json.jsonpath.internal.path.CompiledPath;
import de.keksuccino.konkrete.json.jsonpath.internal.path.PathToken;
import de.keksuccino.konkrete.json.jsonpath.internal.path.RootPathToken;
import de.keksuccino.konkrete.json.jsonpath.internal.path.WildcardPathToken;
import java.util.List;

public class Length implements PathFunction {

    public static final String TOKEN_NAME = "length";

    @Override
    public Object invoke(String currentPath, PathRef parent, Object model, EvaluationContext ctx, List<Parameter> parameters) {
        if (null != parameters && parameters.size() > 0) {
            Parameter lengthOfParameter = (Parameter) parameters.get(0);
            if (!lengthOfParameter.getPath().isFunctionPath()) {
                Path path = lengthOfParameter.getPath();
                if (path instanceof CompiledPath) {
                    RootPathToken root = ((CompiledPath) path).getRoot();
                    PathToken tail = root.getNext();
                    while (null != tail && null != tail.getNext()) {
                        tail = tail.getNext();
                    }
                    if (null != tail) {
                        tail.setNext(new WildcardPathToken());
                    }
                }
            }
            Object innerModel = ((Parameter) parameters.get(0)).getPath().evaluate(model, model, ctx.configuration()).getValue();
            if (ctx.configuration().jsonProvider().isArray(innerModel)) {
                return ctx.configuration().jsonProvider().length(innerModel);
            }
        }
        if (ctx.configuration().jsonProvider().isArray(model)) {
            return ctx.configuration().jsonProvider().length(model);
        } else {
            return ctx.configuration().jsonProvider().isMap(model) ? ctx.configuration().jsonProvider().length(model) : null;
        }
    }
}