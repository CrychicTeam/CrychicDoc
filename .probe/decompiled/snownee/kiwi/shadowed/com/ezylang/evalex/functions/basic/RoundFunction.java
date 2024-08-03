package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "value"), @FunctionParameter(name = "scale") })
public class RoundFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        EvaluationValue value = parameterValues[0];
        EvaluationValue precision = parameterValues[1];
        return expression.convertValue(value.getNumberValue().setScale(precision.getNumberValue().intValue(), expression.getConfiguration().getMathContext().getRoundingMode()));
    }
}