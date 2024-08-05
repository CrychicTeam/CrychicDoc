package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.Instant;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

public class DateTimeNowFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        return expression.convertValue(Instant.now());
    }
}