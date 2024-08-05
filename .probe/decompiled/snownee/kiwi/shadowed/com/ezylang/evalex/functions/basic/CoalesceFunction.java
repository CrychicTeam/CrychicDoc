package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value", isVarArg = true)
public class CoalesceFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        for (EvaluationValue parameter : parameterValues) {
            if (!parameter.isNullValue()) {
                return parameter;
            }
        }
        return EvaluationValue.nullValue();
    }
}