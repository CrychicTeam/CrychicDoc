package snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric;

import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class AcosHFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        double value = parameterValues[0].getNumberValue().doubleValue();
        if (Double.compare(value, 1.0) < 0) {
            throw new EvaluationException(functionToken, "Value must be greater or equal to one");
        } else {
            return expression.convertDoubleValue(Math.log(value + Math.sqrt(Math.pow(value, 2.0) - 1.0)));
        }
    }
}