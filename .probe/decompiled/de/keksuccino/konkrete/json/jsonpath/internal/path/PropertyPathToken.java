package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.InvalidPathException;
import de.keksuccino.konkrete.json.jsonpath.Option;
import de.keksuccino.konkrete.json.jsonpath.PathNotFoundException;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.Utils;
import java.util.ArrayList;
import java.util.List;

class PropertyPathToken extends PathToken {

    private final List<String> properties;

    private final String stringDelimiter;

    public PropertyPathToken(List<String> properties, char stringDelimiter) {
        if (properties.isEmpty()) {
            throw new InvalidPathException("Empty properties");
        } else {
            this.properties = properties;
            this.stringDelimiter = Character.toString(stringDelimiter);
        }
    }

    public List<String> getProperties() {
        return this.properties;
    }

    public boolean singlePropertyCase() {
        return this.properties.size() == 1;
    }

    public boolean multiPropertyMergeCase() {
        return this.isLeaf() && this.properties.size() > 1;
    }

    public boolean multiPropertyIterationCase() {
        return !this.isLeaf() && this.properties.size() > 1;
    }

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        assert Utils.onlyOneIsTrueNonThrow(this.singlePropertyCase(), this.multiPropertyMergeCase(), this.multiPropertyIterationCase());
        if (!ctx.jsonProvider().isMap(model)) {
            if (this.isUpstreamDefinite() && !ctx.options().contains(Option.SUPPRESS_EXCEPTIONS)) {
                String m = model == null ? "null" : model.getClass().getName();
                throw new PathNotFoundException(String.format("Expected to find an object with property %s in path %s but found '%s'. This is not a json object according to the JsonProvider: '%s'.", this.getPathFragment(), currentPath, m, ctx.configuration().jsonProvider().getClass().getName()));
            }
        } else if (this.singlePropertyCase() || this.multiPropertyMergeCase()) {
            this.handleObjectProperty(currentPath, model, ctx, this.properties);
        } else {
            assert this.multiPropertyIterationCase();
            List<String> currentlyHandledProperty = new ArrayList(1);
            currentlyHandledProperty.add(null);
            for (String property : this.properties) {
                currentlyHandledProperty.set(0, property);
                this.handleObjectProperty(currentPath, model, ctx, currentlyHandledProperty);
            }
        }
    }

    @Override
    public boolean isTokenDefinite() {
        return this.singlePropertyCase() || this.multiPropertyMergeCase();
    }

    @Override
    public String getPathFragment() {
        return "[" + Utils.join(",", this.stringDelimiter, this.properties) + "]";
    }
}