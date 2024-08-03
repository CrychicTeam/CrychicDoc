package snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class AtanHFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        double value = parameterValues[0].getNumberValue().doubleValue();
        if (Math.abs(value) >= 1.0) {
            throw new EvaluationException(functionToken, "Absolute value must be less than 1");
        } else {
            return expression.convertDoubleValue(0.5 * Math.log((1.0 + value) / (1.0 - value)));
        }
    }
}