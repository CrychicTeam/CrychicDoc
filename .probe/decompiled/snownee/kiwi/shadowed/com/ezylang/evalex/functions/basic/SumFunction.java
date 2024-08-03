package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import java.math.BigDecimal;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value", isVarArg = true)
public class SumFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal sum = BigDecimal.ZERO;
        for (EvaluationValue parameter : parameterValues) {
            sum = sum.add(this.recursiveSum(parameter, expression), expression.getConfiguration().getMathContext());
        }
        return expression.convertValue(sum);
    }

    private BigDecimal recursiveSum(EvaluationValue parameter, Expression expression) {
        BigDecimal sum = BigDecimal.ZERO;
        if (parameter.isArrayValue()) {
            for (EvaluationValue element : parameter.getArrayValue()) {
                sum = sum.add(this.recursiveSum(element, expression), expression.getConfiguration().getMathContext());
            }
        } else {
            sum = sum.add(parameter.getNumberValue(), expression.getConfiguration().getMathContext());
        }
        return sum;
    }
}