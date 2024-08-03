package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;

public abstract class ArrayPathToken extends PathToken {

    protected boolean checkArrayModel(String currentPath, Object model, EvaluationContextImpl ctx) {
        if (model == null) {
            if (this.isUpstreamDefinite() && !ctx.options().contains(Option.SUPPRESS_EXCEPTIONS)) {
                throw new PathNotFoundException("The path " + currentPath + " is null");
            } else {
                return false;
            }
        } else if (!ctx.jsonProvider().isArray(model)) {
            if (this.isUpstreamDefinite() && !ctx.options().contains(Option.SUPPRESS_EXCEPTIONS)) {
                throw new PathNotFoundException(String.format("Filter: %s can only be applied to arrays. Current context is: %s", this.toString(), model));
            } else {
                return false;
            }
        } else {
            return true;
        }
    }
}