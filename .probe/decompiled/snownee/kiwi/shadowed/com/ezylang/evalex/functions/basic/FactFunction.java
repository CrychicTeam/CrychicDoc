package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "base")
public class FactFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        int number = parameterValues[0].getNumberValue().intValue();
        BigDecimal factorial = BigDecimal.ONE;
        for (int i = 1; i <= number; i++) {
            factorial = factorial.multiply(new BigDecimal(i, expression.getConfiguration().getMathContext()), expression.getConfiguration().getMathContext());
        }
        return expression.convertValue(factorial);
    }
}