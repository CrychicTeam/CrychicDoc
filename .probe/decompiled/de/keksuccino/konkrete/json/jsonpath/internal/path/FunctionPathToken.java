package de.keksuccino.konkrete.json.jsonpath.internal.path;

import de.keksuccino.konkrete.json.jsonpath.internal.Path;
import de.keksuccino.konkrete.json.jsonpath.internal.PathRef;
import de.keksuccino.konkrete.json.jsonpath.internal.function.Parameter;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunction;
import de.keksuccino.konkrete.json.jsonpath.internal.function.PathFunctionFactory;
import de.keksuccino.konkrete.json.jsonpath.internal.function.latebinding.JsonLateBindingValue;
import de.keksuccino.konkrete.json.jsonpath.internal.function.latebinding.PathLateBindingValue;
import java.util.List;

public class FunctionPathToken extends PathToken {

    private final String functionName;

    private final String pathFragment;

    private List<Parameter> functionParams;

    public FunctionPathToken(String pathFragment, List<Parameter> parameters) {
        this.pathFragment = pathFragment + (parameters != null && parameters.size() > 0 ? "(...)" : "()");
        if (null != pathFragment) {
            this.functionName = pathFragment;
            this.functionParams = parameters;
        } else {
            this.functionName = null;
            this.functionParams = null;
        }
    }

    @Override
    public void evaluate(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        PathFunction pathFunction = PathFunctionFactory.newFunction(this.functionName);
        this.evaluateParameters(currentPath, parent, model, ctx);
        Object result = pathFunction.invoke(currentPath, parent, model, ctx, this.functionParams);
        ctx.addResult(currentPath + "." + this.functionName, parent, result);
        this.cleanWildcardPathToken();
        if (!this.isLeaf()) {
            this.next().evaluate(currentPath, parent, result, ctx);
        }
    }

    private void cleanWildcardPathToken() {
        if (null != this.functionParams && this.functionParams.size() > 0) {
            Path path = ((Parameter) this.functionParams.get(0)).getPath();
            if (null != path && !path.isFunctionPath() && path instanceof CompiledPath) {
                RootPathToken root = ((CompiledPath) path).getRoot();
                for (PathToken tail = root.getNext(); null != tail && null != tail.getNext(); tail = tail.getNext()) {
                    if (tail.getNext() instanceof WildcardPathToken) {
                        tail.setNext(tail.getNext().getNext());
                        break;
                    }
                }
            }
        }
    }

    private void evaluateParameters(String currentPath, PathRef parent, Object model, EvaluationContextImpl ctx) {
        if (null != this.functionParams) {
            for (Parameter param : this.functionParams) {
                switch(param.getType()) {
                    case PATH:
                        PathLateBindingValue pathLateBindingValue = new PathLateBindingValue(param.getPath(), ctx.rootDocument(), ctx.configuration());
                        if (!param.hasEvaluated() || !pathLateBindingValue.equals(param.getILateBingValue())) {
                            param.setLateBinding(pathLateBindingValue);
                            param.setEvaluated(true);
                        }
                        break;
                    case JSON:
                        if (!param.hasEvaluated()) {
                            param.setLateBinding(new JsonLateBindingValue(ctx.configuration().jsonProvider(), param));
                            param.setEvaluated(true);
                        }
                }
            }
        }
    }

    @Override
    public boolean isTokenDefinite() {
        return true;
    }

    @Override
    public String getPathFragment() {
        return "." + this.pathFragment;
    }

    public void setParameters(List<Parameter> parameters) {
        this.functionParams = parameters;
    }

    public List<Parameter> getParameters() {
        return this.functionParams;
    }

    public String getFunctionName() {
        return this.functionName;
    }
}