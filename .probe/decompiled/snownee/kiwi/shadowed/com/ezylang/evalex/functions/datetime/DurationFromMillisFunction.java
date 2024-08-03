package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.math.BigDecimal;
import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class DurationFromMillisFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        BigDecimal millis = parameterValues[0].getNumberValue();
        return expression.convertValue(Duration.ofMillis(millis.longValue()));
    }
}