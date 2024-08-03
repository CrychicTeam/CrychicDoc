package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import java.util.Collections;

public class WildcardPathToken extends PathToken {

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (ctx.jsonProvider().isMap(model)) {
            for (String property : ctx.jsonProvider().getPropertyKeys(model)) {
                this.handleObjectProperty(currentPath, model, ctx, Collections.singletonList(property));
            }
        } else if (ctx.jsonProvider().isArray(model)) {
            for (int idx = 0; idx < ctx.jsonProvider().length(model); idx++) {
                try {
                    this.handleArrayIndex(idx, currentPath, model, ctx);
                } catch (PathNotFoundException var7) {
                    if (ctx.options().contains(Option.REQUIRE_PROPERTIES)) {
                        throw var7;
                    }
                }
            }
        }
    }

    @Override
    public boolean isTokenDefinite() {
        return false;
    }

    @Override
    public String getPathFragment() {
        return "[*]";
    }
}