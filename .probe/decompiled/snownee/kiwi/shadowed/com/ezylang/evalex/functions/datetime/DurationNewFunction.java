package snownee.kiwi.shadowed.com.ezylang.evalex.functions.datetime;

import java.time.Duration;
import snownee.kiwi.shadowed.com.ezylang.evalex.EvaluationException;
import snownee.kiwi.shadowed.com.ezylang.evalex.Expression;
import snownee.kiwi.shadowed.com.ezylang.evalex.data.EvaluationValue;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.AbstractFunction;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameter;
import snownee.kiwi.shadowed.com.ezylang.evalex.functions.FunctionParameters;
import snownee.kiwi.shadowed.com.ezylang.evalex.parser.Token;

@FunctionParameters({ @FunctionParameter(name = "days"), @FunctionParameter(name = "parameters", isVarArg = true) })
public class DurationNewFunction extends AbstractFunction {

    @Override
    public EvaluationValue evaluate(Expression expression, Token functionToken, EvaluationValue... parameterValues) throws EvaluationException {
        int parameterLength = parameterValues.length;
        int days = parameterValues[0].getNumberValue().intValue();
        int hours = parameterLength >= 2 ? parameterValues[1].getNumberValue().intValue() : 0;
        int minutes = parameterLength >= 3 ? parameterValues[2].getNumberValue().intValue() : 0;
        int seconds = parameterLength >= 4 ? parameterValues[3].getNumberValue().intValue() : 0;
        int millis = parameterLength >= 5 ? parameterValues[4].getNumberValue().intValue() : 0;
        int nanos = parameterLength == 6 ? parameterValues[5].getNumberValue().intValue() : 0;
        Duration duration = Duration.ofDays((long) days).plusHours((long) hours).plusMinutes((long) minutes).plusSeconds((long) seconds).plusMillis((long) millis).plusNanos((long) nanos);
        return expression.convertValue(duration);
    }
}