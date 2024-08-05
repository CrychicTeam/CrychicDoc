package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameter(name = "value")
public class DurationParseFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) {
        String text = parameterValues[0].getStringValue();
        return expression.convertValue(Duration.parse(text));
    }
}