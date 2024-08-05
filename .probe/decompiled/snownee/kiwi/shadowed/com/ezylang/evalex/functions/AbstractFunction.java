package snownee.kiwi.shadowed.com.ezylang.evalex.functions;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public abstract class AbstractFunction implements FunctionIfc {

    private final List<FunctionParameterDefinition> functionParameterDefinitions = new ArrayList();

    private final boolean hasVarArgs;

    protected AbstractFunction() {
        FunctionParameter[] parameterAnnotations = (FunctionParameter[]) this.getClass().getAnnotationsByType(FunctionParameter.class);
        boolean varArgParameterFound = false;
        for (FunctionParameter parameter : parameterAnnotations) {
            if (varArgParameterFound) {
                throw new IllegalArgumentException("Only last parameter may be defined as variable argument");
            }
            if (parameter.isVarArg()) {
                varArgParameterFound = true;
            }
            this.functionParameterDefinitions.add(FunctionParameterDefinition.builder().name(parameter.name()).isVarArg(parameter.isVarArg()).isLazy(parameter.isLazy()).nonZero(parameter.nonZero()).nonNegative(parameter.nonNegative()).build());
        }
        this.hasVarArgs = varArgParameterFound;
    }

    @Override
    public void validatePreEvaluation(Token token, EvaluationValue... parameterValues) throws EvaluationException {
        for (int i = 0; i < parameterValues.length; i++) {
            FunctionParameterDefinition definition = this.getParameterDefinitionForParameter(i);
            if (definition.isNonZero() && parameterValues[i].getNumberValue().equals(BigDecimal.ZERO)) {
                throw new EvaluationException(token, "Parameter must not be zero");
            }
            if (definition.isNonNegative() && parameterValues[i].getNumberValue().signum() < 0) {
                throw new EvaluationException(token, "Parameter must not be negative");
            }
        }
    }

    @Override
    public List<FunctionParameterDefinition> getFunctionParameterDefinitions() {
        return this.functionParameterDefinitions;
    }

    @Override
    public boolean hasVarArgs() {
        return this.hasVarArgs;
    }

    private FunctionParameterDefinition getParameterDefinitionForParameter(int index) {
        if (this.hasVarArgs && index >= this.functionParameterDefinitions.size()) {
            index = this.functionParameterDefinitions.size() - 1;
        }
        return (FunctionParameterDefinition) this.functionParameterDefinitions.get(index);
    }
}