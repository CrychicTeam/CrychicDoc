package snownee.kiwi.shadowed.com.ezylang.evalex.functions;

import java.util.List;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public interface FunctionIfc {

    List<FunctionParameterDefinition> getFunctionParameterDefinitions();

    EvaluationValue evaluate(Expression var1, Token var2, EvaluationValue... var3) throws EvaluationException;

    void validatePreEvaluation(Token var1, EvaluationValue... var2) throws EvaluationException;

    boolean hasVarArgs();

    default boolean isParameterLazy(int parameterIndex) {
        if (parameterIndex >= this.getFunctionParameterDefinitions().size()) {
            parameterIndex = this.getFunctionParameterDefinitions().size() - 1;
        }
        return ((FunctionParameterDefinition) this.getFunctionParameterDefinitions().get(parameterIndex)).isLazy();
    }

    default int getCountOfNonVarArgParameters() {
        int numOfParameters = this.getFunctionParameterDefinitions().size();
        return this.hasVarArgs() ? numOfParameters - 1 : numOfParameters;
    }
}