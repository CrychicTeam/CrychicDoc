package snownee.kiwi.shadowed.com.ezylang.evalex.functions.trigonometric;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class AsinRFunction extends AbstractFunction {

    private static final BigDecimal MINUS_ONE = BigDecimal.valueOf(-1L);

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        BigDecimal parameterValue = parameterValues[0].getNumberValue();
        if (parameterValue.compareTo(BigDecimal.ONE) > 0) {
            throw new EvaluationException(functionToken, "Illegal asinr(x) for x > 1: x = " + parameterValue);
        } else if (parameterValue.compareTo(MINUS_ONE) < 0) {
            throw new EvaluationException(functionToken, "Illegal asinr(x) for x < -1: x = " + parameterValue);
        } else {
            return expression.convertDoubleValue(Math.asin(parameterValues[0].getNumberValue().doubleValue()));
        }
    }
}