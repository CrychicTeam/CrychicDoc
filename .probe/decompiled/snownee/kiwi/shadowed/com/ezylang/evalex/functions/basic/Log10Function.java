package snownee.kiwi.shadowed.com.ezylang.evalex.functions.basic;

import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value", nonZero = true, nonNegative = true)
public class Log10Function extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        double d = parameterValues[0].getNumberValue().doubleValue();
        return expression.convertDoubleValue(Math.log10(d));
    }
}